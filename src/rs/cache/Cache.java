package rs.cache;

import rs.io.Buffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Cache {

    public static class AccessFile extends RandomAccessFile {
        public AccessFile(String s) throws FileNotFoundException {
            super(s, "rw");
        }

        public int readMedium() throws IOException {
            return (this.readUnsignedShort() << 8) | (this.readUnsignedByte());
        }

        public void writeMedium(int i) throws IOException {
            this.write(i >>> 16);
            this.write(i >>> 8);
            this.write(i);
        }
    }

    /**
     * The header size for our cache's sectors.
     */
    public static final int HEADER_SIZE = 8;

    /**
     * The maximum data that can be stored in a sector.
     */
    public static final int DATA_SIZE = 512;

    /**
     * The index size for our cache's files.
     */
    public static final int INDEX_SIZE = 6;

    /**
     * The total size for each sector including header and data size.
     */
    public static final int SECTOR_SIZE = DATA_SIZE + HEADER_SIZE;

    /**
     * Our static buffer used for write operations.
     */
    public static Buffer buffer = new Buffer(SECTOR_SIZE);

    public final AccessFile data;
    public final AccessFile idx;
    public final int index;

    public Cache(int index, AccessFile data, AccessFile idx) {
        this.index = index;
        this.data = data;
        this.idx = idx;
    }

    public synchronized byte[] get(int file) {
        try {
            this.idx.seek(file * INDEX_SIZE);

            // Read the index information for the requested file.
            int size = this.idx.readMedium();
            int sector = this.idx.readMedium();

            // Setup max sector for bound checking later.
            int max_sector = (int) (this.data.length() / SECTOR_SIZE);

            // If this file doesn't exist.
            if (size <= 0) {
                return null;
            }

            // If the sector doesn't exist.
            if (sector <= 0 || sector > max_sector) {
                return null;
            }

            byte[] payload = new byte[size];

            int part = 0;
            int read = 0;

            // Keep going until we've read the whole file.
            while (read < size) {
                this.data.seek(sector * SECTOR_SIZE);

                // Limit the amount we read per loop cycle, since each sector
                // can only contain so much data.
                int remaining = size - read;

                if (remaining > DATA_SIZE) {
                    remaining = DATA_SIZE;
                }

                // Read the header of the current sector.
                int file_index = this.data.readUnsignedShort();
                int file_part = this.data.readUnsignedShort();
                int next_sector = this.data.readMedium();
                int sector_cache = this.data.readUnsignedByte();

                // Make sure all the information for this sector matches up to the file we're retrieving.
                if (file_index != file) {
                    return null;
                }

                if (file_part != part) {
                    return null;
                }

                if (sector_cache != this.index) {
                    return null;
                }

                if (next_sector < 0 || next_sector > max_sector) {
                    return null;
                }

                // Transfer the data from this sector to our payload.
                this.data.read(payload, read, remaining);

                // Increase the read amount.
                read += remaining;

                // We're now on our next sector. If our file only uses one sector then this doesn't matter.
                sector = next_sector;

                // Increase the current part to keep track of how far into reading the file we are.
                part++;
            }

            return payload;
        } catch (Exception e) {
            return null;
        }
    }

    public int get_file_count() throws IOException {
        return (int) (this.idx.length() / INDEX_SIZE);
    }

    public int get_sector_count() throws IOException {
        return (int) (this.data.length() / SECTOR_SIZE);
    }

    public synchronized boolean put(byte[] data, int file_index) {
        boolean exists = put(data, file_index, true);

        // If the file index we tried to write to doesn't exist or is empty.
        if (!exists) {
            exists = put(data, file_index, false);
        }

        return exists;
    }

    public synchronized boolean put(byte[] file_data, int file_index, boolean check_sectors) {
        try {
            int sector;

            // Check if the file already exists.
            // If so, then we'll just start from the sector it already occupies.
            if (check_sectors) {
                idx.seek(file_index * INDEX_SIZE);

                // Reads the index, but if we reach the end of the file then return false.
                if (idx.read(buffer.payload, 0, INDEX_SIZE) < INDEX_SIZE) {
                    return false;
                }

                buffer.position = 3;
                sector = buffer.get_medium();

                // Check the sector bounds.
                if (sector <= 0 || (long) sector > data.length() / SECTOR_SIZE) {
                    return false;
                }
            } else {
                // The next sector will be a new sector.
                sector = (int) ((data.length() + (SECTOR_SIZE - 1)) / SECTOR_SIZE);

                if (sector == 0) {
                    sector = 1;
                }
            }

            // Update the index information.
            idx.seek(file_index * INDEX_SIZE);
            idx.writeMedium(file_data.length);
            idx.writeMedium(sector);

            int written = 0;

            // Continue looping until we've written all the data.
            for (int file_part = 0; written < file_data.length; file_part++) {
                int next_sector = 0;

                // If the file exists already, and we're trying to replace its sectors.
                if (check_sectors) {
                    data.seek(sector * SECTOR_SIZE);

                    // Reads the header, but if we reach the end of the file or have an invalid header,
                    // then we'll just create a new sector.
                    int read = data.read(buffer.payload, 0, HEADER_SIZE);

                    if (read == HEADER_SIZE) {
                        buffer.position = 0;

                        int file = buffer.get_ushort();
                        int part = buffer.get_ushort();
                        next_sector = buffer.get_medium();
                        int cache = buffer.get_ubyte();

                        // Verify the sector
                        if (file != file_index || part != file_part || cache != index) {
                            return false;
                        }

                        // Check sector bounds
                        if (next_sector < 0 || (long) next_sector > data.length() / SECTOR_SIZE) {
                            return false;
                        }
                    }
                }

                // The only reason next_sector would be 0 by here is if one of these scenarios occur:
                // The file didn't have a next sector.
                // We stopped checking for existing sectors, and are just appending new ones.
                if (next_sector == 0) {
                    check_sectors = false;
                    next_sector = (int) ((data.length() + (SECTOR_SIZE - 1)) / SECTOR_SIZE);

                    // If our data file doesn't exist yet, then we start SECTOR_SIZE bytes into it.
                    if (next_sector == 0) {
                        next_sector++;
                    }

                    // If our next sector is our current sector, then just go to the next.
                    if (next_sector == sector) {
                        next_sector++;
                    }
                }

                int remaining = file_data.length - written;

                // The remaining data ends in our current sector, so we set it to 0 so it writes in this sector
                // that there is no next sector.
                if (remaining <= DATA_SIZE) {
                    next_sector = 0;
                }

                // Write the sector header.
                data.seek(sector * SECTOR_SIZE);
                data.writeShort(file_index);
                data.writeShort(file_part);
                data.writeMedium(next_sector);
                data.writeByte(this.index);

                // We can only write so much data into one sector, so we're clamping how much is being written.
                if (remaining > DATA_SIZE) {
                    remaining = DATA_SIZE;
                }

                // Write the data.
                data.write(file_data, written, remaining);

                // Increase our written amount so our loop will eventually stop.
                written += remaining;

                // Set the current sector to the next.
                sector = next_sector;
            }

            return true;
        } catch (IOException _ex) {
            return false;
        }
    }

}
