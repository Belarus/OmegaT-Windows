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

/**
 * Структура для чытаньня і запісу дадатковага загалоўка COFF з .mui файла.
 */
public class COFF_OptionalHeader {
    short standardMagic;
    byte standardMajorLinkerVersion;
    byte standardMinorLinkerVersion;
    int standardSizeOfCode;
    int standardSizeOfInitializedData;
    int standardSizeOfUninitializedData;
    int standardAddressOfEntryPoint;
    int standardBaseOfCode;
    int standardBaseOfData;

    long windowsImageBase;
    int windowsSectionAlignment;
    int windowsFileAlignment;
    short windowsMajorOperatingSystemVersion;
    short windowsMinorOperatingSystemVersion;
    short windowsMajorImageVersion;
    short windowsMinorImageVersion;
    short windowsMajorSubsystemVersion;
    short windowsMinorSubsystemVersion;
    int windowsReserved;
    int windowsSizeOfImage;
    int windowsSizeOfHeaders;
    int windowsCheckSum;
    short windowsSubsystem;
    short windowsDLLCharacteristics;
    long windowsSizeOfStackReserve;
    long windowsSizeOfStackCommit;
    long windowsSizeOfHeapReserve;
    long windowsSizeOfHeapCommit;
    int windowsLoaderFlags;
    int windowsNumberOfRvaAndSizes;

    List<ImageDataDirectory> dataDirectories = new ArrayList<ImageDataDirectory>();

    public void read(RandomDataReader f) throws Exception {
        standardMagic = f.readShort();
        if (standardMagic != 0x10b && standardMagic != 0x20b) {
            throw new Exception("Invalid MagicNumber in OptionalHeader");
        }
        boolean isPlus = standardMagic == 0x20b;

        standardMajorLinkerVersion = f.readByte();
        standardMinorLinkerVersion = f.readByte();
        standardSizeOfCode = f.readInt();
        standardSizeOfInitializedData = f.readInt();
        standardSizeOfUninitializedData = f.readInt();
        standardAddressOfEntryPoint = f.readInt();
        standardBaseOfCode = f.readInt();
        if (!isPlus) {
            standardBaseOfData = f.readInt();
        }

        windowsImageBase = isPlus ? f.readLong() : f.readInt();
        windowsSectionAlignment = f.readInt();
        windowsFileAlignment = f.readInt();
        windowsMajorOperatingSystemVersion = f.readShort();
        windowsMinorOperatingSystemVersion = f.readShort();
        windowsMajorImageVersion = f.readShort();
        windowsMinorImageVersion = f.readShort();
        windowsMajorSubsystemVersion = f.readShort();
        windowsMinorSubsystemVersion = f.readShort();
        windowsReserved = f.readInt();
        windowsSizeOfImage = f.readInt();
        windowsSizeOfHeaders = f.readInt();
        windowsCheckSum = f.readInt();
        windowsSubsystem = f.readShort();
        windowsDLLCharacteristics = f.readShort();
        windowsSizeOfStackReserve = isPlus ? f.readLong() : f.readInt();
        windowsSizeOfStackCommit = isPlus ? f.readLong() : f.readInt();
        windowsSizeOfHeapReserve = isPlus ? f.readLong() : f.readInt();
        windowsSizeOfHeapCommit = isPlus ? f.readLong() : f.readInt();
        windowsLoaderFlags = f.readInt();
        windowsNumberOfRvaAndSizes = f.readInt();
        if (windowsNumberOfRvaAndSizes != 16) {
            throw new Exception("Invalid number of RVA sections");
        }

        for (int i = 0; i < windowsNumberOfRvaAndSizes; i++) {
            ImageDataDirectory dd = new ImageDataDirectory();
            dd.rva = f.readInt();
            dd.size = f.readInt();
            dataDirectories.add(dd);
            if (i != 2) { // check for empty
                if (dd.rva != 0 || dd.size != 0) {
                    throw new Exception("Non-empty RVA section #" + i);
                }
            }
        }
        ImageDataDirectory resourceTable = dataDirectories.get(2);
        if (resourceTable.rva == 0 || resourceTable.size == 0) {
            throw new Exception("Empty resource RVA section");
        }
    }

    public void write(RandomDataWriter f) throws Exception {
        f.writeShort(standardMagic);

        boolean isPlus = standardMagic == 0x20b;

        f.writeByte(standardMajorLinkerVersion);
        f.writeByte(standardMinorLinkerVersion);
        f.writeInt(standardSizeOfCode);
        f.writeInt(standardSizeOfInitializedData);
        f.writeInt(standardSizeOfUninitializedData);
        f.writeInt(standardAddressOfEntryPoint);
        f.writeInt(standardBaseOfCode);
        if (!isPlus) {
            f.writeInt(standardBaseOfData);
        }

        if (isPlus) {
            f.writeLong(windowsImageBase);
        } else {
            f.writeInt(windowsImageBase);
        }
        f.writeInt(windowsSectionAlignment);
        f.writeInt(windowsFileAlignment);
        f.writeShort(windowsMajorOperatingSystemVersion);
        f.writeShort(windowsMinorOperatingSystemVersion);
        f.writeShort(windowsMajorImageVersion);
        f.writeShort(windowsMinorImageVersion);
        f.writeShort(windowsMajorSubsystemVersion);
        f.writeShort(windowsMinorSubsystemVersion);
        f.writeInt(windowsReserved);
        f.writeInt(windowsSizeOfImage);
        f.writeInt(windowsSizeOfHeaders);
        f.writeInt(windowsCheckSum);
        f.writeShort(windowsSubsystem);
        f.writeShort(windowsDLLCharacteristics);
        if (isPlus) {
            f.writeLong(windowsSizeOfStackReserve);
        } else {
            f.writeInt(windowsSizeOfStackReserve);
        }
        if (isPlus) {
            f.writeLong(windowsSizeOfStackCommit);
        } else {
            f.writeInt(windowsSizeOfStackCommit);
        }
        if (isPlus) {
            f.writeLong(windowsSizeOfHeapReserve);
        } else {
            f.writeInt(windowsSizeOfHeapReserve);
        }
        if (isPlus) {
            f.writeLong(windowsSizeOfHeapCommit);
        } else {
            f.writeInt(windowsSizeOfHeapCommit);
        }
        f.writeInt(windowsLoaderFlags);
        f.writeInt(windowsNumberOfRvaAndSizes);
        if (windowsNumberOfRvaAndSizes != 16) {
            throw new Exception("Invalid number of RVA sections");
        }

        for (int i = 0; i < windowsNumberOfRvaAndSizes; i++) {
            ImageDataDirectory dd = dataDirectories.get(i);
            f.writeInt(dd.rva);
            f.writeInt(dd.size);
            dataDirectories.add(dd);
            if (i != 2) { // check for empty
                if (dd.rva != 0 || dd.size != 0) {
                    throw new Exception("Non-empty RVA section #" + i);
                }
            }
        }
        ImageDataDirectory resourceTable = dataDirectories.get(2);
        if (resourceTable.rva == 0 || resourceTable.size == 0) {
            throw new Exception("Empty resource RVA section");
        }
    }

    static class ImageDataDirectory {
        int rva;
        int size;
    }
}
