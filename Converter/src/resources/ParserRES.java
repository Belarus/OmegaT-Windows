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

import java.util.Map;
import java.util.TreeMap;

public class ParserRES {

    protected final Map<Object, Map<Object, Object>> parsedResources;

    public ParserRES(Map<Object, Map<Object, byte[]>> resources) {

        parsedResources = new TreeMap<Object, Map<Object, Object>>(ResUtils.stringIntegerComparator);

        for (Map.Entry<Object, Map<Object, byte[]>> rt : resources.entrySet()) {
            Map<Object, Object> parsedResourceType = new TreeMap<Object, Object>(ResUtils.stringIntegerComparator);
            parsedResources.put(rt.getKey(), parsedResourceType);

            for (Map.Entry<Object, byte[]> ri : rt.getValue().entrySet()) {
                String processPlace = "Resource type=" + ResUtils.getObjectType(rt.getKey()) + " key=" + ri.getKey();
                try {
                    // parser.parse(ri.getKey(), new
                    // RandomDataReader(ri.getValue()), null);
                    Object parsed = parse(rt.getKey(), new RandomDataReader(ri.getValue()));
                    parsedResourceType.put(ri.getKey(), parsed);
                } catch (Exception ex) {
                    throw new RuntimeException(processPlace+": " + ex.getMessage(), ex);
                }
            }
        }
    }

    public Map<Object, Map<Object, Object>> getParsedResources() {
        return parsedResources;
    }

    protected Object parse(Object type, RandomDataReader data) throws Exception {
        /* чытаем толькі блёкі, якія мае сэнс перакладаць */
        if (type.equals(ResUtils.TYPE_ACCELERATOR)) {
            return ParserAccelerator.parse(data);
        } else if (type.equals(ResUtils.TYPE_DIALOG)) {
            return ParserDialog.parse(data);
        } else if (type.equals(ResUtils.TYPE_MENU)) {
            return ParserMenu.parse(data);
        } else if (type.equals(ResUtils.TYPE_MESSAGETABLE)) {
            return ParserMessageTable.parse(data);
        } else if (type.equals(ResUtils.TYPE_STRING)) {
            return ParserString.parse(data);
        } else {
            throw new RuntimeException("Unknown type: " + type);
        }
    }
}
