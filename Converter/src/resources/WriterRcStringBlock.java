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

public class WriterRcStringBlock {

    public static void write(PrintWriter out, Object ID, ResourceStringBlock data) throws Exception {
        boolean empty = true;
        for (String s : data.strings) {
            if (s != null) {
                empty = false;
                break;
            }
        }
        if (empty) {
            return;
        }

        out.println("STRINGTABLE");
        out.println("LANGUAGE LANG_ENGLISH, SUBLANG_ENGLISH_US");
        out.println("{");
        for (int i = 0; i < 16; i++) {
            if (data.strings[i] != null) {
                out.println("  " + (((Integer) ID) * 16 - 16 + i) + ",  \"" + ResUtils.escape(data.strings[i]) + "\"");
            }
        }
        out.println("}");
        out.println();
    }
}
