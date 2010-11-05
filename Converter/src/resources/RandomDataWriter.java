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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Інтэрфэйс для запісу бінарных блёкаў.
 */
public class RandomDataWriter {
    static final byte[] PADDING;
    static {
        try {
            PADDING = "PADDINGXXPADDING".getBytes("UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private byte buf[] = new byte[0];
    private int count, pos;

    public void writeToFile(File f) throws IOException {
        FileOutputStream out = new FileOutputStream(f);
        out.write(buf, 0, count);
        out.close();
    }

    public byte[] getBytes() {
        byte[] r = new byte[count];
        System.arraycopy(buf, 0, r, 0, count);
        return r;
    }

    public int length() {
        return count;
    }

    public void seek(int p) {
        pos = p;
    }

    public int pos() {
        return pos;
    }

    public void writeFully(byte[] a) {
        int newpos = pos + a.length;
        if (newpos > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newpos));
        }
        System.arraycopy(a, 0, buf, pos, a.length);
        pos = newpos;
        if (count < pos) {
            count = pos;
        }
    }

    public final void writeLong(long v) {
        write((byte) (v >>> 0));
        write((byte) (v >>> 8));
        write((byte) (v >>> 16));
        write((byte) (v >>> 24));
        write((byte) (v >>> 32));
        write((byte) (v >>> 40));
        write((byte) (v >>> 48));
        write((byte) (v >>> 56));
    }

    public final void writeDWord(long v) {
        write((byte) (v >>> 0));
        write((byte) (v >>> 8));
        write((byte) (v >>> 16));
        write((byte) (v >>> 24));
    }

    public void writeInt(int v) {
        write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v >>> 16) & 0xFF);
        write((v >>> 24) & 0xFF);
    }

    public void writeInt(long v) {
        write((byte) (v >>> 0));
        write((byte) (v >>> 8));
        write((byte) (v >>> 16));
        write((byte) (v >>> 24));
    }

    public void writeWord(int v) {
        write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
    }

    public void writeShort(short v) {
        write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
    }

    public void writeShort(int v) {
        write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
    }

    public void writeByte(byte v) {
        write(v);
    }

    public synchronized void write(int b) {
        if (pos + 1 > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, pos + 1));
        }
        buf[pos] = (byte) b;
        pos++;
        if (count < pos) {
            count = pos;
        }
    }

    public void padding32bit() {
        int c = ResUtils.ceil(pos, 4) - pos;
        byte[] p = new byte[c];
        System.arraycopy(PADDING, 0, p, 0, c);
        writeFully(p);
    }
    
    public void padding32bit0() {
        while (pos%4!=0) {
            write(0);
        }
    }


    public void padding512bytes() {
        writeFully(PADDING);
        int c = ResUtils.ceil(pos, 512) - pos;
        byte[] p = new byte[c];
        for (int pp = 0; pp < c; pp += PADDING.length) {
            System.arraycopy(PADDING, 0, p, pp, Math.min(PADDING.length, c - pp));
        }

        writeFully(p);
    }
}
