/**************************************************************************
 Converter for Widnows MUI files.

 Copyright (C) 2010 Alex Buloichik <alex73mail@gmail.com>

 This program is free software; you can redistribute it and/or modify 
 it under the terms of the GNU General Public License as published by 
 the Free Software Foundation; either version 3 of the License, or 
 (at your option) any later version.

 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License 
 along with this program; if not, see http://www.gnu.org/licenses.
 **************************************************************************/

package resources;

/**
 * Інтэрфэйс для чытыньня бінарных блёкаў.
 */
public class RandomDataReader {
    private final byte[] data;
    private int pos;

    public RandomDataReader(byte[] data) {
        this.data = data;
    }

    public byte[] getBytes() {
        byte[] r = new byte[data.length];
        System.arraycopy(data, 0, r, 0, data.length);
        return r;
    }

    public int length() {
        return data.length;
    }

    public int pos() {
        return pos;
    }

    public void seek(int pos) {
        this.pos = pos;
    }

    public long readLong() {
        int i2 = readInt();
        int i1 = readInt();
        return ((long) (i1) << 32) + (i2 & 0xFFFFFFFFL);
    }

    public int read() {
        int r = data[pos++];
        if (r < 0) {
            r = 256 + r;
        }
        return r;
    }

    public int readInt() {
        int ch4 = read();
        int ch3 = read();
        int ch2 = read();
        int ch1 = read();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public short readShort() {
        int ch2 = read();
        int ch1 = read();
        return (short) ((ch1 << 8) + (ch2 << 0));
    }

    public int readWord() {
        int ch2 = read();
        int ch1 = read();
        return ((ch1 << 8) + (ch2 << 0));
    }

    public long readDWord() {
        long ch4 = read();
        long ch3 = read();
        long ch2 = read();
        long ch1 = read();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public byte readByte() {
        return (byte) read();
    }

    public void readFully(byte b[]) {
        System.arraycopy(data, pos, b, 0, b.length);
        pos += b.length;
    }

    public void padding32bit() {
        while (pos % 4 != 0) {
            pos++;
        }
    }
}
