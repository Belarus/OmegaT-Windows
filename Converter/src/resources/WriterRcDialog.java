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
import java.util.HashMap;
import java.util.Map;

public class WriterRcDialog {

    static final Map<Long, String> EX_STYLES = new HashMap<Long, String>();
    static final Map<Long, String> STYLES = new HashMap<Long, String>();

    static final long WS_VISIBLE = 0x10000000L;

    static final long WS_CAPTION = 0x00C00000L;

    static {
        STYLES.put(0x80000000L, "WS_POPUP");
        STYLES.put(0x40000000L, "WS_CHILDWINDOW");
        STYLES.put(0x20000000L, "WS_MINIMIZE");
        STYLES.put(0x10000000L, "WS_VISIBLE");
        STYLES.put(0x08000000L, "WS_DISABLED");
        STYLES.put(0x04000000L, "WS_CLIPSIBLINGS");
        STYLES.put(0x02000000L, "WS_CLIPCHILDREN");
        STYLES.put(0x01000000L, "WS_MAXIMIZE");
        STYLES.put(0x00C00000L, "WS_CAPTION");
        STYLES.put(0x00800000L, "WS_BORDER");
        STYLES.put(0x00400000L, "WS_DLGFRAME");
        STYLES.put(0x00200000L, "WS_VSCROLL");
        STYLES.put(0x00100000L, "WS_HSCROLL");
        STYLES.put(0x00080000L, "WS_SYSMENU");
        STYLES.put(0x00040000L, "WS_THICKFRAME");
        STYLES.put(0x00020000L, "WS_MINIMIZEBOX");
        STYLES.put(0x00010000L, "WS_MAXIMIZEBOX");
        STYLES.put(0x00000000L, "WS_OVERLAPPED");

        EX_STYLES.put(0x00000010L, "WS_EX_ACCEPTFILES");
        EX_STYLES.put(0x00040000L, "WS_EX_APPWINDOW");
        EX_STYLES.put(0x00000200L, "WS_EX_CLIENTEDGE");
        EX_STYLES.put(0x02000000L, "WS_EX_COMPOSITED");
        EX_STYLES.put(0x00000400L, "WS_EX_CONTEXTHELP");
        EX_STYLES.put(0x00010000L, "WS_EX_CONTROLPARENT");
        EX_STYLES.put(0x00000001L, "WS_EX_DLGMODALFRAME");
        EX_STYLES.put(0x00080000L, "WS_EX_LAYERED");
        EX_STYLES.put(0x00400000L, "WS_EX_LAYOUTRTL");
        EX_STYLES.put(0x00004000L, "WS_EX_LEFTSCROLLBAR");
        EX_STYLES.put(0x00000040L, "WS_EX_MDICHILD");
        EX_STYLES.put(0x08000000L, "WS_EX_NOACTIVATE");
        EX_STYLES.put(0x00100000L, "WS_EX_NOINHERITLAYOUT");
        EX_STYLES.put(0x00000004L, "WS_EX_NOPARENTNOTIFY");
        EX_STYLES.put(0x00001000L, "WS_EX_RIGHT");
        EX_STYLES.put(0x00002000L, "WS_EX_RTLREADING");
        EX_STYLES.put(0x00020000L, "WS_EX_STATICEDGE");
        EX_STYLES.put(0x00000080L, "WS_EX_TOOLWINDOW");
        EX_STYLES.put(0x00000008L, "WS_EX_TOPMOST");
        EX_STYLES.put(0x00000020L, "WS_EX_TRANSPARENT");
        EX_STYLES.put(0x00000100L, "WS_EX_WINDOWEDGE");
    }

    public static void write(PrintWriter out, Object ID, ResourceDialog object) throws Exception {

        out.println(ID + " DIALOGEX " + object.x + ", " + object.y + ", " + object.cx + ", " + object.cy);
        out.println("CAPTION \"" + ResUtils.escape(object.title) + "\"");
        out.print("STYLE 0x" + Long.toHexString(object.style));
        if ((object.style & WS_CAPTION) == 0) {
            out.print(" | NOT WS_CAPTION");
        }
        out.println();
        out.println("EXSTYLE 0x" + Long.toHexString(object.exStyle));
        out.println("FONT " + object.pointsize + ", \"" + ResUtils.escape(object.typeface) + "\", " + object.weight
                + ", " + object.italic + ", " + object.charset);
        out.println("{");
        for (ResourceDialog.DlgItemTemplateEx it : object.items) {
            out.print("  CONTROL ");
            if (it.title instanceof Integer) {
                out.print(it.title);
            } else if (it.title == null) {
                out.print("\"\"");
            } else {
                out.print("\"" + ResUtils.escape((String) it.title) + "\"");
            }
            out.print(", " + (/* it.id == 65535 ? -1: */it.id) + ", " + getWindowClassText(it.windowClass));
            out.print(", 0x" + Long.toHexString(it.style));
            if ((it.style & WS_VISIBLE) == 0) {
                out.print(" | NOT WS_VISIBLE");
            }
            out.print(", " + it.x + ", " + it.y + ", " + it.cx + ", " + it.cy);
            out.print(", 0x" + Long.toHexString(it.exStyle));
            if (it.helpID != 0) {
                out.print(", 0x" + Long.toHexString(it.helpID));
            }
            out.println();
        }
        out.println("}");
        out.println();
    }

    protected static String getFlags(Map<Long, String> allFlags, long value) {
        StringBuilder r = new StringBuilder();
        for (Map.Entry<Long, String> f : allFlags.entrySet()) {
            if ((f.getKey() & value) != 0) {
                r.append(f.getValue()).append(" | ");
            }
        }
        if (r.length() > 0) {
            r.setLength(r.length() - 3);
        }
        return r.toString();
    }

    public static String getWindowClassText(Object windowClass) throws Exception {
        if (windowClass instanceof String) {
            return '"' + ResUtils.escape((String) windowClass) + '"';
        }
        switch ((Integer) windowClass) {
        case 0x0080:
            return "Button";
        case 0x0081:
            return "Edit";
        case 0x0082:
            return "Static";
        case 0x0083:
            return "ListBox";
        case 0x0084:
            return "ScrollBar";
        case 0x0085:
            return "ComboBox";
        default:
            throw new Exception("Unknown dialog window class: 0x" + Integer.toHexString((Integer) windowClass));
        }
    }
}
