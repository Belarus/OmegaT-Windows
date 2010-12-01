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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import muifile.ReaderWriterMUI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import resources.ParserVersion;
import resources.RandomDataReader;
import resources.ResUtils;

/**
 * Ствараем файл усталёўкі для NSIS.
 */
public class Installer {

    /** Трэба падаваць свае назвы, бо у JDK 1.6 назвы месяцаў няправільныя. */
    protected static final String[] MONTHS = new String[] { "студзеня", "лютага", "сакавіка", "красавіка", "траўня",
            "чэрвеня", "ліпеня", "жніўня", "верасня", "кастрычніка", "лістапада", "снежня" };

    public static void main(String[] args) throws Exception {
        Map<String, String> SYSDIR32 = new TreeMap<String, String>();
        Map<String, String> SYSDIR64 = new TreeMap<String, String>();

        SYSDIR32.put("Program Files", "$PROGRAMFILES");
        SYSDIR32.put("Windows", "$WINDIR");

        SYSDIR64.put("Program Files (x86)", "$PROGRAMFILES32");
        SYSDIR64.put("Program Files", "$PROGRAMFILES64");
        SYSDIR64.put("Windows", "$WINDIR");

        Process p32 = new Process(new File("mui-bin/x32/"), "x32/", SYSDIR32);
        Process p64 = new Process(new File("mui-bin/x64/"), "x64/",SYSDIR64);

        Calendar c = Calendar.getInstance();
        String dateText = c.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[c.get(Calendar.MONTH)] + " "
                + c.get(Calendar.YEAR);

        Map<String, String> templateVars = new TreeMap<String, String>();
        templateVars.put("##DATE##", dateText);
        templateVars.put("##FILEVERSIONS32##", p32.fileVersions.toString());
        templateVars.put("##FILEUNPACK32##", p32.fileUnpack.toString());
        templateVars.put("##FILEINSTALL32##", p32.fileInstall.toString());
        templateVars.put("##DIR_INSTALL32##", p32.dirInstall.toString());
        templateVars.put("##DIR_UNINSTALL32##", p32.dirUninstall.toString());
        templateVars.put("##FILEVERSIONS64##", p64.fileVersions.toString());
        templateVars.put("##FILEUNPACK64##", p64.fileUnpack.toString());
        templateVars.put("##FILEINSTALL64##", p64.fileInstall.toString());
        templateVars.put("##DIR_INSTALL64##", p64.dirInstall.toString());
        templateVars.put("##DIR_UNINSTALL64##", p64.dirUninstall.toString());

        patchFile("readme.txt", templateVars);
        patchFile("win7bel.nsi", templateVars);
        patchFile("Belarusian.nlf", templateVars);
        patchFile("Belarusian.nsh", templateVars);
    }

    protected static void patchFile(String fn, Map<String, String> templateVars) throws Exception {
        String template = IOUtils.toString(Installer.class.getResourceAsStream("/installer/" + fn), "UTF-8");

        String out = template;
        for (Map.Entry<String, String> e : templateVars.entrySet()) {
            out = out.replace(e.getKey(), e.getValue());
        }

        FileUtils.writeStringToFile(new File("out/" + fn), out, "Cp1251");
    }

    protected static String getVersion(File f) throws Exception {
        ReaderWriterMUI mui = new ReaderWriterMUI(FileUtils.readFileToByteArray(f));
        byte[] ver = mui.getCompiledResources().get(ResUtils.TYPE_VERSION).get(1);
        ParserVersion pv = new ParserVersion();
        pv.parse(1, new RandomDataReader(ver), new PrintWriter(new StringWriter()));

        return (pv.vi.dwFileVersionMS >> 16) + "." + (pv.vi.dwFileVersionMS & 0xFFFF) + "."
                + (pv.vi.dwFileVersionLS >> 16) + "." + (pv.vi.dwFileVersionLS & 0xFFFF);
    }

    protected static void copy(String file) throws Exception {
        byte[] data = IOUtils.toByteArray(Installer.class.getResourceAsStream("/installer/" + file));
        FileUtils.writeByteArrayToFile(new File("out/" + file), data);
    }

    protected static class Process {
        StringBuilder fileVersions = new StringBuilder();
        StringBuilder fileUnpack = new StringBuilder();
        StringBuilder fileInstall = new StringBuilder();
        StringBuilder dirInstall = new StringBuilder();
        StringBuilder dirUninstall = new StringBuilder();

        Set<String> dirs = new TreeSet<String>();

        public Process(File dirFind, String dirSrc, Map<String, String> sysDirs) throws Exception {
            Map<String, File> files = ResUtils.listFiles(dirFind, "mui");
            for (Map.Entry<String, File> f : files.entrySet()) {
                processFile(sysDirs, f.getKey(), dirSrc, f.getValue());
                System.out.println(f.getValue());
            }

            for (String d : dirs) {
                dirInstall.append("\tCreateDirectory  '" + d + "'\n");
                dirUninstall.append("\tRmDir /r /REBOOTOK '" + d + "'\n");
            }
        }

        protected boolean processFile(Map<String, String> nsisVars, String fn, String srcPrefix, File f)
                throws Exception {
            String srcFile = (srcPrefix + fn).replace('/', '\\').replace("\\en-US\\", "\\be-BY\\");

            int pos = fn.indexOf('/');
            String prefix = fn.substring(0, pos);
            String var = nsisVars.get(prefix);
            String destFile = (var + fn.substring(pos)).replace('/', '\\').replace("\\en-US\\", "\\be-BY\\");

            int posDir = destFile.lastIndexOf('\\');
            String outPath = destFile.substring(0, posDir);
            dirs.add(outPath);

            fileVersions.append("\tStrCpy $FileBe \"" + destFile + "\"\n");
            fileVersions.append("\tStrCpy $RequiredVersion \"" + getVersion(f) + "\"\n");
            fileVersions.append("\tCall VersionCheckFunc\n");
            fileVersions.append("\n");

            fileUnpack.append("\tFile '/oname=" + destFile + ".new'  '" + srcFile + "'\n");
            fileInstall.append("\tDelete /REBOOTOK '" + destFile + "'\n");
            fileInstall.append("\tRename /REBOOTOK '" + destFile + ".new' '" + destFile + "'\n");
            fileInstall.append("\n");

            return true;
        }
    }

}
