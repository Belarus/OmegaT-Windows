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

package muifile;

import resources.RandomDataReader;
import resources.RandomDataWriter;
import resources.ResUtils;

/**
 * Чытаньне і запіс ResDirEntry.
 */
public class ResDirEntry {
    int posInFile;
    int sid;
    Object ID; // NameRVA. IntegerID
    int DataEntryRVA, SubdirectoryRVA;
    
    public ResDirEntry() {
    }

    public ResDirEntry(RandomDataReader f) {
        sid = f.readInt();
        if (sid < 0) {
            int id = sid & 0x7FFFFFFF;
            int pos = f.pos();
            f.seek(id);
            int sz = f.readWord();
            ID = ResUtils.readUnicodeString(f, sz * 2);
            f.seek(pos);
        } else {
            ID = sid;
        }
        int v = f.readInt();
        if (v < 0) {
            // High bit 1
            SubdirectoryRVA = v & 0x7FFFFFFF;
        } else {
            // High bit 0
            DataEntryRVA = v;
        }
    }

    public void write(RandomDataWriter wr) {
        wr.writeInt(sid);
        // if (sid < 0) {
        // int id = sid & 0x7FFFFFFF;
        // int pos = wr.pos();
        // wr.seek(id);
        // ResUtils.writeUnicodeString(wr, (String) ID);
        // wr.seek(pos);
        // }
        if (SubdirectoryRVA != 0) {
            wr.writeInt(SubdirectoryRVA | 0x80000000);
        } else {
            wr.writeInt(DataEntryRVA);
        }
    }
}
