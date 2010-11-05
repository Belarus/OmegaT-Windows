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
 * Чытаньне і запіс загалоўка сэкцыі.
 */
public class SectionHeader {
    byte[] Name;
    int VirtualSize;
    int VirtualAddress;
    int SizeOfRawData;
    int PointerToRawData;
    int PointerToRelocations;
    int PointerToLinenumbers;
    short NumberOfRelocations;
    short NumberOfLinenumbers;
    int Characteristics;

    public void read(RandomDataReader f) throws Exception {
        Name = new byte[8];
        f.readFully(Name);
        VirtualSize = f.readInt();
        VirtualAddress = f.readInt();
        SizeOfRawData = f.readInt();
        PointerToRawData = f.readInt();
        PointerToRelocations = f.readInt();
        PointerToLinenumbers = f.readInt();
        NumberOfRelocations = f.readShort();
        NumberOfLinenumbers = f.readShort();
        Characteristics = f.readInt();
    }

    public void write(RandomDataWriter f) throws Exception {
        f.writeFully(Name);
        f.writeInt(VirtualSize);
        f.writeInt(VirtualAddress);
        f.writeInt(SizeOfRawData);
        f.writeInt(PointerToRawData);
        f.writeInt(PointerToRelocations);
        f.writeInt(PointerToLinenumbers);
        f.writeShort(NumberOfRelocations);
        f.writeShort(NumberOfLinenumbers);
        f.writeInt(Characteristics);
    }
}
