package rs.audio;

import javax.sound.midi.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Ripped from 414
 *
 * @author WalrusViking
 */
public class MusicPlayer implements Receiver {

    public static final int ALL_NOTES_OFF = 0x7B;
    public static final int ALL_SOUND_OFF = 0x78;
    public static final int LSB_BANK_SELECT = 0x20;
    public static final int LSB_CHANNEL_VOLUME = 0x27;

    /* Constants */
    public static final int MSB_BANK_SELECT = 0;
    public static final int MSB_CHANNEL_VOLUME = 0x7;
    public static final int RESET_CONTROLLERS = 0x79;

    private static final int method1004(int i) {
        return (int) (Math.log((double) i * 0.00390625) * 868.5889638065036 + 0.5);
    }

    public byte[] buffer;
    public int currentSong = -1;
    public boolean fetch;
    public int fvar1, fvar2, fvar3;

    public int[] frequency = new int[16];
    public int loop1, loop2;
    public boolean loopmusic1, loopmusic2;

    public Receiver receiver;
    public Sequencer sequencer;
    public InputStream stream;
    public int tmpDelay = 0;
    public int var1 = -1, var2 = 0, var3 = 0, var4 = 0;
    public int volume0 = 255, volume1 = 256, volume2, volume3;

    {
        Arrays.fill(this.frequency, 12800);
    }

    public MusicPlayer() {
        try {
            this.receiver = MidiSystem.getReceiver();
            this.sequencer = MidiSystem.getSequencer(false);
            this.sequencer.getTransmitter().setReceiver(this);
            this.sequencer.open();
            this.reset(-1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean _send(int status, int data1, int data2, long time) {
        if ((status & 0xF0) == ShortMessage.CONTROL_CHANGE) {
            switch (data1) {
                case RESET_CONTROLLERS: {
                    this.send(status, data1, data2, time);

                    int channel = status & 0xF;
                    this.frequency[channel] = 12800;
                    int i = this.get_volume(channel);

                    this.send(status, MSB_CHANNEL_VOLUME, i >> 7, time);
                    this.send(status, LSB_CHANNEL_VOLUME, i & 0x7F, time);
                    return true;
                }
                case MSB_CHANNEL_VOLUME:
                case LSB_CHANNEL_VOLUME: {
                    int channel = status & 0xF;

                    if (data1 == MSB_CHANNEL_VOLUME) {
                        this.frequency[channel] = ((this.frequency[channel] & 0x7F) + (data2 << 7));
                    } else {
                        this.frequency[channel] = ((this.frequency[channel] & 0x3F80) + data2);
                    }

                    int i = this.get_volume(channel);
                    this.send(status, MSB_CHANNEL_VOLUME, i >> 7, time);
                    this.send(status, LSB_CHANNEL_VOLUME, i & 0x7F, time);
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public final void close() {
        /* empty */
    }

    private int get_volume(int channel) {
        int i = this.frequency[channel];
        i = (i * this.volume1 >> 8) * i;
        return (int) (Math.sqrt(i) + 0.5D);
    }

    public void halt() {
        if (this.stream != null) {
            try {
                this.stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.stream = null;
        }
        if (this.sequencer != null) {
            this.sequencer.close();
            this.sequencer = null;
        }
        if (this.receiver != null) {
            this.receiver.close();
            this.receiver = null;
        }
    }

    public void handle() {
        if (this.fetch) {
            if (this.buffer != null) {
                if (loop1 >= 0) {
                    this.method684(this.loopmusic1, this.loop1, this.volume2, this.buffer);
                } else if (loop2 >= 0) {
                    this.method899(this.loop2, this.loopmusic1, this.buffer, this.volume2);
                } else {
                    this.method853(this.volume2, this.buffer, this.loopmusic1);
                }
                this.fetch = false;
            }
        }

        if (var1 < 0) {
            if (var2 > 0) {
                var2--;
                if (var2 == 0) {
                    if (this.buffer == null)
                        this.setVolume(256);
                    else {
                        this.setVolume(volume2);
                        var1 = volume2;
                        this.play(this.buffer, this.volume2, this.loopmusic2);
                        this.buffer = null;
                    }
                    var3 = 0;
                }
            }
        } else if (var2 > 0) {
            var3 += var4;
            this.setVolumeVelocity(var1, var3);
            var2--;
            if (var2 == 0) {
                this.stop();
                var2 = 20;
                var1 = -1;
            }
        }
    }

    public void method684(boolean loopMusic1, int musicLoop1, int musicVolume2, byte[] buffer) {
        if (var1 >= 0) {
            var4 = loop1;
            if (var1 != 0) {
                int i_4_ = method1004(var1);
                i_4_ -= var3;
                var2 = (i_4_ + 3600) / loop1;
                if (var2 < 1)
                    var2 = 1;
            } else
                var2 = 1;
            this.buffer = buffer;
            this.volume3 = volume2;
            loopmusic2 = loopMusic1;
        } else if (var2 == 0)
            method853(volume2, buffer, loopMusic1);
        else {
            this.volume3 = volume2;
            loopmusic2 = loopMusic1;
            this.buffer = buffer;
        }
    }

    public void method853(int volume2, byte[] buffer, boolean loopMusic1) {
        if (var1 >= 0) {
            this.stop();
            var1 = -1;
            this.buffer = null;
            var2 = 20;
            var3 = 0;
        }
        if (buffer != null) {
            if (var2 > 0) {
                this.setVolume(volume2);
                var2 = 0;
            }
            var1 = volume2;
            this.play(buffer, volume2, loopMusic1);
        }
    }

    public void method899(int musicLoop2, boolean loopMusic2, byte[] buffer, int volume2) {
        if (var1 >= 0) {
            musicLoop2 -= 20;
            if (musicLoop2 < 1)
                musicLoop2 = 1;
            var2 = musicLoop2;
            if (var1 == 0)
                var4 = 0;
            else {
                int i_31_ = method1004(var1);
                i_31_ -= var3;
                var4 = ((var4 - 1 + (i_31_ + 3600)) / var4);
            }
            this.loopmusic2 = loopMusic2;
            this.buffer = buffer;
            this.volume3 = volume2;
        } else if (var2 != 0) {
            this.loopmusic2 = loopMusic2;
            this.buffer = buffer;
            this.volume3 = volume2;
        } else {
            method853(volume2, buffer, loopMusic2);
        }
    }

    public void play(byte[] buffer, int volume, boolean loop) {
        try {
            this.sequencer.setSequence(MidiSystem.getSequence(new ByteArrayInputStream(buffer)));
            this.sequencer.setLoopCount(loop ? Sequencer.LOOP_CONTINUOUSLY : 0);
            this.setVolume(volume, 0, -1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset(long time) {
        for (int c = 0; c < 16; ++c) {
            this.send(ShortMessage.CONTROL_CHANGE + c, ALL_NOTES_OFF, 0, time);
        }
        for (int c = 0; c < 16; ++c) {
            this.send(ShortMessage.CONTROL_CHANGE + c, ALL_SOUND_OFF, 0, time);
        }
        for (int c = 0; c < 16; ++c) {
            this.send(ShortMessage.CONTROL_CHANGE + c, RESET_CONTROLLERS, 0, time);
        }
        for (int c = 0; c < 16; ++c) {
            this.send(ShortMessage.CONTROL_CHANGE + c, MSB_BANK_SELECT, 0, time);
        }
        for (int c = 0; c < 16; ++c) {
            this.send(ShortMessage.CONTROL_CHANGE + c, LSB_BANK_SELECT, 0, time);
        }
        for (int c = 0; c < 16; ++c) {
            this.send(ShortMessage.PROGRAM_CHANGE + c, 0, 0, time);
        }
    }

    public void send(int status, int data1, int data2, long time) {
        try {
            ShortMessage s = new ShortMessage();
            s.setMessage(status, data1, data2);
            this.receiver.send(s, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(MidiMessage m, long time) {
        byte[] b = m.getMessage();
        if (b.length < 3 || !this._send(b[0], b[1], b[2], time)) {
            this.receiver.send(m, time);
        }
    }

    public void set_volume(int volume, long timeStamp) {
        if (this.volume1 != volume) {
            this.volume1 = volume;

            Arrays.fill(this.frequency, 12800);

            for (int c = 0; c < 16; ++c) {
                int i = this.get_volume(c);
                this.send(ShortMessage.CONTROL_CHANGE + c, MSB_CHANNEL_VOLUME, i >> 7, timeStamp);
                this.send(ShortMessage.CONTROL_CHANGE + c, LSB_CHANNEL_VOLUME, i & 0x7F, timeStamp);
            }
        }
    }

    public void setVolume(int volume) {
        if (this.sequencer != null) {
            this.set_volume(volume, -1L);
        }
    }

    public synchronized void setVolume(int volume, int velocity, long timeStamp) {
        volume = (int) (volume * Math.pow(0.1D, velocity * 0.0005D) + 0.5D);

        if (this.volume1 != volume) {
            this.volume1 = volume;

            for (int c = 0; c < 16; ++c) {
                int i = this.get_volume(c);
                this.send(ShortMessage.CONTROL_CHANGE + c, LSB_CHANNEL_VOLUME, i >> 7, timeStamp);
                this.send(ShortMessage.CONTROL_CHANGE + c, MSB_CHANNEL_VOLUME, i & 0x7F, timeStamp);
            }
        }
    }

    public synchronized void setVolumeVelocity(int volume, int velocity) {
        if (this.sequencer != null) {
            this.setVolume(volume, velocity, -1L);
        }
    }

    public void stop() {
        if (this.sequencer != null) {
            this.sequencer.stop();
            this.reset(-1L);
        }
    }

}
