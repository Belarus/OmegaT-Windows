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

import java.util.Map;

/**
 * Дэкампілятар рэсурсаў дыялогаў.
 * 
 * http://msdn.microsoft.com/en-us/library/ms645398(v=VS.85).aspx
 */
public class ParserDialog {

    public static ResourceDialog parse(RandomDataReader data) throws Exception {
        ResourceDialog object = new ResourceDialog();
        readDlgTemplateEx(data, object);

        for (int i = 0; i < object.cDlgItems; i++) {
            ResourceDialog.DlgItemTemplateEx item = readDlgItemTemplateEx(data);
            if (item.extraCount != 0) {
                throw new Exception("extraCount!=0");
            }
            object.items.add(item);
        }

        return object;
    }

    protected static String getFlags(Map<Long, String> allFlags, long value) {
        StringBuilder r = new StringBuilder();
        for (Map.Entry<Long, String> f : allFlags.entrySet()) {
            if ((f.getKey() & value) != 0) {
                r.append(f.getValue()).append(" | ");
            }
        }
        if (r.length() > 0) {
            r.setLength(r.length() - 3);
        }
        return r.toString();
    }

    public static void readDlgTemplateEx(RandomDataReader data, ResourceDialog o) throws Exception {
        o.dlgVer = data.readWord();
        o.signature = data.readWord();
        o.helpID = data.readDWord();
        o.exStyle = data.readDWord();
        o.style = data.readDWord();
        o.cDlgItems = data.readWord();
        o.x = data.readShort();
        o.y = data.readShort();
        o.cx = data.readShort();
        o.cy = data.readShort();
        o.menu = (Integer) ResUtils.sz_Or_Ord(data);
        o.windowClass = (Integer) ResUtils.sz_Or_Ord(data);
        o.title = ResUtils.readUnicodeString0(data);
        o.pointsize = data.readWord();
        o.weight = data.readWord();
        o.italic = data.readByte();
        o.charset = data.readByte();
        o.typeface = ResUtils.readUnicodeString0(data);
    }

    public static ResourceDialog.DlgItemTemplateEx readDlgItemTemplateEx(RandomDataReader data) throws Exception {
        data.padding32bit();

        ResourceDialog.DlgItemTemplateEx r = new ResourceDialog.DlgItemTemplateEx();

        r.helpID = data.readDWord();
        r.exStyle = data.readDWord();
        r.style = data.readDWord();
        r.x = data.readShort();
        r.y = data.readShort();
        r.cx = data.readShort();
        r.cy = data.readShort();
        r.id = data.readDWord();
        r.windowClass = ResUtils.sz_Or_Ord(data);
        r.title = ResUtils.sz_Or_Ord(data);
        r.extraCount = data.readWord();

        return r;
    }
}
