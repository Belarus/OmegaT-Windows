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
 * Структура для чытаньня і запісу загалоўка COFF з .mui файла.
 */
public class COFF_Header {
    short Machine;
    short NumberOfSections;
    int TimeDateStamp;
    int PointerToSymbolTable;
    int NumberOfSymbols;
    short SizeOfOptionalHeader;
    short Characteristics;

    public void read(RandomDataReader f) throws Exception {
        Machine = f.readShort();
        NumberOfSections = f.readShort();
        TimeDateStamp = f.readInt();
        PointerToSymbolTable = f.readInt();
        NumberOfSymbols = f.readInt();
        SizeOfOptionalHeader = f.readShort();
        Characteristics = f.readShort();
    }

    public void write(RandomDataWriter f) throws Exception {
        f.writeShort(Machine);
        f.writeShort(NumberOfSections);
        f.writeInt(TimeDateStamp);
        f.writeInt(PointerToSymbolTable);
        f.writeInt(NumberOfSymbols);
        f.writeShort(SizeOfOptionalHeader);
        f.writeShort(Characteristics);
    }
}
