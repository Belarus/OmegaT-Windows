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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Дэкампілятар рэсурсаў вэрсій.
 */
public class ParserVersion  {
    static final byte[] MARK = "VS_VERSION_INFO\0\0".getBytes(ResUtils.UNICODE);
    static final byte[] SFI = "StringFileInfo".getBytes(ResUtils.UNICODE);
    static final int TYPE_STRING = 1;
    static final int TYPE_BIN = 0;

    public VersionInfo vi;

    public void parse(Object ID, RandomDataReader data, PrintWriter out) throws Exception {
        vi = new VersionInfo(data);

        StringBlock sfi = new StringBlock(data, TYPE_STRING, false);
        StringBlock sfiLang = new StringBlock(new RandomDataReader(sfi.children), TYPE_STRING, false);
        RandomDataReader sfiData = new RandomDataReader(sfiLang.children);
        List<StringBlock> sfiValues = new ArrayList<StringBlock>();
        try {
        while (sfiData.pos() < sfiData.length()) {
            StringBlock bl = new StringBlock(sfiData, TYPE_STRING, true);
            sfiValues.add(bl);
        }
        } catch(Exception ex) {
            // TODO hack: блёк вэрсій можа быць невалідны, але вэрсію можна прачытаць
        }
        StringBlock vfi = new StringBlock(data, TYPE_STRING, false);
        RandomDataReader vfiData = new RandomDataReader(vfi.children);
        List<StringBlock> vfiValues = new ArrayList<StringBlock>();
        while (vfiData.pos() < vfiData.length()) {
            vfiValues.add(new StringBlock(vfiData, TYPE_BIN, true));
        }

        // StringFileInfo sfi = new StringFileInfo(data);
        // StringTable st = new StringTable(data);
        // RandomData sd = new RandomData(sfil.children);
        // List<SString> sfiValues = new ArrayList<SString>();
        // while (sd.pos() < sd.length()) {
        // sfiValues.add(new SString(sd));
        // }

        out.println(ID + " VERSIONINFO");
        out.print("FILEVERSION ");
        out.print((vi.dwFileVersionMS >> 16) + "," + (vi.dwFileVersionMS & 0xFFFF) + ",");
        out.println((vi.dwFileVersionLS >> 16) + "," + (vi.dwFileVersionLS & 0xFFFF));
        out.print("PRODUCTVERSION ");
        out.print((vi.dwProductVersionMS >> 16) + "," + (vi.dwProductVersionMS & 0xFFFF) + ",");
        out.println((vi.dwProductVersionLS >> 16) + "," + (vi.dwProductVersionLS & 0xFFFF));
        out.println("FILEFLAGSMASK 0x" + Long.toHexString(vi.dwFileFlagsMask));
        out.println("FILEFLAGS 0x" + Long.toHexString(vi.dwFileFlags));
        out.println("FILEOS 0x" + Long.toHexString(vi.dwFileOS));
        out.println("FILETYPE 0x" + Long.toHexString(vi.dwFileType));
        out.println("FILESUBTYPE 0x" + Long.toHexString(vi.dwFileSubtype));
        out.println("{");
        out.println("  BLOCK \"StringFileInfo\"");
        out.println("  {");
        out.println("    BLOCK \"040904B0\"");
        out.println("    {");
        for (StringBlock ss : sfiValues) {
            out.println("      VALUE \"" + ss.key + "\", \"" + ss.value + "\"");
        }
        out.println("    }");
        out.println("  }");
        out.println("  BLOCK \"VarFileInfo\"");
        out.println("  {");
        for (StringBlock ss : vfiValues) {
            out.print("    VALUE \"" + ss.key + "\",");
            for (int v : ss.binValues) {
                out.print(" 0x" + Integer.toHexString(v));
            }
            out.println();
        }
        out.println("  }");
        out.println("}");
        out.println();
    }

    public static class VersionInfo {
        int Length;
        int ValueLength;
        int Type;

        long dwSignature;
        long dwStrucVersion;
        public long dwFileVersionMS;
        public long dwFileVersionLS;
        long dwProductVersionMS;
        long dwProductVersionLS;
        long dwFileFlagsMask;
        long dwFileFlags;
        long dwFileOS;
        long dwFileType;
        long dwFileSubtype;
        long dwFileDateMS;
        long dwFileDateLS;

        public VersionInfo(RandomDataReader data) throws Exception {
            Length = data.readWord();
            ValueLength = data.readWord();
            Type = data.readWord();
            byte[] mark = new byte[MARK.length];
            data.readFully(mark);
            if (!Arrays.equals(mark, MARK)) {
                throw new Exception("Invalid version mark");
            }

            dwSignature = data.readDWord();
            if (dwSignature != 0xFEEF04BDL) {
                throw new Exception("Invalid version signature");
            }
            dwStrucVersion = data.readDWord();
            dwFileVersionMS = data.readDWord();
            dwFileVersionLS = data.readDWord();
            dwProductVersionMS = data.readDWord();
            dwProductVersionLS = data.readDWord();
            dwFileFlagsMask = data.readDWord();
            dwFileFlags = data.readDWord();
            dwFileOS = data.readDWord();
            dwFileType = data.readDWord();
            dwFileSubtype = data.readDWord();
            dwFileDateMS = data.readDWord();
            dwFileDateLS = data.readDWord();
        }
    }

    static class StringBlock {
        int wLength;
        int wValueLength;
        int wType;
        String key, value;
        byte[] children;
        List<Integer> binValues;

        public StringBlock(RandomDataReader data, int requiredType, boolean loadValues) throws Exception {
            int pos = data.pos();
            wLength = data.readWord();
            wValueLength = data.readWord();
            if (!loadValues && wValueLength != 0) {
                throw new Exception("Invalid wValueLength");
            }
            wType = data.readWord();
            if (wType != requiredType) {
                throw new Exception("Invalid wType="+wType);
            }
            key = "";
            int wc;
            while ((wc = data.readWord()) != 0) {
                key += (char) wc;
            }
            data.padding32bit();
            if (loadValues) {
                if (wType == TYPE_STRING) {
                    value = "";
                    while ((wc = data.readWord()) != 0) {
                        value += (char) wc;
                    }
                } else {
                    binValues = new ArrayList<Integer>();
                    for (int i = 0; i < wValueLength / 2; i++) {
                        binValues.add(data.readWord());
                    }
                }
            } else {
                children = new byte[wLength - (data.pos() - pos)];
                data.readFully(children);
            }
            data.padding32bit();
        }
    }

    static class StringFileInfo {
        int wLength;
        int wValueLength;
        int wType;
        int szKey;

        public StringFileInfo(RandomDataReader data) throws Exception {
            wLength = data.readWord();
            wValueLength = data.readWord();
            if (wValueLength != 0) {
                throw new Exception("Invalid version wValueLength");
            }
            wType = data.readWord();
            if (wType != 1) {
                throw new Exception("Invalid StringFileInfo type");
            }
            byte[] sfi = new byte[SFI.length];
            data.readFully(sfi);
            if (!Arrays.equals(sfi, SFI)) {
                throw new Exception("Invalid version SFI mark");
            }
            data.padding32bit();
        }
    }

    static class StringTable {
        int wLength;
        int wValueLength;
        int wType;
        String szKey;
        byte[] children;

        public StringTable(RandomDataReader data) throws Exception {
            wLength = data.readWord();

            wValueLength = data.readWord();
            wType = data.readWord();
            byte[] szKeyb = new byte[16];
            data.readFully(szKeyb);
            szKey = new String(szKeyb, ResUtils.UNICODE);
            data.padding32bit();
            children = new byte[wLength - 24];
            data.readFully(children);
        }
    }

    static class SString {
        int wLength;
        int wValueLength;
        int wType;
        String key;
        String value;

        public SString(RandomDataReader data) throws Exception {
            wLength = data.readWord();
            wValueLength = data.readWord();
            wType = data.readWord();
            if (wType != 1) {
                throw new Exception("Invalid SString type");
            }
            key = ResUtils.readUnicodeString0(data);
            data.padding32bit();
            value = ResUtils.readUnicodeString0(data);
            data.padding32bit();
        }
    }

    static class VarFileInfo {
        int wLength;
        int wValueLength;
        int wType;
        String szKey;
        byte[] children;

        public VarFileInfo(RandomDataReader data) throws Exception {
            wLength = data.readWord();
            wValueLength = data.readWord();
            wType = data.readWord();

            byte[] szKeyb = new byte[16];
            data.readFully(szKeyb);
            szKey = new String(szKeyb, ResUtils.UNICODE);
            data.padding32bit();
            children = new byte[wLength - 24];
            data.readFully(children);
        }
    }
}
