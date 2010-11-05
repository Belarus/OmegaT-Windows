import java.text.SimpleDateFormat;
import java.util.Date;

/**************************************************************************
 * Converter for Widnows MUI files.
 * 
 * Copyright (C) 2010 Alex Buloichik <alex73mail@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see http://www.gnu.org/licenses.
 **************************************************************************/

public class mt {
    public static void main(String[] args) throws Exception {
        int id = -2147416395;

        String v = ("00000000000000000000000000000000" + Integer.toBinaryString(id));
        v = v.substring(v.length() - 32);

        System.out.println(v.length() + " " + v);
        int sev = extract(v, 0, 2);
        int c = extract(v, 2, 1);
        int r = extract(v, 3, 1);
        int fac = extract(v, 4, 12);
        int code = extract(v, 16, 16);
        System.out.println("sev=" + sev);
        System.out.println("c=" + c);
        System.out.println("r=" + r);
        System.out.println("fac=" + fac);
        System.out.println("code=" + code);
    }

    protected static int extract(String bin, int start, int len) {
        String v = bin.substring(start, start + len);
        System.out.println(v);
        return Integer.parseInt(v, 2);
    }
}
