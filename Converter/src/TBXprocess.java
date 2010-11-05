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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import org.omegat.gui.glossary.GlossaryEntry;
import org.omegat.gui.glossary.GlossaryReaderTBX;

public class TBXprocess {
    public static void main(String[] args) throws Exception {
        compile();
    }

    public static void parse() throws Exception {
        List<GlossaryEntry> ens = GlossaryReaderTBX.read(new File(
                "/w/sf.net/i18n-bel/Windows7/glossary/MicrosoftTermCollection.tbx"));
        Properties p = new Properties();
        for (GlossaryEntry ge : ens) {
            p.put(ge.getSrcText(), ge.getSrcText());
        }
        FileOutputStream f = new FileOutputStream("/tmp/MicrosoftTermCollection.properties");
        p.store(f, "");
        f.flush();
        f.close();
    }

    public static void compile() throws Exception {
        List<GlossaryEntry> ens = GlossaryReaderTBX.read(new File(
                "/w/sf.net/i18n-bel/Windows7/glossary/MicrosoftTermCollection.tbx"));
        Properties p = new Properties();
        p.load(new FileInputStream(
                "/w/sf.net/i18n-bel/MicrosoftTermCollection/target/MicrosoftTermCollection_be.properties"));

        Writer wr = new OutputStreamWriter(new FileOutputStream(
                "/w/sf.net/i18n-bel/Windows7/glossary/MicrosoftTermCollection-bel.csv"));
        for (GlossaryEntry ge : ens) {
            String tr = p.getProperty(ge.getSrcText());
            if (tr != null) {
                wr.write("\"" + ge.getSrcText() + "\",\"" + tr + "\",\"" + ge.getCommentText() + "\"\n");
            }
        }
        wr.close();
    }
}
