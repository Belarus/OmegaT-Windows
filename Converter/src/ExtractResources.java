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
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import muifile.ReaderWriterMUI;

import org.apache.commons.io.FileUtils;

import resources.ParserRES;
import resources.ResUtils;
import resources.io.WriterRC;
import win7.SkipResources;

/**
 * Распакоўка рэсурсаў з .mui файлаў у .rc.
 */
public class ExtractResources {

    public static void main(String[] args) throws Exception {
        // System.err.println("x86:");
        // WriterRC.dump(new MUIFile(FileUtils.readFileToByteArray(new
        // File("shell32.dll.mui.x86"))).getResources(),
        // new File("o.rc"));
        // System.err.println("amd64:");
        // mui2rc.parse(new File("shell32.dll.mui.amd64"));

        String sPath = "mui-bin/";
        String tPath = "../i18n-bel/Windows7/source/";

        Map<String, File> filesRC = ResUtils.listFiles(new File(tPath), "rc");
        Map<String, File> filesMUI = ResUtils.listFiles(new File(sPath), "mui");
        for (Map.Entry<String, File> f : filesMUI.entrySet()) {
            String fspath = f.getKey();
            String ftpath = fspath.replaceAll("\\.mui$", ".rc");

            System.err.println(fspath + " -> " + ftpath);
            File ft = new File(tPath + ftpath);
            ft.getParentFile().mkdirs();
            try {
                ReaderWriterMUI mui = new ReaderWriterMUI(FileUtils.readFileToByteArray(f.getValue()));

                Map<Object, Map<Object, byte[]>> minused = SkipResources.minus(fspath, mui.getCompiledResources(),
                        SkipResources.SKIP_EXTRACT);

                ParserRES res = new ParserRES(minused);

                new WriterRC().write(res.getParsedResources(), ft);
                filesRC.remove(ftpath);
            } catch (Exception ex) {
                System.err.println("Error in file " + f.getValue().getPath() + ": " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        for (String f : filesRC.keySet()) {
            System.out.println("Exist, but not converted: " + f);
        }
    }
}
