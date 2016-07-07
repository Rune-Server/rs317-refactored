package rs.bzip2;

public class BZip2 {

    public static BZip2Context aClass32_305 = new BZip2Context();

    public static int decompress(byte dst[], int dst_off, byte src[], int src_len, int src_off) {
        synchronized (aClass32_305) {
            aClass32_305.aByteArray563 = src;
            aClass32_305.anInt564 = src_off;
            aClass32_305.aByteArray568 = dst;
            aClass32_305.anInt569 = 0;
            aClass32_305.anInt565 = src_len;
            aClass32_305.anInt570 = dst_off;
            aClass32_305.anInt577 = 0;
            aClass32_305.anInt576 = 0;
            aClass32_305.anInt566 = 0;
            aClass32_305.anInt567 = 0;
            aClass32_305.anInt571 = 0;
            aClass32_305.anInt572 = 0;
            aClass32_305.anInt579 = 0;
            method227(aClass32_305);
            dst_off -= aClass32_305.anInt570;
            BZip2Context.anIntArray587 = null;
            return dst_off;
        }
    }

    public static void method226(BZip2Context c) {
        byte byte4 = c.aByte573;
        int i = c.anInt574;
        int j = c.anInt584;
        int k = c.anInt582;
        int ai[] = BZip2Context.anIntArray587;
        int l = c.anInt581;
        byte abyte0[] = c.aByteArray568;
        int i1 = c.anInt569;
        int j1 = c.anInt570;
        int k1 = j1;
        int l1 = c.anInt601 + 1;
        label0:
        do {
            if (i > 0) {
                do {
                    if (j1 == 0) {
                        break label0;
                    }
                    if (i == 1) {
                        break;
                    }
                    abyte0[i1] = byte4;
                    i--;
                    i1++;
                    j1--;
                } while (true);
                if (j1 == 0) {
                    i = 1;
                    break;
                }
                abyte0[i1] = byte4;
                i1++;
                j1--;
            }
            boolean flag = true;
            while (flag) {
                flag = false;
                if (j == l1) {
                    i = 0;
                    break label0;
                }
                byte4 = (byte) k;
                l = ai[l];
                byte byte0 = (byte) (l & 0xff);
                l >>= 8;
                j++;
                if (byte0 != k) {
                    k = byte0;
                    if (j1 == 0) {
                        i = 1;
                    } else {
                        abyte0[i1] = byte4;
                        i1++;
                        j1--;
                        flag = true;
                        continue;
                    }
                    break label0;
                }
                if (j != l1) {
                    continue;
                }
                if (j1 == 0) {
                    i = 1;
                    break label0;
                }
                abyte0[i1] = byte4;
                i1++;
                j1--;
                flag = true;
            }
            i = 2;
            l = ai[l];
            byte byte1 = (byte) (l & 0xff);
            l >>= 8;
            if (++j != l1) {
                if (byte1 != k) {
                    k = byte1;
                } else {
                    i = 3;
                    l = ai[l];
                    byte byte2 = (byte) (l & 0xff);
                    l >>= 8;
                    if (++j != l1) {
                        if (byte2 != k) {
                            k = byte2;
                        } else {
                            l = ai[l];
                            byte byte3 = (byte) (l & 0xff);
                            l >>= 8;
                            j++;
                            i = (byte3 & 0xff) + 4;
                            l = ai[l];
                            k = (byte) (l & 0xff);
                            l >>= 8;
                            j++;
                        }
                    }
                }
            }
        } while (true);
        int i2 = c.anInt571;
        c.anInt571 += k1 - j1;
        if (c.anInt571 < i2) {
            c.anInt572++;
        }
        c.aByte573 = byte4;
        c.anInt574 = i;
        c.anInt584 = j;
        c.anInt582 = k;
        BZip2Context.anIntArray587 = ai;
        c.anInt581 = l;
        c.aByteArray568 = abyte0;
        c.anInt569 = i1;
        c.anInt570 = j1;
    }

    public static void method227(BZip2Context c) {
        int k8 = 0;
        int ai[] = null;
        int ai1[] = null;
        int ai2[] = null;
        c.anInt578 = 1;
        if (BZip2Context.anIntArray587 == null) {
            BZip2Context.anIntArray587 = new int[c.anInt578 * 0x186a0];
        }
        boolean flag19 = true;
        while (flag19) {
            byte byte0 = method228(c);
            if (byte0 == 23) {
                return;
            }
            byte0 = method228(c);
            byte0 = method228(c);
            byte0 = method228(c);
            byte0 = method228(c);
            byte0 = method228(c);
            c.anInt579++;
            byte0 = method228(c);
            byte0 = method228(c);
            byte0 = method228(c);
            byte0 = method228(c);
            byte0 = method229(c);
            if (byte0 != 0) {
                c.aBoolean575 = true;
            } else {
                c.aBoolean575 = false;
            }
            if (c.aBoolean575) {
                System.out.println("PANIC! RANDOMISED BLOCK!");
            }
            c.anInt580 = 0;
            byte0 = method228(c);
            c.anInt580 = c.anInt580 << 8 | byte0 & 0xff;
            byte0 = method228(c);
            c.anInt580 = c.anInt580 << 8 | byte0 & 0xff;
            byte0 = method228(c);
            c.anInt580 = c.anInt580 << 8 | byte0 & 0xff;
            for (int j = 0; j < 16; j++) {
                byte byte1 = method229(c);
                if (byte1 == 1) {
                    c.aBooleanArray590[j] = true;
                } else {
                    c.aBooleanArray590[j] = false;
                }
            }

            for (int k = 0; k < 256; k++) {
                c.aBooleanArray589[k] = false;
            }

            for (int l = 0; l < 16; l++) {
                if (c.aBooleanArray590[l]) {
                    for (int i3 = 0; i3 < 16; i3++) {
                        byte byte2 = method229(c);
                        if (byte2 == 1) {
                            c.aBooleanArray589[l * 16 + i3] = true;
                        }
                    }

                }
            }

            method231(c);
            int i4 = c.anInt588 + 2;
            int j4 = method230(3, c);
            int k4 = method230(15, c);
            for (int i1 = 0; i1 < k4; i1++) {
                int j3 = 0;
                do {
                    byte byte3 = method229(c);
                    if (byte3 == 0) {
                        break;
                    }
                    j3++;
                } while (true);
                c.aByteArray595[i1] = (byte) j3;
            }

            byte abyte0[] = new byte[6];
            for (byte byte16 = 0; byte16 < j4; byte16++) {
                abyte0[byte16] = byte16;
            }

            for (int j1 = 0; j1 < k4; j1++) {
                byte byte17 = c.aByteArray595[j1];
                byte byte15 = abyte0[byte17];
                for (; byte17 > 0; byte17--) {
                    abyte0[byte17] = abyte0[byte17 - 1];
                }

                abyte0[0] = byte15;
                c.aByteArray594[j1] = byte15;
            }

            for (int k3 = 0; k3 < j4; k3++) {
                int l6 = method230(5, c);
                for (int k1 = 0; k1 < i4; k1++) {
                    do {
                        byte byte4 = method229(c);
                        if (byte4 == 0) {
                            break;
                        }
                        byte4 = method229(c);
                        if (byte4 == 0) {
                            l6++;
                        } else {
                            l6--;
                        }
                    } while (true);
                    c.aByteArrayArray596[k3][k1] = (byte) l6;
                }

            }

            for (int l3 = 0; l3 < j4; l3++) {
                byte byte8 = 32;
                int i = 0;
                for (int l1 = 0; l1 < i4; l1++) {
                    if (c.aByteArrayArray596[l3][l1] > i) {
                        i = c.aByteArrayArray596[l3][l1];
                    }
                    if (c.aByteArrayArray596[l3][l1] < byte8) {
                        byte8 = c.aByteArrayArray596[l3][l1];
                    }
                }

                method232(c.anIntArrayArray597[l3], c.anIntArrayArray598[l3], c.anIntArrayArray599[l3], c.aByteArrayArray596[l3], byte8, i, i4);
                c.anIntArray600[l3] = byte8;
            }

            int l4 = c.anInt588 + 1;
            int i5 = -1;
            int j5 = 0;
            for (int i2 = 0; i2 <= 255; i2++) {
                c.anIntArray583[i2] = 0;
            }

            int j9 = 4095;
            for (int l8 = 15; l8 >= 0; l8--) {
                for (int i9 = 15; i9 >= 0; i9--) {
                    c.aByteArray592[j9] = (byte) (l8 * 16 + i9);
                    j9--;
                }

                c.anIntArray593[l8] = j9 + 1;
            }

            int i6 = 0;
            if (j5 == 0) {
                i5++;
                j5 = 50;
                byte byte12 = c.aByteArray594[i5];
                k8 = c.anIntArray600[byte12];
                ai = c.anIntArrayArray597[byte12];
                ai2 = c.anIntArrayArray599[byte12];
                ai1 = c.anIntArrayArray598[byte12];
            }
            j5--;
            int i7 = k8;
            int l7;
            byte byte9;
            for (l7 = method230(i7, c); l7 > ai[i7]; l7 = l7 << 1 | byte9) {
                i7++;
                byte9 = method229(c);
            }

            for (int k5 = ai2[l7 - ai1[i7]]; k5 != l4; ) {
                if (k5 == 0 || k5 == 1) {
                    int j6 = -1;
                    int k6 = 1;
                    do {
                        if (k5 == 0) {
                            j6 += k6;
                        } else if (k5 == 1) {
                            j6 += 2 * k6;
                        }
                        k6 *= 2;
                        if (j5 == 0) {
                            i5++;
                            j5 = 50;
                            byte byte13 = c.aByteArray594[i5];
                            k8 = c.anIntArray600[byte13];
                            ai = c.anIntArrayArray597[byte13];
                            ai2 = c.anIntArrayArray599[byte13];
                            ai1 = c.anIntArrayArray598[byte13];
                        }
                        j5--;
                        int j7 = k8;
                        int i8;
                        byte byte10;
                        for (i8 = method230(j7, c); i8 > ai[j7]; i8 = i8 << 1 | byte10) {
                            j7++;
                            byte10 = method229(c);
                        }

                        k5 = ai2[i8 - ai1[j7]];
                    } while (k5 == 0 || k5 == 1);
                    j6++;
                    byte byte5 = c.aByteArray591[c.aByteArray592[c.anIntArray593[0]] & 0xff];
                    c.anIntArray583[byte5 & 0xff] += j6;
                    for (; j6 > 0; j6--) {
                        BZip2Context.anIntArray587[i6] = byte5 & 0xff;
                        i6++;
                    }

                } else {
                    int j11 = k5 - 1;
                    byte byte6;
                    if (j11 < 16) {
                        int j10 = c.anIntArray593[0];
                        byte6 = c.aByteArray592[j10 + j11];
                        for (; j11 > 3; j11 -= 4) {
                            int k11 = j10 + j11;
                            c.aByteArray592[k11] = c.aByteArray592[k11 - 1];
                            c.aByteArray592[k11 - 1] = c.aByteArray592[k11 - 2];
                            c.aByteArray592[k11 - 2] = c.aByteArray592[k11 - 3];
                            c.aByteArray592[k11 - 3] = c.aByteArray592[k11 - 4];
                        }

                        for (; j11 > 0; j11--) {
                            c.aByteArray592[j10 + j11] = c.aByteArray592[(j10 + j11) - 1];
                        }

                        c.aByteArray592[j10] = byte6;
                    } else {
                        int l10 = j11 / 16;
                        int i11 = j11 % 16;
                        int k10 = c.anIntArray593[l10] + i11;
                        byte6 = c.aByteArray592[k10];
                        for (; k10 > c.anIntArray593[l10]; k10--) {
                            c.aByteArray592[k10] = c.aByteArray592[k10 - 1];
                        }

                        c.anIntArray593[l10]++;
                        for (; l10 > 0; l10--) {
                            c.anIntArray593[l10]--;
                            c.aByteArray592[c.anIntArray593[l10]] = c.aByteArray592[(c.anIntArray593[l10 - 1] + 16) - 1];
                        }

                        c.anIntArray593[0]--;
                        c.aByteArray592[c.anIntArray593[0]] = byte6;
                        if (c.anIntArray593[0] == 0) {
                            int i10 = 4095;
                            for (int k9 = 15; k9 >= 0; k9--) {
                                for (int l9 = 15; l9 >= 0; l9--) {
                                    c.aByteArray592[i10] = c.aByteArray592[c.anIntArray593[k9] + l9];
                                    i10--;
                                }

                                c.anIntArray593[k9] = i10 + 1;
                            }

                        }
                    }
                    c.anIntArray583[c.aByteArray591[byte6 & 0xff] & 0xff]++;
                    BZip2Context.anIntArray587[i6] = c.aByteArray591[byte6 & 0xff] & 0xff;
                    i6++;
                    if (j5 == 0) {
                        i5++;
                        j5 = 50;
                        byte byte14 = c.aByteArray594[i5];
                        k8 = c.anIntArray600[byte14];
                        ai = c.anIntArrayArray597[byte14];
                        ai2 = c.anIntArrayArray599[byte14];
                        ai1 = c.anIntArrayArray598[byte14];
                    }
                    j5--;
                    int k7 = k8;
                    int j8;
                    byte byte11;
                    for (j8 = method230(k7, c); j8 > ai[k7]; j8 = j8 << 1 | byte11) {
                        k7++;
                        byte11 = method229(c);
                    }

                    k5 = ai2[j8 - ai1[k7]];
                }
            }

            c.anInt574 = 0;
            c.aByte573 = 0;
            c.anIntArray585[0] = 0;
            for (int j2 = 1; j2 <= 256; j2++) {
                c.anIntArray585[j2] = c.anIntArray583[j2 - 1];
            }

            for (int k2 = 1; k2 <= 256; k2++) {
                c.anIntArray585[k2] += c.anIntArray585[k2 - 1];
            }

            for (int l2 = 0; l2 < i6; l2++) {
                byte byte7 = (byte) (BZip2Context.anIntArray587[l2] & 0xff);
                BZip2Context.anIntArray587[c.anIntArray585[byte7 & 0xff]] |= l2 << 8;
                c.anIntArray585[byte7 & 0xff]++;
            }

            c.anInt581 = BZip2Context.anIntArray587[c.anInt580] >> 8;
            c.anInt584 = 0;
            c.anInt581 = BZip2Context.anIntArray587[c.anInt581];
            c.anInt582 = (byte) (c.anInt581 & 0xff);
            c.anInt581 >>= 8;
            c.anInt584++;
            c.anInt601 = i6;
            method226(c);
            if (c.anInt584 == c.anInt601 + 1 && c.anInt574 == 0) {
                flag19 = true;
            } else {
                flag19 = false;
            }
        }
    }

    public static byte method228(BZip2Context class32) {
        return (byte) method230(8, class32);
    }

    public static byte method229(BZip2Context class32) {
        return (byte) method230(1, class32);
    }

    public static int method230(int i, BZip2Context class32) {
        int j;
        do {
            if (class32.anInt577 >= i) {
                int k = class32.anInt576 >> class32.anInt577 - i & (1 << i) - 1;
                class32.anInt577 -= i;
                j = k;
                break;
            }
            class32.anInt576 = class32.anInt576 << 8 | class32.aByteArray563[class32.anInt564] & 0xff;
            class32.anInt577 += 8;
            class32.anInt564++;
            class32.anInt565--;
            class32.anInt566++;
            if (class32.anInt566 == 0) {
                class32.anInt567++;
            }
        } while (true);
        return j;
    }

    public static void method231(BZip2Context class32) {
        class32.anInt588 = 0;
        for (int i = 0; i < 256; i++) {
            if (class32.aBooleanArray589[i]) {
                class32.aByteArray591[class32.anInt588] = (byte) i;
                class32.anInt588++;
            }
        }

    }

    public static void method232(int ai[], int ai1[], int ai2[], byte abyte0[], int i, int j, int k) {
        int l = 0;
        for (int i1 = i; i1 <= j; i1++) {
            for (int l2 = 0; l2 < k; l2++) {
                if (abyte0[l2] == i1) {
                    ai2[l] = l2;
                    l++;
                }
            }

        }

        for (int j1 = 0; j1 < 23; j1++) {
            ai1[j1] = 0;
        }

        for (int k1 = 0; k1 < k; k1++) {
            ai1[abyte0[k1] + 1]++;
        }

        for (int l1 = 1; l1 < 23; l1++) {
            ai1[l1] += ai1[l1 - 1];
        }

        for (int i2 = 0; i2 < 23; i2++) {
            ai[i2] = 0;
        }

        int i3 = 0;
        for (int j2 = i; j2 <= j; j2++) {
            i3 += ai1[j2 + 1] - ai1[j2];
            ai[j2] = i3 - 1;
            i3 <<= 1;
        }

        for (int k2 = i + 1; k2 <= j; k2++) {
            ai1[k2] = (ai[k2 - 1] + 1 << 1) - ai1[k2];
        }

    }

}
