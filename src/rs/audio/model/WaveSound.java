package rs.audio.model;

import rs.io.Buffer;

/*
 * Please ignore this class for now.
 */
public class WaveSound {

    public static Buffer buffer;
    public static WaveSound cache[] = new WaveSound[5000];
    public static byte data[];
    public static int delay[] = new int[5000];

    public static Buffer get(int sound_type, int sound_index) {
        if (cache[sound_index] != null) {
            return cache[sound_index].get_header(sound_type);
        } else {
            return null;
        }
    }

    public static void unpack(Buffer buffer) {
        WaveSound.data = new byte[0x6BAA8];
        WaveSound.buffer = new Buffer(data);
        SoundSample.init();

        do {
            int index = buffer.get_ushort();
            if (index == 65535) {
                return;
            }
            WaveSound.cache[index] = new WaveSound();
            WaveSound.cache[index].method242(buffer);
            WaveSound.delay[index] = cache[index].method243(0);
        } while (true);
    }

    public boolean aBoolean321;
    public SoundSample sample[];
    public int anInt324;
    public int anInt330;
    public int anInt331;

    public WaveSound() {
        aBoolean321 = true;
        sample = new SoundSample[10];
    }

    public Buffer get_header(int i) {
        int subchunk2_size = method245(i);

        buffer.position = 0;

        buffer.put_int(0x52494646); // Chunk ID ("RIFF")
        buffer.put_le_int(36 + subchunk2_size); //Chunk Size
        buffer.put_int(0x57415645); //Format ("WAVE")

        buffer.put_int(0x666d7420); //SubChunk1 ID ("fmt ")
        buffer.put_le_int(16); // SubChunk1 Size
        buffer.put_le_short(1); // Audio Format (1 for PCM i.e. Linear quantization)
        buffer.put_le_short(1); // Num Channels(1 = Mono, 2 = Stereo)
        buffer.put_le_int(22050); // Sample Rate
        buffer.put_le_int(22050); // Byte Rate ( SampleRate * NumChannels * BitsPerSample / 8 )
        buffer.put_le_short(1); // Block Align ( NumChannels * BitsPerSample / 8 )
        buffer.put_le_short(8); // Bits Per Sample

        buffer.put_int(0x64617461); //SubChunk2 ID ("data")
        buffer.put_le_int(subchunk2_size); // SubChunk2 Size

        buffer.position += subchunk2_size;

        return buffer;
    }

    public void method242(Buffer b) {
        for (int i = 0; i < 10; i++) {
            int j = b.get_ubyte();
            if (j != 0) {
                b.position--;
                sample[i] = new SoundSample();
                sample[i].decode(b);
            }
        }
        anInt330 = b.get_ushort();
        anInt331 = b.get_ushort();
    }

    public int method243(int i) {
        int j = 0x98967F;

        if (i != 0) {
            anInt324 = -52;
        }

        for (int k = 0; k < 10; k++) {
            if (sample[k] != null && sample[k].remaining / 20 < j) {
                j = sample[k].remaining / 20;
            }
        }

        if (anInt330 < anInt331 && anInt330 / 20 < j) {
            j = anInt330 / 20;
        }

        if (j == 0x98967F || j == 0) {
            return 0;
        }

        for (int l = 0; l < 10; l++) {
            if (sample[l] != null) {
                sample[l].remaining -= j * 20;
            }
        }

        if (anInt330 < anInt331) {
            anInt330 -= j * 20;
            anInt331 -= j * 20;
        }
        return j;
    }

    public int method245(int num) {
        int i = 0;

        for (int k = 0; k < 10; k++) {
            if (sample[k] != null && sample[k].position + sample[k].remaining > i) {
                i = sample[k].position + sample[k].remaining;
            }
        }

        if (i == 0) {
            return 0;
        }

        int i1 = (22050 * i) / 1000;
        int i2 = (22050 * anInt330) / 1000;
        int i3 = (22050 * anInt331) / 1000;

        if (i2 < 0 || i2 > i1 || i3 < 0 || i3 > i1 || i2 >= i3) {
            num = 0;
        }

        int i4 = i1 + (i3 - i2) * (num - 1);

        for (int j = 44; j < i4 + 44; j++) {
            data[j] = -128;
        }

        for (int j = 0; j < 10; j++) {
            if (sample[j] != null) {
                int j1 = (sample[j].position * 22050) / 1000;
                int j2 = (sample[j].remaining * 22050) / 1000;
                int j3[] = sample[j].synthesize(j1, sample[j].position);

                for (int k = 0; k < j1; k++) {
                    data[k + j2 + 44] += (byte) (j3[k] >> 8);
                }

            }
        }

        if (num > 1) {
            i2 += 44;
            i3 += 44;
            i1 += 44;
            int j = (i4 += 44) - i1;

            for (int k = i1 - 1; k >= i3; k--) {
                data[k + j] = data[k];
            }

            for (int k = 1; k < num; k++) {
                int j1 = (i3 - i2) * k;

                for (int l = i2; l < i3; l++) {
                    data[l + j1] = data[l];
                }
            }

            i4 -= 44;
        }

        return i4;
    }

}
