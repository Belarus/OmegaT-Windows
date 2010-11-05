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
 * Дэкампілятар рэсурсаў Message Table.
 */
public class ParserMessageTable {

    public static ResourceMessageTable parse(RandomDataReader data) throws Exception {
        ResourceMessageTable result = new ResourceMessageTable();

        int blocksCount = (int) data.readDWord();
        MessageResourceBlock[] blocks = new MessageResourceBlock[blocksCount];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new MessageResourceBlock(data);
        }
        for (MessageResourceBlock mrb : blocks) {
            data.seek((int) mrb.OffsetToEntries);
            for (long i = mrb.LowId; i <= mrb.HighId; i++) {
                short Length = data.readShort();
                short Flags = data.readShort();
                if (Flags != 1) {
                    throw new Exception("Non-Unicode messagetable entry #" + i + " Flags=0x"
                            + Integer.toHexString(Flags));
                }
                String str = ResUtils.readUnicodeString(data, Length - 4);
                while (str.charAt(str.length() - 1) == 0) {
                    str = str.substring(0, str.length() - 1);
                }
                result.messages.put(i, str);
            }
        }
        return result;
    }

    protected static class MessageResourceBlock {
        long LowId;
        long HighId;
        long OffsetToEntries;

        public MessageResourceBlock(RandomDataReader data) {
            LowId = data.readDWord();
            HighId = data.readDWord();
            OffsetToEntries = data.readDWord();
        }
    }
}
