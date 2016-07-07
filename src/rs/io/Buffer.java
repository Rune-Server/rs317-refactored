package rs.io;

import rs.node.CacheLink;
import rs.util.JString;

import java.math.BigInteger;

public class Buffer extends CacheLink {

    public static final int[] BIT_MASK;

    static {
        BIT_MASK = new int[32];
        for (int i = 0; i < BIT_MASK.length; i++) {
            BIT_MASK[i] = (1 << i) - 1;
        }
    }

    public static Buffer create(int type) {
        Buffer b = new Buffer();
        b.position = 0;

        switch (type) {
            case 0:
                b.payload = new byte[100];
                break;
            case 1:
                b.payload = new byte[5000];
                break;
            default:
                b.payload = new byte[30000];
                break;
        }

        return b;
    }

    public int bit_off;
    public ISAACCipher isaac;
    public byte[] payload;
    public int position;

    public Buffer() {
    }

    public Buffer(byte[] payload) {
        this.payload = payload;
        this.position = 0;
    }

    public Buffer(int i) {
        this.payload = new byte[i];
        this.position = 0;
    }

    public void encrypt(BigInteger exponent, BigInteger modulus) {
        int start = this.position;
        this.position = 0;

        // Get the payload
        byte data[] = new byte[start];
        this.get_bytes(data, 0, start);

        // Encrypt the payload
        data = new BigInteger(data).modPow(exponent, modulus).toByteArray();
        this.position = 0;

        // Write the length and encrypted bytes.
        this.put_byte(data.length);
        this.put_bytes(data, data.length, 0);
    }

    public int get_bits(int bits) {
        int pos = this.bit_off >> 3;
        int l = 8 - (this.bit_off & 7);
        int value = 0;
        this.bit_off += bits;
        for (; bits > l; l = 8) {
            value += (this.payload[pos++] & BIT_MASK[l]) << bits - l;
            bits -= l;
        }

        if (bits == l) {
            value += this.payload[pos] & BIT_MASK[l];
        } else {
            value += this.payload[pos] >> l - bits & BIT_MASK[bits];
        }
        return value;
    }

    public boolean get_bool() {
        return (this.payload[this.position++] & 0xFF) == 1;
    }

    public byte get_byte() {
        return this.payload[this.position++];
    }

    public byte get_byte_c() {
        return (byte) -this.get_byte();
    }

    public byte get_byte_s() {
        return (byte) (128 - this.get_byte());
    }

    public void get_bytes(byte dst[], int off, int len) {
        for (int i = off; i < off + len; i++) {
            dst[i] = this.payload[this.position++];
        }
    }

    public void get_bytes_reversed(byte src[], int off, int len) {
        for (int k = (off + len) - 1; k >= off; k--) {
            src[k] = this.payload[this.position++];
        }
    }

    public int get_ime_int() {
        this.position += 4;
        return ((this.payload[this.position - 3] & 0xFF) << 24) + ((this.payload[this.position - 4] & 0xFF) << 16) + ((this.payload[this.position - 1] & 0xFF) << 8) + (this.payload[this.position - 2] & 0xFF);
    }

    public int get_int() {
        this.position += 4;
        return ((this.payload[position - 4] & 0xFF) << 24) + ((this.payload[position - 3] & 0xFF) << 16) + ((this.payload[position - 2] & 0xFF) << 8) + (this.payload[position - 1] & 0xFF);
    }

    public int get_le_short() {
        this.position += 2;
        int i = ((this.payload[this.position - 1] & 0xFF) << 8) + (this.payload[this.position - 2] & 0xFF);
        if (i > 0x7FFF) {
            i -= 0x10000;
        }
        return i;
    }

    public int get_le_ushort() {
        this.position += 2;
        return ((this.payload[this.position - 1] & 0xFF) << 8) + (this.payload[this.position - 2] & 0xFF);
    }

    public int get_le_ushort_a() {
        this.position += 2;
        int i = ((this.payload[this.position - 1] & 0xFF) << 8) + (this.payload[this.position - 2] - 128 & 0xFF);
        if (i > 0x7FFF) {
            i -= 0x10000;
        }
        return i;
    }

    public long get_long() {
        return (((long) get_int() & 0xFFffffffL) << 32) + ((long) get_int() & 0xFFffffffL);
    }

    public int get_me_int() {
        this.position += 4;
        return ((this.payload[this.position - 2] & 0xFF) << 24) + ((this.payload[this.position - 1] & 0xFF) << 16) + ((this.payload[this.position - 4] & 0xFF) << 8) + (this.payload[this.position - 3] & 0xFF);
    }

    public int get_medium() {
        this.position += 3;
        return ((this.payload[position - 3] & 0xFF) << 16) + ((this.payload[position - 2] & 0xFF) << 8) + (this.payload[position - 1] & 0xFF);
    }

    public int get_short() {
        this.position += 2;
        int i = ((this.payload[position - 2] & 0xFF) << 8) + (this.payload[position - 1] & 0xFF);
        if (i > 0x7FFF) {
            i -= 0x10000;
        }
        return i;
    }

    public int get_short_a() {
        this.position += 2;
        int j = ((this.payload[this.position - 1] & 0xFF) << 8) + (this.payload[this.position - 2] - 128 & 0xFF);
        if (j > 0x7FFF) {
            j -= 0x10000;
        }
        return j;
    }

    public int get_smart() {
        int i = this.payload[this.position] & 0xFF;
        if (i < 0x80) {
            return this.get_ubyte() - 0x40;
        } else {
            return this.get_ushort() - 0xC000;
        }
    }

    public String get_string() {
        int start = this.position;
        while (this.payload[position++] != 10)
            ;
        return new String(this.payload, start, this.position - start - 1);
    }

    public byte[] get_string_bytes() {
        int start = this.position;
        while (this.payload[this.position++] != 10)
            ;
        byte[] data = new byte[this.position - start - 1];
        for (int i = start; i < this.position - 1; i++) {
            data[i - start] = this.payload[i];
        }
        return data;
    }

    public int get_ubyte() {
        return this.payload[this.position++] & 0xFF;
    }

    public int get_ubyte_a() {
        return this.payload[this.position++] - 128 & 0xFF;
    }

    public int get_ubyte_c() {
        return -this.payload[this.position++] & 0xFF;
    }

    public int get_ubyte_s() {
        return 128 - this.payload[this.position++] & 0xFF;
    }

    public String get_username() {
        return JString.get_formatted_string(this.get_long());
    }

    public int get_ushort() {
        this.position += 2;
        return ((this.payload[position - 2] & 0xFF) << 8) + (this.payload[position - 1] & 0xFF);
    }

    public int get_ushort_a() {
        this.position += 2;
        return ((this.payload[this.position - 2] & 0xFF) << 8) + (this.payload[this.position - 1] - 128 & 0xFF);
    }

    public int get_usmart() {
        int i = this.payload[this.position] & 0xFF;
        if (i < 0x80) {
            return this.get_ubyte();
        } else {
            return this.get_ushort() - 32768;
        }
    }

    public void put_bool(boolean b) {
        this.put_byte(b ? 1 : 0);
    }

    public void put_byte(int i) {
        this.payload[this.position++] = (byte) i;
    }

    public void put_byte_c(int i) {
        this.payload[this.position++] = (byte) (-i);
    }

    public void put_byte_s(int i) {
        this.payload[this.position++] = (byte) (128 - i);
    }

    public void put_bytes(byte src[], int off, int pos) {
        for (int i = pos; i < pos + off; i++) {
            this.payload[this.position++] = src[i];
        }
    }

    public void put_bytes_reversed_add(byte[] src, int off, int len) {
        for (int i = (off + len) - 1; i >= off; i--) {
            this.payload[this.position++] = (byte) (src[i] + 128);
        }
    }

    public void put_int(int i) {
        this.payload[this.position++] = (byte) (i >> 24);
        this.payload[this.position++] = (byte) (i >> 16);
        this.payload[this.position++] = (byte) (i >> 8);
        this.payload[this.position++] = (byte) i;
    }

    public void put_le_int(int j) {
        this.payload[this.position++] = (byte) j;
        this.payload[this.position++] = (byte) (j >> 8);
        this.payload[this.position++] = (byte) (j >> 16);
        this.payload[this.position++] = (byte) (j >> 24);
    }

    public void put_le_short(int i) {
        this.payload[this.position++] = (byte) i;
        this.payload[this.position++] = (byte) (i >> 8);
    }

    public void put_le_short_a(int i) {
        this.payload[this.position++] = (byte) (i + 128);
        this.payload[this.position++] = (byte) (i >> 8);
    }

    public void put_length(int len) {
        this.payload[this.position - len - 1] = (byte) len;
    }

    public void put_long(long l) {
        this.payload[this.position++] = (byte) (int) (l >> 56);
        this.payload[this.position++] = (byte) (int) (l >> 48);
        this.payload[this.position++] = (byte) (int) (l >> 40);
        this.payload[this.position++] = (byte) (int) (l >> 32);
        this.payload[this.position++] = (byte) (int) (l >> 24);
        this.payload[this.position++] = (byte) (int) (l >> 16);
        this.payload[this.position++] = (byte) (int) (l >> 8);
        this.payload[this.position++] = (byte) (int) l;
    }

    public void put_medium(int i) {
        this.payload[this.position++] = (byte) (i >> 16);
        this.payload[this.position++] = (byte) (i >> 8);
        this.payload[this.position++] = (byte) i;
    }

    public void put_opcode(int i) {
        this.payload[this.position++] = (byte) (i + this.isaac.nextInt());
    }

    public void put_short(int i) {
        this.payload[this.position++] = (byte) (i >> 8);
        this.payload[this.position++] = (byte) i;
    }

    public void put_short_a(int i) {
        this.payload[this.position++] = (byte) (i >> 8);
        this.payload[this.position++] = (byte) (i + 128);
    }

    public void put_string(String s) {
        System.arraycopy(s.getBytes(), 0, this.payload, this.position, s.length());
        this.position += s.length();
        this.payload[this.position++] = 10;
    }

    public void start_bit_access() {
        this.bit_off = this.position * 8;
    }

    public void stop_bit_access() {
        this.position = (this.bit_off + 7) / 8;
    }

}
