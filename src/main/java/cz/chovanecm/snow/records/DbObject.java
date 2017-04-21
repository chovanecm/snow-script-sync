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

package cz.chovanecm.snow.records;


import cz.chovanecm.snow.RecordAccessor;
import cz.chovanecm.snow.tables.DbObjectTable;

import java.util.HashSet;
import java.util.Set;

public class DbObject extends SnowRecord {

    private final Set<DbObject> childs = new HashSet<>();
    private String name = "";
    private String superClassId = "";
    private DbObject superClass;

    public DbObject(String sysId) {
        super(new DbObjectTable(), sysId);
    }

    public DbObject() {
        super(new DbObjectTable());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperClassId() {
        return superClassId;
    }

    public void setSuperClassId(String superClassId) {
        this.superClassId = superClassId;
    }

    public DbObject getSuperClass() {
        return superClass;
    }

    public void setSuperClass(DbObject superClass) {
        this.superClass = superClass;
    }

    public void addChildObject(DbObject child) {
        childs.add(child);
        child.setSuperClass(this);
    }

    public Set<DbObject> getChilds() {
        return childs;
    }

    @Override
    public void save(RecordAccessor destination) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
