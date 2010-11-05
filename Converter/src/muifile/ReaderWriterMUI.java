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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import resources.RandomDataReader;
import resources.RandomDataWriter;
import resources.ResUtils;

/**
 * Чытаньне і запіс .mui файла.
 * 
 * Спэцыфікацыя COFF:
 * http://www.microsoft.com/whdc/system/platform/firmware/pecoff.mspx
 */
public class ReaderWriterMUI {
    static final short IMAGE_FILE_MACHINE_I386 = 0x014c;
    static final short IMAGE_FILE_MACHINE_AMD64 = (short) 0x8664;
    static final short IMAGE_SUBSYSTEM_WINDOWS_GUI = 2;
    static final Charset ASCII = Charset.forName("ASCII");

    byte[] MSDOSstub;
    COFF_Header coffHeader;
    COFF_OptionalHeader coffOptionalHeader;
    SectionHeader sectionHeader;
    byte[] resourceData;
    ResourceDirectoryEntry rsrcTable;
    Map<Object, Map<Object, byte[]>> resources;

    public ReaderWriterMUI(byte[] data) throws Exception {
        RandomDataReader rd = new RandomDataReader(data);

        read(rd);
    }

    public Map<Object, Map<Object, byte[]>> getCompiledResources() {
        return resources;
    }

    protected void read(RandomDataReader f) throws Exception {
        // check PE signature
        f.seek(0x3C);
        int PEsignatureOffset = f.readInt();

        f.seek(PEsignatureOffset);
        int PESignature = f.readInt();
        if (PESignature != 0x00004550) { // 'PE\0\0' signature
            throw new Exception("There is no 'PE' signature");
        }

        // read file begin from 0 to signature(current location)
        MSDOSstub = new byte[f.pos()];
        f.seek(0);
        f.readFully(MSDOSstub);

        // read COFF header
        coffHeader = new COFF_Header();
        coffHeader.read(f);
        checkCoffHeader();

        // read optional header
        coffOptionalHeader = new COFF_OptionalHeader();
        coffOptionalHeader.read(f);
        checkOptionalHeader();

        // check section
        sectionHeader = new SectionHeader();
        sectionHeader.read(f);
        if (sectionHeader.Characteristics != 0x40000040) {
            throw new Exception("Invalid sectionHeader.Characteristics: "
                    + Integer.toHexString(sectionHeader.Characteristics));
        }
        String sectionName = new String(sectionHeader.Name, ASCII);
        if (!".rsrc\0\0\0".equals(sectionName)) {
            throw new Exception("Invalid sectionHeader.Name: " + sectionName);
        }
        f.seek(sectionHeader.PointerToRawData);
        resourceData = new byte[sectionHeader.SizeOfRawData];
        f.readFully(resourceData);

        RandomDataReader rsrc = new RandomDataReader(resourceData);

        // should be end of file
        if (f.pos() != f.length()) {
            throw new Exception("Resource data not in the file end");
        }

        rsrcTable = new ResourceDirectoryEntry(rsrc, 0, sectionHeader.VirtualAddress);
        // dump("", rsrcTable);

        // check tree entries types
        for (ResourceEntry es1 : rsrcTable.res) {
            if (!(es1 instanceof ResourceDirectoryEntry)) {
                throw new Exception("Resource data in " + es1.ID);
            }
            ResourceDirectoryEntry s1 = (ResourceDirectoryEntry) es1;
            for (ResourceEntry es2 : s1.res) {
                if (!(es2 instanceof ResourceDirectoryEntry)) {
                    throw new Exception("Resource data in " + s1.ID + "/" + es2.ID);
                }
                ResourceDirectoryEntry s2 = (ResourceDirectoryEntry) es2;
                for (ResourceEntry es3 : s2.res) {
                    if (!(es3 instanceof ResourceDataEntry)) {
                        throw new Exception("Submenu in " + s1.ID + "/" + s2.ID + "/" + es3.ID);
                    }
                }
            }
        }

        resources = new HashMap<Object, Map<Object, byte[]>>();
        for (ResourceDirectoryEntry s1 : (List<ResourceDirectoryEntry>) (List) rsrcTable.res) {
            Map<Object, byte[]> ri = new TreeMap<Object, byte[]>(ResUtils.stringIntegerComparator);

            for (ResourceDirectoryEntry s2 : (List<ResourceDirectoryEntry>) (List) s1.res) {

                for (ResourceDataEntry d3 : (List<ResourceDataEntry>) (List) s2.res) {
                    if (d3.Codepage != 1252) {
                        throw new Exception("Invalid codepage #" + d3.Codepage + " for " + s1.ID + "/" + s2.ID + "/"
                                + d3.ID);
                    }

                    ri.put(s2.ID, d3.resdata);
                }
            }
            resources.put(s1.ID, ri);
        }
    }

    public void write(RandomDataWriter wr) throws Exception {
        write0(wr);
        write0(wr);// write twice for update offsets
    }

    protected void write0(RandomDataWriter wr) throws Exception {
        wr.seek(0);
        wr.writeFully(MSDOSstub);
        coffHeader.write(wr);
        coffOptionalHeader.write(wr);
        sectionHeader.write(wr);
        wr.seek(sectionHeader.PointerToRawData);

        RandomDataWriter wrrs = new RandomDataWriter();

        rsrcTable.write1(wrrs); // Resource Directory Tables
        for (ResourceDirectoryEntry s1 : (List<ResourceDirectoryEntry>) (List) rsrcTable.res) {
            s1.write1(wrrs);
        }
        for (ResourceDirectoryEntry s1 : (List<ResourceDirectoryEntry>) (List) rsrcTable.res) {
            for (ResourceDirectoryEntry s2 : (List<ResourceDirectoryEntry>) (List) s1.res) {
                s2.write1(wrrs);
            }
        }
        for (ResourceDirectoryEntry s1 : (List<ResourceDirectoryEntry>) (List) rsrcTable.res) {
            for (ResourceDirectoryEntry s2 : (List<ResourceDirectoryEntry>) (List) s1.res) {
                for (ResourceEntry d3 : s2.res) {
                    // d3.write1(wrrs);
                }
            }
        }

        rsrcTable.write3(wrrs); // Resource Data Description
        for (ResourceDirectoryEntry s1 : (List<ResourceDirectoryEntry>) (List) rsrcTable.res) {
            s1.write3(wrrs);
            for (ResourceDirectoryEntry s2 : (List<ResourceDirectoryEntry>) (List) s1.res) {
                s2.write3(wrrs);
                for (ResourceEntry d3 : s2.res) {
                    d3.write3(wrrs);
                }
            }
        }

        rsrcTable.write2(wrrs); // Resource Directory Strings
        for (ResourceDirectoryEntry s1 : (List<ResourceDirectoryEntry>) (List) rsrcTable.res) {
            s1.write2(wrrs);
            for (ResourceDirectoryEntry s2 : (List<ResourceDirectoryEntry>) (List) s1.res) {
                s2.write2(wrrs);
                for (ResourceEntry d3 : s2.res) {
                    d3.write2(wrrs);
                }
            }
        }

        rsrcTable.write4(wrrs); // Resource Data
        for (ResourceDirectoryEntry s1 : (List<ResourceDirectoryEntry>) (List) rsrcTable.res) {
            s1.write4(wrrs);
            for (ResourceDirectoryEntry s2 : (List<ResourceDirectoryEntry>) (List) s1.res) {
                s2.write4(wrrs);
                for (ResourceEntry d3 : s2.res) {
                    d3.write4(wrrs);
                }
            }
        }

        wrrs.padding512bytes();

        sectionHeader.SizeOfRawData = wrrs.length();
        sectionHeader.VirtualSize = ResUtils.ceil(wrrs.length(), 0x1000);
        coffOptionalHeader.standardSizeOfInitializedData = wrrs.length();

        // from first pass
        coffOptionalHeader.windowsSizeOfImage = ResUtils.ceil(coffOptionalHeader.windowsSizeOfImage,
                coffOptionalHeader.windowsSectionAlignment);

        wr.writeFully(wrrs.getBytes());

        // for the second pass
        coffOptionalHeader.windowsSizeOfImage = sectionHeader.VirtualSize + sectionHeader.VirtualAddress;
    }

    public void replaceResources(int newLocale, Map<Object, Map<Object, byte[]>> newResources) {
        for (ResourceDirectoryEntry s1 : (List<ResourceDirectoryEntry>) (List) rsrcTable.res) {
            for (ResourceDirectoryEntry s2 : (List<ResourceDirectoryEntry>) (List) s1.res) {
                for (ResourceDataEntry d3 : (List<ResourceDataEntry>) (List) s2.res) {
                    if (newResources.get(s1.ID) != null) {
                        byte[] n = newResources.get(s1.ID).get(s2.ID);
                        if (n != null) {// TODO: remove check
                            d3.replace(newLocale, n);
                        }
                    }
                }
            }
        }
    }

    protected void checkCoffHeader() throws Exception {
        if (coffHeader.Machine != IMAGE_FILE_MACHINE_I386 && coffHeader.Machine != IMAGE_FILE_MACHINE_AMD64) {
            throw new Exception("Invalid coff.Machine: " + Integer.toHexString(coffHeader.Machine));
        }
        if (coffHeader.NumberOfSections != 1) {
            throw new Exception("Invalid number of sections: " + coffHeader.NumberOfSections);
        }
        if (coffHeader.Characteristics != 0x2102 && coffHeader.Characteristics != 0x2022
                && coffHeader.Characteristics != 0x2122) {
            throw new Exception("Invalid coff.Characteristics: " + Integer.toHexString(coffHeader.Characteristics));
        }
        if (coffHeader.SizeOfOptionalHeader == 0) {
            throw new Exception("Empty OptionalHeader");
        }
    }

    protected void checkOptionalHeader() throws Exception {
        if (coffOptionalHeader.windowsSubsystem != IMAGE_SUBSYSTEM_WINDOWS_GUI) {
            throw new Exception("Invalid coffOptionalHeader.windowsSubsystem: " + coffOptionalHeader.windowsSubsystem);
        }
        if (coffOptionalHeader.windowsDLLCharacteristics != 0x540
                && coffOptionalHeader.windowsDLLCharacteristics != 0x140
                && coffOptionalHeader.windowsDLLCharacteristics != 0x400) {
            throw new Exception("Invalid coffOptionalHeader.windowsDLLCharacteristics: "
                    + Integer.toHexString(coffOptionalHeader.windowsDLLCharacteristics));
        }
    }
}
