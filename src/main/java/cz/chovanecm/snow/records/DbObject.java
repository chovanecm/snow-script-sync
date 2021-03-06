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


import lombok.Getter;
import lombok.Setter;

public class DbObject extends AbstractSnowRecord {

    @Getter
    @Setter
    private String name = "";
    @Getter
    @Setter
    private String superClassId = "";
    @Getter
    @Setter
    private DbObject superClass;

    public DbObject(String sysId) {
        super(sysId);
    }

    public DbObject() {
    }

    public boolean hasSuperClass() {
        return superClassId != null && !"".equals(superClassId);
    }

}
