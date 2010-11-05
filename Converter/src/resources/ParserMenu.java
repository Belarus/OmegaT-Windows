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


/**
 * Дэкампілятар рэсурсаў мэню.
 */
public class ParserMenu {

    static final int MF_POPUP = 0x10;

    public static Object parse(RandomDataReader data) throws Exception {
        int wVersion = data.readWord();
        int wOffset = data.readWord();
        switch (wVersion) {
        case 0:// bin_to_res_menuitems - MENUHEADER
            if (wOffset != 0) {
                throw new Exception("Menu offset not 0");
            }
            return readMenu(data);
        case 1:
            // bin_to_res_menuexitems - MENUEX_TEMPLATE_HEADER
            if (wOffset != 4) {
                throw new Exception("Menu offset not 4");
            }
            return readMenuEx(data);
        default:
            throw new Exception("Unknown menu version: " + wVersion);
        }
    }

    protected static ResourceMenu readMenu(RandomDataReader data) {
        ResourceMenu result = new ResourceMenu();

        while (true) {
            ResourceMenu.MenuTemplateItem it = readMenuTemplateItem(data);
            result.items.add(it);
            if ((it.mtOption & MF_POPUP) != 0) {
                it.submenu = readMenu(data);
            }
            if ((it.mtOption & 0x80) != 0) {
                break;
            }
        }
        return result;
    }

    protected static ResourceMenu.MenuTemplateItem readMenuTemplateItem(RandomDataReader data) {
        ResourceMenu.MenuTemplateItem it = new ResourceMenu.MenuTemplateItem();

        it.mtOption = data.readWord();
        if ((it.mtOption & MF_POPUP) == 0) {
            it.mtID = data.readWord();
        }
        it.mtString = ResUtils.readUnicodeString0(data);

        return it;
    }

    protected static ResourceMenuEx readMenuEx(RandomDataReader data) {
        data.padding32bit();

        ResourceMenuEx result = new ResourceMenuEx();
        result.dwHelpId = data.readDWord();
        while (true) {
            ResourceMenuEx.MenuExTemplateItem it = readMenuExTemplateItem(data);
            result.items.add(it);
            if ((it.bResInfo & 0x01) != 0) {
                it.submenu = readMenuEx(data);
            }
            if ((it.bResInfo & 0x80) != 0) {
                break;
            }
        }
        
        return result;
    }

    protected static ResourceMenuEx.MenuExTemplateItem readMenuExTemplateItem(RandomDataReader data) {
        data.padding32bit();

        ResourceMenuEx.MenuExTemplateItem it = new ResourceMenuEx.MenuExTemplateItem();
        it.dwType = data.readDWord();
        it.dwState = data.readDWord();
        it.menuId = data.readInt();
        it.bResInfo = data.readWord();
        it.szText = ResUtils.readUnicodeString0(data);
        
        return it;
    }
}
