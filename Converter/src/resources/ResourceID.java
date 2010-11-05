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
 * Тут мы захоўваем маскі тых рэсурсаў, якія ня трэба апрацоўваць, таму што яны
 * запісаныя не па спэцыфікацыям.
 * 
 * Кожнае поле можа быць пустым. У гэтым выпадку рэсурсы з усякім значэньнем ня
 * трэба апрацоўваць.
 */
public class ResourceID {
    private final String file;
    private final Object type;
    private final Object[] ids;

    public ResourceID(String file, Object type, Object... ids) {
        this.file = file;
        this.type = type;
        this.ids = ids;
    }

    public boolean contains(String f, Object t, Object i) {
        if (file != null && !file.equals(f))
            return false;
        if (type != null && !type.equals(t))
            return false;
        if (ids.length == 0)
            return true;

        for (Object id : ids) {
            if (id.equals(i)) {
                return true;
            }
        }
        return false;
    }
}
