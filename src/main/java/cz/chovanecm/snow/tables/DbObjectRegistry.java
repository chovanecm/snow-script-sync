/*
 * Snow Script Synchronizer is a tool helping developers to write scripts for ServiceNow
 *     Copyright (C) 2015-2017  Martin Chovanec <chovamar@fit.cvut.cz>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.chovanecm.snow.tables;

import cz.chovanecm.snow.records.DbObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DbObjectRegistry {

    private Map<String, DbObject> sysIdToObject = new HashMap<>();
    private Map<String, DbObject> nameToObject = new HashMap<>();

    public DbObjectRegistry(Iterable<DbObject> dbObjects) {
        for (DbObject table : dbObjects) {
            sysIdToObject.put(table.getSysId(), table);
            nameToObject.put(table.getName(), table);
        }

        sysIdToObject.values().stream()
                .filter((table) -> (!"".equals(table.getSuperClassId())))
                .forEach((DbObject table) -> {
                    try {
                        sysIdToObject.get(table.getSuperClassId()).addChildObject(table);
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                });
    }

    public DbObject getObjectBySysId(String sysId) {
        return sysIdToObject.get(sysId);
    }

    public DbObject getObjectByName(String name) {
        return nameToObject.get(name);
    }

    public Collection<DbObject> getAllObjects() {
        return sysIdToObject.values();
    }

}
