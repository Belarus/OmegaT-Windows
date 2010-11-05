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
 * Дэкампілятар рэсурсаў аксэлератараў.
 */
public class ParserAccelerator {
    public static ResourceAccelerator parse(RandomDataReader data) throws Exception {
        ResourceAccelerator result = new ResourceAccelerator();
        while (true) {
            ResourceAccelerator.AccelTableEntry e = read(data);
            if (e.padding != 0) {
                throw new Exception("Padding in accelerator is not 0");
            }
            result.table.add(e);

            if ((e.fFlags & 0x80) != 0) {
                break;
            }
        }
        return result;
    }

    protected static ResourceAccelerator.AccelTableEntry read(RandomDataReader d) {
        ResourceAccelerator.AccelTableEntry e = new ResourceAccelerator.AccelTableEntry();
        e.fFlags = d.readWord();
        e.wAnsi = d.readWord();
        e.wId = d.readWord();
        e.padding = d.readWord();
        return e;
    }
}
