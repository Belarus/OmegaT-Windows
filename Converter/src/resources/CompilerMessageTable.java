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
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Стварае блёк рэсурсаў з радкоў.
 */
public class CompilerMessageTable {

    /**
     * Compile message table
     */
    public static byte[] compile(ResourceMessageTable data) throws Exception {
        List<Long> allIDs = new ArrayList<Long>(data.messages.size());
        for (long id : data.messages.keySet()) {
            allIDs.add(id);
        }
        Collections.sort(allIDs);

        List<MessageResourceBlock> blocks = new ArrayList<MessageResourceBlock>();
        MessageResourceBlock block = null;
        for (long id : allIDs) {
            if (block != null) {
                if (id == block.HighId + 1) {
                    // continue block;
                    block.HighId = id;
                } else {
                    // need new block
                    block = null;
                }
            }
            if (block == null) {
                // need to create new block
                block = new MessageResourceBlock();
                blocks.add(block);
                block.LowId = id;
                block.HighId = id;
            }
        }
        write0(data.messages, blocks); // twice for calculate offsets
        return write0(data.messages, blocks);
    }

    protected static byte[] write0(Map<Long, String> messages, List<MessageResourceBlock> blocks) {
        RandomDataWriter wr = new RandomDataWriter();
        wr.writeDWord(blocks.size());
        for (MessageResourceBlock b : blocks) {
            b.write(wr);
        }
        for (MessageResourceBlock b : blocks) {
            b.OffsetToEntries = wr.pos();
            for (long i = b.LowId; i <= b.HighId; i++) {
                String msg = messages.get(i);
                byte[] txt = (msg + "\0").getBytes(ResUtils.UNICODE);
                byte[] txt2 = new byte[(txt.length + 3) / 4 * 4];
                System.arraycopy(txt, 0, txt2, 0, txt.length);// align
                wr.writeShort(txt2.length + 4);
                wr.writeShort(1); // flag
                wr.writeFully(txt2);
            }
        }

        return wr.getBytes();
    }

    static class MessageResourceBlock {
        long LowId;
        long HighId;
        long OffsetToEntries;

        public void write(RandomDataWriter wr) {
            wr.writeDWord(LowId);
            wr.writeDWord(HighId);
            wr.writeDWord(OffsetToEntries);
        }
    }
}
