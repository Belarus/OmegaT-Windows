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
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

/**
 * Утыліты для працы з рэсурсамі.
 */
public class ResUtils {
    public static final int TYPE_MENU = 4;
    public static final int TYPE_DIALOG = 5;
    public static final int TYPE_STRING = 6;
    public static final int TYPE_ACCELERATOR = 9;
    public static final int TYPE_MESSAGETABLE = 11;
    public static final int TYPE_VERSION = 16;
    public static final int TYPE_HTML = 23;
    static final Map<Integer, String> OT = new TreeMap<Integer, String>();
    static {
        OT.put(TYPE_ACCELERATOR, "ACCELERATOR");
        OT.put(TYPE_DIALOG, "DIALOG");
        OT.put(TYPE_MENU, "MENU");
        OT.put(TYPE_MESSAGETABLE, "MESSAGETABLE");
        OT.put(TYPE_STRING, "STRING");
        OT.put(TYPE_VERSION, "VERSION");
        OT.put(TYPE_HTML, "HTML");
    }

    public static final Charset UNICODE = Charset.forName("UTF-16LE");

    public static String readUnicodeString(RandomDataReader d, int len) {
        byte[] str = new byte[len];
        d.readFully(str);
        return new String(str, UNICODE);
    }

    public static void writeUnicodeString(RandomDataWriter d, String s) {
        byte[] sb = s.getBytes(UNICODE);
        d.writeWord(sb.length / 2);
        d.writeFully(sb);
    }

    public static String readUnicodeString0(RandomDataReader d) {
        StringBuilder v = new StringBuilder();
        int c;
        while ((c = d.readWord()) != 0) {
            v.append((char) c);
        }
        return v.toString();
    }

    public static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\0", "\\0").replace("\n", "\\n").replace("\r", "\\r")
                .replace("\t", "\\t").replace("\"", "\"\"");
    }

    public static String unescape(String text) {
        return text.replace("\\0", "\0").replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t")
                .replace("\"\"", "\"").replace("\\\\", "\\");
    }

    public static Object sz_Or_Ord(RandomDataReader data) throws Exception {
        int beg = data.pos();
        int first = data.readWord();
        if (first == 0) {
            // empty string
            return null;
        } else if (first == 0xFFFF) {
            // int
            return data.readWord();
        } else {
            data.seek(beg);
            return ResUtils.readUnicodeString0(data);
        }
    }

    public static int ceil(int v, int d) {
        return ((v + d - 1) / d) * d;
    }

    public static String getObjectType(Object objectType) {
        if (objectType instanceof Integer) {
            String t = OT.get(objectType);
            return t != null ? t : ("UNKOWN:" + objectType);
        } else {
            return '"' + objectType.toString() + '"';
        }
    }

    public static Map<String, File> listFiles(File dir, String ext) {
        int prefix = dir.getAbsolutePath().length() + 1;
        Map<String, File> result = new TreeMap<String, File>();
        for (File f : (Collection<File>) FileUtils.listFiles(dir, new String[] { ext }, true)) {
            String fn = f.getAbsolutePath().substring(prefix);
            result.put(fn, f);
        }
        return result;
    }

    static final byte[] EMPTY_STRINGS_DATA = new byte[32];

    public static void removeEmptyStrings(Map<Object, Map<Object, byte[]>> source) {
        Map<Object, byte[]> binStrings = source.get(ResUtils.TYPE_STRING);
        if (binStrings != null) {
            /*
             * remove empty strings blocks: they can exist in original file, but
             * not produced by linker
             */
            Set<Object> emptyBinStrings = new TreeSet<Object>();
            for (Map.Entry<Object, byte[]> en : binStrings.entrySet()) {
                if (Arrays.equals(EMPTY_STRINGS_DATA, en.getValue())) {
                    emptyBinStrings.add(en.getKey());
                }
            }
            for (Object e : emptyBinStrings) {
                binStrings.remove(e);
            }
        }
    }

    public static Comparator<Object> stringIntegerComparator = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            int c1 = o1 instanceof String ? 1 : 0;
            int c2 = o2 instanceof String ? 1 : 0;
            if (c1 != c2) {
                return c1 - c2;
            }
            if (c1 == 1) {
                String s1 = (String) o1;
                String s2 = (String) o2;
                return s1.compareTo(s2);
            } else {
                int i1 = (Integer) o1;
                int i2 = (Integer) o2;
                return i1 - i2;
            }
        }
    };
}
