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

import java.util.ArrayList;
import java.util.List;

import resources.RandomDataReader;
import resources.RandomDataWriter;
import resources.ResUtils;

/**
 * Чытаньне і запіс ResourceDirectoryEntry.
 */
public class ResourceDirectoryEntry extends ResourceEntry {
    int Characteristics;
    int TimeDateStamp;
    short MajorVersion;
    short MinorVersion;
    int posInFileStrID;

    List<ResourceEntry> res = new ArrayList<ResourceEntry>();

    public ResourceDirectoryEntry(RandomDataReader f, Object ID, int sectionHeaderVirtualAddress) throws Exception {
        posInFile = f.pos();// TODO
        this.ID = ID;
        Characteristics = f.readInt();
        TimeDateStamp = f.readInt();
        MajorVersion = f.readShort();
        MinorVersion = f.readShort();

        int cEntriesNamed = f.readShort();
        int cEntriesID = f.readShort();

        ResDirEntry[] entries = new ResDirEntry[cEntriesNamed + cEntriesID];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new ResDirEntry(f);
        }

        for (ResDirEntry de : entries) {
            if (de.SubdirectoryRVA != 0) {
                f.seek(de.SubdirectoryRVA);
                res.add(new ResourceDirectoryEntry(f, de.ID,  sectionHeaderVirtualAddress));
            } else if (de.DataEntryRVA != 0) {
                f.seek(de.DataEntryRVA);
                res.add(new ResourceDataEntry(f, de.ID,  sectionHeaderVirtualAddress));
            } else {
                throw new Exception("Unknown ResourceDirectoryEntry type");
            }
        }
    }

    @Override
    public ResDirEntry getResourceDirectoryEntry() {
        ResDirEntry rde = new ResDirEntry();
        if (ID instanceof String) {
            rde.sid = posInFileStrID | 0x80000000;
        } else {
            rde.sid = (Integer) ID;
        }
        rde.SubdirectoryRVA = posInFile;
        return rde;
    }

    public void write1(RandomDataWriter wr) {
        posInFile = wr.pos();
        wr.writeInt(Characteristics);
        wr.writeInt(TimeDateStamp);
        wr.writeShort(MajorVersion);
        wr.writeShort(MinorVersion);

        int cEntriesNamed = 0;
        int cEntriesID = 0;
        for (ResourceEntry e : res) {
            if (e.ID instanceof String) {
                cEntriesNamed++;
            } else {
                cEntriesID++;
            }
        }

        wr.writeShort(cEntriesNamed);
        wr.writeShort(cEntriesID);

        for (ResourceEntry e : res) {
            e.getResourceDirectoryEntry().write(wr);
        }
    }

    public void write2(RandomDataWriter wr) {
        if (ID instanceof String) {
            posInFileStrID = wr.pos();
            ResUtils.writeUnicodeString(wr, (String) ID);
            wr.padding32bit0();
        }
    }

    public void write3(RandomDataWriter wr) {

    }

    public void write4(RandomDataWriter wr) {

    }
}
