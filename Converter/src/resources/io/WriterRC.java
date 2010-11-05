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

package resources.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import resources.ResUtils;
import resources.ResourceAccelerator;
import resources.ResourceDialog;
import resources.ResourceMessageTable;
import resources.ResourceStringBlock;
import resources.WriterRcAccelerator;
import resources.WriterRcDialog;
import resources.WriterRcMenu;
import resources.WriterRcMessageTable;
import resources.WriterRcStringBlock;

/**
 * Стварае .rc файл.
 */
public class WriterRC {
    private PrintWriter pout;

    public void write(Map<Object, Map<Object, Object>> resources, File outFile) throws Exception {
        pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8")));

        pout.println("#pragma code_page(65001)");
        pout.println("#include <resources.h>");
        pout.println();

        for (Map.Entry<Object, Map<Object, Object>> rt : resources.entrySet()) {
            for (Map.Entry<Object, Object> ri : rt.getValue().entrySet()) {
                write(rt.getKey(), ri.getKey(), ri.getValue());
            }
        }

        pout.close();
    }

    protected void write(Object type, Object id, Object data) throws Exception {
        /* Экспартуем толькі блёкі, якія мае сэнс перакладаць */
        if (type.equals(ResUtils.TYPE_ACCELERATOR)) {
            WriterRcAccelerator.write(pout, id, (ResourceAccelerator) data);
        } else if (type.equals(ResUtils.TYPE_DIALOG)) {
            WriterRcDialog.write(pout, id, (ResourceDialog) data);
        } else if (type.equals(ResUtils.TYPE_MENU)) {
            WriterRcMenu.write(pout, id, data);
        } else if (type.equals(ResUtils.TYPE_MESSAGETABLE)) {
            WriterRcMessageTable.write(pout, id, (ResourceMessageTable) data);
        } else if (type.equals(ResUtils.TYPE_STRING)) {
            WriterRcStringBlock.write(pout, id, (ResourceStringBlock) data);
        }
    }
}
