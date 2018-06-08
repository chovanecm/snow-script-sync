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

package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.records.DbObject;

public class DbObjectJsonManipulator extends JsonManipulator<DbObject> {

    @Override
    public DbObject readFromJson(JsonObject json) {
        return this.setMyFields(json, new DbObject());
    }

    @Override
    protected DbObject setMyFields(JsonObject json, DbObject record) {
        super.setMyFields(json, record); //To change body of generated methods, choose Tools | Templates.
        record.setName(json.getString("name"));
        record.setSuperClassId(json.getString("super_class", "value"));
        if (record.getSuperClassId() == null) {
            record.setSuperClassId("");
        }
        return record;
    }


}
