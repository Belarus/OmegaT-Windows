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
import java.util.Map;
import java.util.TreeMap;

/**
 * Піша рэсурс у RC файл.
 */
public class WriterRcAccelerator {
    protected static final Map<Integer, String> OTHER_KEYS = new TreeMap<Integer, String>();
    static {
        OTHER_KEYS.put(0x70, "VK_F1");
        OTHER_KEYS.put(0x71, "VK_F2");
        OTHER_KEYS.put(0x72, "VK_F3");
        OTHER_KEYS.put(0x73, "VK_F4");
        OTHER_KEYS.put(0x74, "VK_F5");
        OTHER_KEYS.put(0x75, "VK_F6");
        OTHER_KEYS.put(0x76, "VK_F7");
        OTHER_KEYS.put(0x77, "VK_F8");
        OTHER_KEYS.put(0x78, "VK_F9");
        OTHER_KEYS.put(0x79, "VK_F10");
        OTHER_KEYS.put(0x7A, "VK_F11");
        OTHER_KEYS.put(0x7B, "VK_F12");
        OTHER_KEYS.put(45, "VK_INSERT");
        OTHER_KEYS.put(46, "VK_DELETE");
        OTHER_KEYS.put(13, "VK_RETURN");
        OTHER_KEYS.put(8, "VK_BACK");
        OTHER_KEYS.put(9, "VK_TAB");
    }

    public static void write(PrintWriter out, Object ID, ResourceAccelerator data) throws Exception {
        out.println(ID + " ACCELERATORS");
        out.println("LANGUAGE LANG_ENGLISH, SUBLANG_ENGLISH_US");
        out.println("{");
        for (ResourceAccelerator.AccelTableEntry e : data.table) {
            boolean virt = (e.fFlags & 0x01) != 0;
            out.print("  ");
            out.print(getKey(e.wId, e.wAnsi, virt) + "  , " + e.wId);
            out.print(virt ? ", VIRTKEY" : ", ASCII");
            out.print((e.fFlags & 0x02) != 0 ? ", NOINVERT" : "");
            out.print((e.fFlags & 0x04) != 0 ? ", SHIFT" : "");
            out.print((e.fFlags & 0x08) != 0 ? ", CONTROL" : "");
            out.print((e.fFlags & 0x10) != 0 ? ", ALT" : "");
            out.println();
        }
        out.println("}");
        out.println();
    }

    protected static String getKey(int kID, int ansi, boolean isVirt) throws Exception {
        if (isVirt) {
            String ot = OTHER_KEYS.get(ansi);
            if (ot != null) {
                return ot;
            }
            if (ansi >= 'A' && ansi <= 'Z') {
                char a = (char) ansi;
                return "VK_" + a;
            }
        } else {
            if ((ansi >= 'A' && ansi <= 'Z') || (ansi >= 'a' && ansi <= 'z') || (ansi >= '0' && ansi <= '9')) {
                char a = (char) ansi;
                return "\"" + a + '"';
            }
        }
        return Integer.toString(ansi);
    }
}
