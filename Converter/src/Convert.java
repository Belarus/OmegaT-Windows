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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convert {
    static Pattern RE_MSG = Pattern.compile("(\\d+) MESSAGETABLE");
    static Pattern RE_M = Pattern.compile("(-?\\d+),\\s+\"(.+)\"");

    static FileOutputStream msgOut;

    public static void main(String[] args) throws Exception {
        String from = "OmegaT/Windows7/target/";
        String to = "OmegaT/Windows7/target2/";

        List<File> files = new ArrayList<File>();
        find(files, new File(from));

        new File(to).mkdirs();
        BufferedWriter cmd = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(to + "run.cmd"), "Cp1251"));
        for (File f : files) {
            String fn = f.getPath().substring(from.length());
            File fo = new File(to + fn);
            conv(f, fo);
            String fw = fn.replaceAll("\\.rc$", "").replace('/', '\\');

            cmd
                    .write("rc.exe /c 1251 /d \"_UNICODE\" /d \"UNICODE\" /fo temp.res /g1 /fm temp.muires /q config.rcconfig \""
                            + fw + ".rc\"\r\n");
            cmd.write("IF ERRORLEVEL 1 GOTO Error\r\n");

            cmd
                    .write("link.exe /OUT:temp.out /DLL /NOENTRY /MACHINE:X86 temp.res\r\n");
            cmd.write("IF ERRORLEVEL 1 GOTO Error\r\n");

            cmd.write("muirct -c \"..\\bin\\" + fw + "\" -e temp.out\r\n");
            cmd.write("IF ERRORLEVEL 1 GOTO Error\r\n");

            int p = fw.lastIndexOf('\\');
            String fwdir = fw.substring(0, p);
            cmd.write("mkdir \"..\\out\\" + fwdir + "\"\r\n");

            cmd.write("copy temp.out \"..\\out\\" + fw + "\"\r\n");
            cmd.write("del temp.*\r\n");
        }
        cmd.write("goto End\r\n");
        cmd.write(":Error\r\n");
        cmd.write("Echo Error in compile resource\r\n");
        cmd.write(":End\r\n");
        cmd.flush();
        cmd.close();

        InputStream in = Convert.class.getResourceAsStream("config.rcconfig");
        byte[] buf = new byte[8192];
        int len = in.read(buf);
        in.close();
        FileOutputStream out = new FileOutputStream(to + "config.rcconfig");
        out.write(buf, 0, len);
        out.flush();
        out.close();
    }

    protected static void find(List<File> res, File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                find(res, f);
            } else {
                res.add(f);
            }
        }
    }

    static Pattern RE_MUI = Pattern.compile("\\d+\\s+MUI\\s+\"Data.+");

    protected static void conv(File from, File to) throws Exception {
        System.out.println(from.getPath());
        to.getParentFile().mkdirs();

        BufferedReader rd = new BufferedReader(new InputStreamReader(
                new FileInputStream(from), "Cp1251"));
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(to), "Cp1251"));
        String s = rd.readLine();
        if (!RE_MUI.matcher(s).matches()) {
            throw new Exception("Invalid first line: " + s);
        }
        wr.write("#include <windows.h>");
        wr.newLine();
        wr.write("#include <wingdi.h>");
        wr.newLine();
        wr.write("#define FALSE 0");
        wr.newLine();
        String msg = null;

        List<Msg> messages = new ArrayList<Msg>();
        while ((s = rd.readLine()) != null) {
            Matcher m = RE_MSG.matcher(s);
            if (m.matches()) {
                msg = m.group(1);
                wr.write(s + " " + to.getName() + "_" + msg + ".bin");
                wr.newLine();

                continue;
            }
            if (msg == null) {
                wr.write(s);
                wr.newLine();
            } else {
                if (!"{".equals(s.trim()) && !"}".equals(s.trim())) {
                    Matcher z = RE_M.matcher(s);
                    if (!z.matches()) {
                        throw new Exception(s);
                    }
                    Msg ms = new Msg();
                    ms.id = Integer.parseInt(z.group(1));
                    ms.text = z.group(2);
                    messages.add(ms);
                }
            }
            if (msg != null && "}".equals(s.trim())) {
                write(to.getPath() + "_" + msg + ".bin", messages);
                messages.clear();
                msg = null;
            }
        }
        wr.flush();
        wr.close();
    }

    protected static int extract(String bin, int start, int len) {
        String v = bin.substring(start, start + len);
        return Integer.parseInt(v, 2);
    }

    /**
     * http://www.skynet.ie/~caolan/publink/winresdump/winresdump/doc/resfmt.txt
     */
    protected static void write(String fn, List<Msg> messages) throws Exception {
        msgOut = new FileOutputStream(fn);

        writeInt(messages.size());
        int len = 0;
        for (Msg m : messages) {
            writeInt(m.id);
            writeInt(m.id);
            writeInt(4 + 3 * 4 * messages.size() + len);
            len += 4 + m.text.length() * 2 + 8;
        }
        for (Msg m : messages) {
            writeShort(4 + m.text.length() * 2 + 8);
            writeShort(1);
            msgOut.write(m.text.getBytes("UTF-16LE"));
            writeShort(0x0D);
            writeShort(0x0A);
            writeShort(0x00);
            writeShort(0x00);
        }

        msgOut.flush();
        msgOut.close();
    }

    protected static void writeInt(int v) throws Exception {
        msgOut.write((v >>> 0) & 0xFF);
        msgOut.write((v >>> 8) & 0xFF);
        msgOut.write((v >>> 16) & 0xFF);
        msgOut.write((v >>> 24) & 0xFF);
    }

    protected static void writeShort(int v) throws Exception {
        msgOut.write((v >>> 0) & 0xFF);
        msgOut.write((v >>> 8) & 0xFF);
    }

    protected static class Msg {
        int id;
        String text;
    }

}
