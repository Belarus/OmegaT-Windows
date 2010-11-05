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
import java.nio.charset.Charset;
import java.util.Map;

public class WriterRcMessageTable {
    protected static final Charset UNICODE = Charset.forName("UTF-16LE");

    public static void write(PrintWriter out, Object ID, ResourceMessageTable data) throws Exception {
        out.println(ID + " MESSAGETABLE");
        out.println("{");

        for (Map.Entry<Long, String> mr : data.messages.entrySet()) {
            out.println("  " + mr.getKey() + ",  \"" + ResUtils.escape(mr.getValue()) + "\"");
        }
        out.println("}");
    }
}
