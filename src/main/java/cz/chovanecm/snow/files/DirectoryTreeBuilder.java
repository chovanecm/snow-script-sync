/*
 * Snow Script Synchroniser is a tool helping developers to write scripts for ServiceNow
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

package cz.chovanecm.snow.files;

import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.DeactivableSnowRecord;
import cz.chovanecm.snow.records.TableBasedObject;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryTreeBuilder {
    private final DbObjectRegistry objectRegistry;

    public DirectoryTreeBuilder(DbObjectRegistry objectRegistry) {
        this.objectRegistry = objectRegistry;
    }

    public DbObjectRegistry getObjectRegistry() {
        return objectRegistry;
    }
    
    /**
     * Returns path based on table according to table extensions, e.g. cmdb_ci/cmdb_ci_hardware/cmdb_ci_computer
     * @param tableBasedObject
     * @return 
     */
    public Path getPathForTableBasedObject(TableBasedObject tableBasedObject) {
        DbObject object = getObjectRegistry().getObjectByName(tableBasedObject.getTableName());
        if (object == null) {
            return Paths.get(".");
        }
        Path path = Paths.get(object.getName());
        while (object.getSuperClass() != null) {
            object = object.getSuperClass();
            path = Paths.get(object.getName()).resolve(path);
        }
        return path;
    }
    
    public Path getPathForDeactivableSnowRecord(DeactivableSnowRecord record) {
        return (record.isActive() ? Paths.get(".") : Paths.get("_inactive_"));
    }
}
