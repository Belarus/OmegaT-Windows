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

package win7;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import resources.ResUtils;
import resources.ResourceID;

/**
 * Тут пазначаныя рэсурсы, якія мы не апрацоўваем.
 */
public class SkipResources {

    public static final ResourceID[] SKIP_EXTRACT = new ResourceID[] {
            /* Блёкі вэрсій перакладаць ня мае сэнсу, і часам яны невалідныя. */
            new ResourceID(null, ResUtils.TYPE_VERSION),
            /* HTML пакуль не перакладаем */
            new ResourceID(null, ResUtils.TYPE_HTML),
            /* MUI */
            new ResourceID(null, "MUI"),
            /* дзіўны тып */
            new ResourceID(null, 240),
            /* дзіўны тып */
            new ResourceID(null, 2110),
            /* Невалідны блёк */
            new ResourceID("x32/Windows/System32/en-US/mspaint.exe.mui", ResUtils.TYPE_DIALOG, 134),
            /* Невалідны блёк */
            new ResourceID("x64/Windows/SysWOW64/en-US/mspaint.exe.mui", ResUtils.TYPE_DIALOG, 134),
            /* Невалідны блёк */
            new ResourceID("x64/Windows/System32/en-US/mspaint.exe.mui", ResUtils.TYPE_DIALOG, 134),
            /* Невалідны блёк */
            new ResourceID("x32/Windows/System32/en-US/winsrv.dll.mui", ResUtils.TYPE_DIALOG, 21, 22),
            /* Невалідны блёк */
            new ResourceID("x64/Windows/SysWOW64/en-US/winsrv.dll.mui", ResUtils.TYPE_DIALOG, 21, 22),
            /* Невалідны блёк */
            new ResourceID("x64/Windows/System32/en-US/winsrv.dll.mui", ResUtils.TYPE_DIALOG, 21, 22),
            /*
             * Невалідны блёк(extraCount у нестандартавым кантроде ў dialog
             * item)
             */
            new ResourceID("x32/Windows/System32/en-US/wmploc.DLL.mui", ResUtils.TYPE_DIALOG, 1150, 1170, 1360, 1368,
                    2009, 2043, 2049, 2070),
            /*
             * Невалідны блёк(extraCount у нестандартавым кантроде ў dialog
             * item)
             */
            new ResourceID("x64/Windows/SysWOW64/en-US/wmploc.DLL.mui", ResUtils.TYPE_DIALOG, 1150, 1170, 1360, 1368,
                    2009, 2043, 2049, 2070),
            /*
             * Невалідны блёк(extraCount у нестандартавым кантроде ў dialog
             * item)
             */
            new ResourceID("x64/Windows/System32/en-US/wmploc.DLL.mui", ResUtils.TYPE_DIALOG, 1150, 1170, 1360, 1368,
                    2009, 2043, 2049, 2070) };

    public static final ResourceID[] SKIP_COMPARE = new ResourceID[] {
    /* Немагчыма стварыць такі самы dialog style. */
    new ResourceID("x32/Windows/System32/en-US/sndvol.exe.mui", ResUtils.TYPE_DIALOG, 204),
    /* Немагчыма стварыць такі самы dialog style. */
    new ResourceID("x64/Windows/SysWOW64/en-US/sndvol.exe.mui", ResUtils.TYPE_DIALOG, 204),
    /* Немагчыма стварыць такі самы dialog style. */
    new ResourceID("x64/Windows/System32/en-US/sndvol.exe.mui", ResUtils.TYPE_DIALOG, 204),
    /* StringTable ствараецца без \0 напрыканцы. */
    new ResourceID("x32/Windows/System32/en-US/VAN.dll.mui", ResUtils.TYPE_STRING),
    /* StringTable ствараецца без \0 напрыканцы. */
    new ResourceID("x64/Windows/SysWOW64/en-US/VAN.dll.mui", ResUtils.TYPE_STRING),
    /* StringTable ствараецца без \0 напрыканцы. */
    new ResourceID("x64/Windows/System32/en-US/VAN.dll.mui", ResUtils.TYPE_STRING),
    /* Няправільны парадак радкоў */
    new ResourceID("x32/Windows/System32/en-US/winsrv.dll.mui", ResUtils.TYPE_MESSAGETABLE, 1),
    /* Няправільны парадак радкоў */
    new ResourceID("x64/Windows/SysWOW64/en-US/winsrv.dll.mui", ResUtils.TYPE_MESSAGETABLE, 1),
    /* Няправільны парадак радкоў */
    new ResourceID("x64/Windows/System32/en-US/winsrv.dll.mui", ResUtils.TYPE_MESSAGETABLE, 1),
    /* Няправільны парадак радкоў */
    new ResourceID("x32/Windows/System32/en-US/ntshrui.dll.mui", ResUtils.TYPE_MESSAGETABLE, 1),
    /* Няправільны парадак радкоў */
    new ResourceID("x64/Windows/SysWOW64/en-US/ntshrui.dll.mui", ResUtils.TYPE_MESSAGETABLE, 1),
    /* Няправільны парадак радкоў */
    new ResourceID("x64/Windows/System32/en-US/ntshrui.dll.mui", ResUtils.TYPE_MESSAGETABLE, 1) };

    public static final Set<String> SKIP_COMPARE_FILES = new TreeSet<String>(Arrays.asList(
    /* Padding напрыканцы файла занадта доўгі. */
    "x32/Windows/System32/en-US/pnidui.dll.mui",
    /* Padding напрыканцы файла занадта доўгі. */
    "x64/Windows/SysWOW64/en-US/pnidui.dll.mui",
    /* Padding напрыканцы файла занадта доўгі. */
    "x64/Windows/System32/en-US/pnidui.dll.mui",
    /* Padding напрыканцы файла занадта доўгі. */
    "x32/Windows/System32/en-US/msutb.dll.mui",
    /* Padding напрыканцы файла занадта доўгі. */
    "x64/Windows/SysWOW64/en-US/msutb.dll.mui",
    /* Padding напрыканцы файла занадта доўгі. */
    "x64/Windows/System32/en-US/msutb.dll.mui",
    /* Padding у загалоўках рэсурсаў */
    "x32/Windows/System32/en-US/calc.exe.mui",
    /* Padding у загалоўках рэсурсаў */
    "x64/Windows/SysWOW64/en-US/calc.exe.mui",
    /* Padding у загалоўках рэсурсаў */
    "x64/Windows/System32/en-US/calc.exe.mui",
    /* Загалоўкі рэсурсаў */
    "x32/Windows/System32/en-US/ieframe.dll.mui",
    /* Загалоўкі рэсурсаў */
    "x64/Windows/SysWOW64/en-US/ieframe.dll.mui",
    /* Загалоўкі рэсурсаў */
    "x64/Windows/System32/en-US/ieframe.dll.mui",
    /* Padding у загалоўках рэсурсаў */
    "x32/Windows/ehome/en-US/ehres.dll.mui",
    /* Padding у загалоўках рэсурсаў */
    "x64/Windows/ehome/en-US/ehres.dll.mui",
    /** */
    "x32/Windows/System32/en-US/wmploc.DLL.mui",
    /** */
    "x64/Windows/SysWOW64/en-US/wmploc.DLL.mui",
    /** */
    "x64/Windows/System32/en-US/wmploc.DLL.mui"));

    /*
     * KNOWN_RESOURCE_TYPES.put(ResUtils.TYPE_VERSION, new ParserVersion());
     * KNOWN_RESOURCE_TYPES.put(ResUtils.TYPE_HTML, new ParserNull());
     * KNOWN_RESOURCE_TYPES.put("MUI", new ParserMUI());
     * KNOWN_RESOURCE_TYPES.put(240, new ParserNull());
     * KNOWN_RESOURCE_TYPES.put(2110, new ParserNull());
     */

    public static <T> Map<Object, Map<Object, T>> minus(String filename, Map<Object, Map<Object, T>> source,
            ResourceID[] skips) {
        Map<Object, Map<Object, T>> result = new TreeMap<Object, Map<Object, T>>(ResUtils.stringIntegerComparator);
        for (Map.Entry<Object, Map<Object, T>> rt : source.entrySet()) {
            Map<Object, T> rest = null;

            for (Map.Entry<Object, T> ri : rt.getValue().entrySet()) {
                boolean add = true;
                for (ResourceID s : skips) {
                    if (s.contains(filename, rt.getKey(), ri.getKey())) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    if (rest == null) {
                        rest = new TreeMap<Object, T>(ResUtils.stringIntegerComparator);
                        result.put(rt.getKey(), rest);
                    }
                    rest.put(ri.getKey(), ri.getValue());
                }
            }
        }
        return result;
    }
}
