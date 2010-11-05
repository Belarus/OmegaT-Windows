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

package resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Захоўваем тут DLGTEMPLATEX.
 * http://msdn.microsoft.com/en-us/library/ms645398(v=VS.85).aspx
 */
public class ResourceDialog {
    public int dlgVer;
    public int signature;
    public long helpID;
    public long exStyle;
    public long style;
    public int cDlgItems;
    public short x;
    public short y;
    public short cx;
    public short cy;
    public Integer menu;
    public Integer windowClass;
    public String title;
    public int pointsize;
    public int weight;
    public byte italic;
    public byte charset;
    public String typeface;
    public final List<DlgItemTemplateEx> items = new ArrayList<DlgItemTemplateEx>();

    /**
     * DLGITEMTEMPLATEX.
     * http://msdn.microsoft.com/en-us/library/ms645389(v=VS.85).aspx
     */
    public static class DlgItemTemplateEx {
        public long helpID;
        public long exStyle;
        public long style;
        public short x;
        public short y;
        public short cx;
        public short cy;
        public long id;
        public Object windowClass;
        public Object title;
        public int extraCount;
    }
}
