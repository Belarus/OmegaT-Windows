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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

import resources.ResUtils;

/**
 * Спраўджваем файлы рэсурсаў:
 * 
 * 1. Ці ўсе файлы маюць унікальную назву
 * 
 * 2. Ці усе файлы з x64 існуюць у x32, і наадварот
 * 
 * 3. Ці аднолькавыя яны
 * 
 * 4. Ці аднолькавыя варыянты 32-бітавыя і 64-бітавыя ў x64
 * 
 * Калі гэта так, мы можам перакладаць толькі 32-бітавую вэрсію, бо 64-бітавая
 * мае такія ж самыя рэсурсы.
 */
public class CheckExtracted {
    protected static final File RESOURCES_DIR = new File("../i18n-bel/Windows7/source/");

    protected static final Set<String> SKIP1 = new TreeSet<String>(Arrays.asList("notepad.exe.rc"));

    protected static Map<String, File> files32;
    protected static Map<String, File> files64;

    public static void main(String[] a) throws Exception {
        files32 = ResUtils.listFiles(new File(RESOURCES_DIR, "x32"), "rc");
        files64 = ResUtils.listFiles(new File(RESOURCES_DIR, "x64"), "rc");

        check1();
        check2();
        check3();
        check4();
        System.err.println("done");
    }

    protected static void check1() {
        Set<String> exist = new HashSet<String>();
        for (File f : files32.values()) {
            if (exist.contains(f.getName()) && !SKIP1.contains(f.getName())) {
                System.err.println("Дуюлюецца назва ў файле " + f);
            } else {
                exist.add(f.getName());
            }
        }
    }

    protected static void check2() {
        Set<String> exist64 = new HashSet<String>();
        for (Map.Entry<String,File> f : files64.entrySet()) {
            String mapped32 = map64to32(f.getKey());
            if (!files32.containsKey(mapped32)) {
                System.out.println("Не існуе ў x32: " + f);
            }
            exist64.add(f.getValue().getName());
        }
        for (Map.Entry<String,File> f : files32.entrySet()) {
            if (!exist64.contains(f.getValue().getName())) {
                System.out.println("Не існуе ў x64: " + f);
            }
        }
    }

    protected static void check3() throws IOException {
        for (Map.Entry<String, File> f64 : files64.entrySet()) {
            File f32 = files32.get(map64to32(f64.getKey()));
            if (!compareFiles(f32, f64.getValue())) {
                System.err.println("Не супадае x64 і x32: " + f64.getKey());
            }
        }
    }

    protected static void check4() throws IOException {
        for (Map.Entry<String, File> f64 : files64.entrySet()) {
            String mapped32 = map64to32(f64.getKey());
            File f1 = f64.getValue();
            File f2 = files64.get(mapped32);
            if (!compareFiles(f1, f2)) {
                System.err.println("Не супадае x64 і x64/x32: " + f1 + "/" + f2);
            }
        }
    }

    /**
     * Якая назва файла супадае ў 32-бітавай сыстэме.
     */
    protected static String map64to32(String f64) {
        return f64.replace("/SysWOW64/", "/System32/").replace("Program Files (x86)/", "Program Files/");
    }

    protected static boolean compareFiles(File f1, File f2) throws IOException {
        byte[] b32 = FileUtils.readFileToByteArray(f1);
        byte[] b64 = FileUtils.readFileToByteArray(f2);
        return Arrays.equals(b32, b64);
    }
}
