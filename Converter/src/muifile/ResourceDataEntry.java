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

/**
 * Чытаньне і запіс ResourceDataEntry.
 */
public class ResourceDataEntry extends ResourceEntry {
    final int sectionHeaderVirtualAddress;
    int DataRVA;
    int Size;
    int Codepage;
    int Reserved;
    byte[] resdata;

    public ResourceDataEntry(RandomDataReader f, Object ID, int sectionHeaderVirtualAddress) {
        this.sectionHeaderVirtualAddress = sectionHeaderVirtualAddress;

        posInFile = f.pos();
        this.ID = ID;
        DataRVA = f.readInt();
        Size = f.readInt();
        Codepage = f.readInt();
        Reserved = f.readInt();

        f.seek(DataRVA - sectionHeaderVirtualAddress);
        resdata = new byte[Size];
        f.readFully(resdata);
    }

    @Override
    public ResDirEntry getResourceDirectoryEntry() {
        ResDirEntry rde = new ResDirEntry();
        rde.sid = (Integer) ID;
        rde.DataEntryRVA = posInFile;
        return rde;
    }

    @Override
    public void write1(RandomDataWriter wr) {
        posInFile = wr.pos();// TODO
        getResourceDirectoryEntry().write(wr);
    }

    @Override
    public void write2(RandomDataWriter wr) {

    }

    @Override
    public void write3(RandomDataWriter wr) {
        wr.writeInt(DataRVA);
        wr.writeInt(Size);
        wr.writeInt(Codepage);
        wr.writeInt(Reserved);
    }

    @Override
    public void write4(RandomDataWriter wr) {
        DataRVA = wr.pos() + sectionHeaderVirtualAddress;
        Size = resdata.length;

        wr.writeFully(resdata);
        wr.padding32bit();
    }

    public void replace(int newLocale, byte[] newr) {
        ID = newLocale;
        resdata = newr;
    }
}
