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

package resources.res;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import resources.RandomDataReader;
import resources.ResUtils;

/**
 * Чытаньне .rc файла.
 *
 * .res format : http://www.moon-soft.com/program/format/windows/res32.htm
 */
public class RESFile {
    Map<Object, Map<Object, byte[]>> resources;

    public RESFile(File file) throws Exception {
        resources = new HashMap<Object, Map<Object, byte[]>>();
        RandomDataReader f = new RandomDataReader(FileUtils.readFileToByteArray(file));
        Resource empty = new Resource(f);
        if (((Integer) empty.resType) != 0 || ((Integer) empty.resName) != 0 || empty.data.length != 0) {
            throw new Exception("Not empty first resource in .res file");
        }

        while (f.pos() < f.length()) {
            Resource r = new Resource(f);
            Map<Object, byte[]> ri = resources.get(r.resType);
            if (ri == null) {
                ri = new TreeMap<Object, byte[]>(ResUtils.stringIntegerComparator);
                resources.put(r.resType, ri);
            }
            ri.put(r.resName, r.data);
        }
    }

    public Map<Object, Map<Object, byte[]>> getResources() {
        return resources;
    }

    static class Resource {
        int dataSize;
        int headerSize;
        Object resType;
        Object resName;
        long dataversion;
        int memoryflags;
        int languageid;
        long version;
        long characteristics;
        byte[] data;

        public Resource(RandomDataReader d) throws Exception {
            d.padding32bit();
            int beg = d.pos();
            dataSize = d.readInt();
            headerSize = d.readInt();
            resType = ResUtils.sz_Or_Ord(d);
            resName = ResUtils.sz_Or_Ord(d);
            dataversion = d.readDWord();
            memoryflags = d.readWord();
            languageid = d.readWord();
            version = d.readDWord();
            characteristics = d.readDWord();

            if (d.pos() > beg + headerSize) {
                throw new Exception("Overread");
            }
            if (headerSize % 4 != 0) {
                throw new Exception("Invalid boundary");
            }
            d.seek(beg + headerSize);
            data = new byte[dataSize];
            d.readFully(data);
            d.padding32bit();
        }

        public void print() {
            String t;
            switch ((Integer) resType) {
            case 4:
                t = "MENU";
                break;
            case 5:
                t = "DIALOG";
                break;
            case 6:
                t = "STRING";
                break;
            default:
                t = Integer.toString((Integer) resType);
            }
            System.out.println(t + " " + resName + " lang:0x" + Integer.toHexString(languageid));
        }
    }
}
