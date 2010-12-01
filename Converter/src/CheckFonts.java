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

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import muifile.ReaderWriterMUI;

import org.apache.commons.io.FileUtils;

import resources.ParserDialog;
import resources.RandomDataReader;
import resources.ResUtils;
import resources.ResourceDialog;

/**
 * Спраўджваем, ці хапае месца для перакладу ў дыялогах, бо беларускі тэкст можа
 * патрабаваць больш месца за ангельскі.
 */
public class CheckFonts {
    protected static int DPI;

    public static void main(String[] a) throws Exception {
        DPI = Toolkit.getDefaultToolkit().getScreenResolution();
        System.out.println("DPI=" + DPI);
        Collection<File> files = FileUtils.listFiles(new File("out/"), new String[] { "mui" }, true);
        for (File f : files) {
            System.out.println(f.getPath());
            ReaderWriterMUI mui = new ReaderWriterMUI(FileUtils.readFileToByteArray(f));
            Map<Object, Map<Object, byte[]>> res = mui.getCompiledResources();
            Map<Object, byte[]> dialogs = res.get(ResUtils.TYPE_DIALOG);
            if (dialogs == null) {
                continue;
            }
            for (Map.Entry<Object, byte[]> en : dialogs.entrySet()) {
                ResourceDialog dialog = ParserDialog.parse(new RandomDataReader(en.getValue()));
                for (ResourceDialog.DlgItemTemplateEx it : dialog.items) {
                    if (!(it.title instanceof String)) {
                        continue;
                    }
                    int linesCount = 1;
                    try {
                        int h = it.cy;
                        if (h == dialog.pointsize - 1) {
                            h++;
                        }
                        linesCount = h / dialog.pointsize;
                    } catch (ArithmeticException ex) {
                        System.out.println(en.getKey() + "/" + it.id + " - zero height");
                    }
                    if (linesCount == 0) {
                        System.out.println(en.getKey() + "/" + it.id + " - zero height");
                        continue;
                    }
                    int w = getWidth(dialog, (String) it.title) / linesCount;
                    if (w > it.cx) {
                        System.out.println(en.getKey() + "/" + it.id + " - " + it.title + " w=" + w + " cx=" + it.cx);
                    }
                }
            }
        }
    }

    static Map<String, Font> FONTS = new TreeMap<String, Font>();

    protected static Font getDialogFont(ResourceDialog dialog) throws Exception {
        Font r = FONTS.get(dialog.typeface);
        if (r == null) {
            r = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/" + dialog.typeface.replace(' ', '_') + ".ttf"));
            FONTS.put(dialog.typeface, r);
        }
        return r;
    }

    protected static int getWidth(ResourceDialog dialog, String text) throws Exception {
        Font f = getDialogFont(dialog).deriveFont(dialog.italic != 0 ? Font.ITALIC : 0, dialog.pointsize);
        FontRenderContext frc = new FontRenderContext(null, false, false);
        int r = (int) Math.ceil(f.getStringBounds(text, frc).getWidth());
        r = r * DPI / 96;
        return r;
    }
}
