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

import java.io.PrintWriter;

public class WriterRcMenu {
    static final long MFS_GRAYED = 0x00000003L;
    static final long MFS_DEFAULT = 0x00001000L;

    static final long MF_GRAYED = 0x00000001L;
    static final long MF_DISABLED = 0x00000002L;
    static final long MF_CHECKED = 0x00000008L;

    public static void write(PrintWriter out, Object ID, Object data) throws Exception {
        if (data instanceof ResourceMenu) {
            writeMenu(out, ID, (ResourceMenu) data);
        } else {
            writeMenuEx(out, ID, (ResourceMenuEx) data);
        }

        out.println();
    }

    protected static void writeMenu(PrintWriter out, Object ID, ResourceMenu data) throws Exception {
        out.println(ID + " MENU");
        out.println("LANGUAGE LANG_ENGLISH, SUBLANG_ENGLISH_US");
        printMenu(out, data, "");
    }

    protected static void writeMenuEx(PrintWriter out, Object ID, ResourceMenuEx data) throws Exception {
        out.println(ID + " MENUEX");
        out.println("LANGUAGE LANG_ENGLISH, SUBLANG_ENGLISH_US");
        printMenuEx(out, data, "");
    }

    protected static void printMenu(PrintWriter out, ResourceMenu data, String prefix) throws Exception {
        out.println(prefix + "{");
        for (ResourceMenu.MenuTemplateItem it : data.items) {
            if (it.submenu != null) {
                out.print(prefix + "  POPUP \"" + it.mtString + "\"");
            } else {
                if ("".equals(it.mtString) && it.mtID == 0) {
                    out.print(prefix + "  MENUITEM SEPARATOR");
                } else {
                    out.print(prefix + "  MENUITEM \"" + ResUtils.escape(it.mtString) + "\", " + it.mtID);
                }
            }
            out.print(getFStateText(it.mtOption));
            out.println();
            if (it.submenu != null) {
                printMenu(out, it.submenu, prefix + "  ");
            }
        }
        out.println(prefix + "}");
    }

    protected static void printMenuEx(PrintWriter out, ResourceMenuEx data, String prefix) throws Exception {
        out.println(prefix + "{");
        for (ResourceMenuEx.MenuExTemplateItem it : data.items) {
            if ((it.dwType & 0x0800) != 0) {
                out.print(prefix + "  MENUITEM \"" + ResUtils.escape(it.szText) + "\", " + it.menuId
                        + ", MFT_SEPARATOR");
            } else {
                if (it.submenu != null) {
                    out.print(prefix + "  POPUP \"" + ResUtils.escape(it.szText) + "\", " + it.menuId + ", MFT_STRING");
                } else {
                    out.print(prefix + "  MENUITEM \"" + ResUtils.escape(it.szText) + "\", " + it.menuId
                            + ", MFT_STRING");
                }
            }
            out.print(", ");
            out.print(getFStateTextEx(it.dwState));
            if (it.submenu != null) {
                out.print(", 0");
            }
            out.println();
            if (it.submenu != null) {
                printMenuEx(out, it.submenu, prefix + "  ");
            }
        }
        out.println(prefix + "}");
    }

    protected static String getFStateText(int mtOption) {
        StringBuilder o = new StringBuilder();

        if ((mtOption & MF_GRAYED) != 0) {
            o.append("GRAYED |");
        }
        if ((mtOption & MF_DISABLED) != 0) {
            o.append("INACTIVE |");
        }
        if ((mtOption & MF_CHECKED) != 0) {
            o.append("CHECKED |");
        }

        if (o.length() > 0) {
            o.setLength(o.length() - 2);
        }

        return o.length() > 0 ? (", " + o.toString()) : "";
    }

    protected static String getFStateTextEx(long dwState) {
        StringBuilder o = new StringBuilder();

        if ((dwState & MFS_GRAYED) != 0) {
            o.append("MFS_GRAYED |");
        } else {
            o.append("MFS_ENABLED |");
        }
        if ((dwState & MFS_DEFAULT) != 0) {
            o.append("MFS_DEFAULT |");
        }

        if (o.length() > 0) {
            o.setLength(o.length() - 2);
        }
        return o.toString();
    }
}
