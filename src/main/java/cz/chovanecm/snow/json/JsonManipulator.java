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

package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.records.SnowRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class JsonManipulator {

    public abstract SnowRecord readFromJson(JsonObject json) throws ParseException;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    protected SnowRecord setMyFields(JsonObject json, SnowRecord record) throws ParseException {
        record.setSysId(json.getString("sys_id"));
        record.setUpdatedOn(dateFormat.parse(json.getString("sys_updated_on") + " GMT"));
        record.setCreatedOn(dateFormat.parse(json.getString("sys_created_on") + " GMT"));
        return record;
    }

}
