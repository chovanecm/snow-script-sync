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
import cz.chovanecm.snow.records.SnowRecord;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class JsonManipulator {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    public abstract SnowRecord readFromJson(JsonObject json) throws ParseException;

    protected SnowRecord setMyFields(JsonObject json, SnowRecord record) throws ParseException {
        record.setSysId(json.getString("sys_id"));
        ZonedDateTime sysUpdatedOn = ZonedDateTime.parse(json.getString("sys_updated_on") + " GMT", dateFormat);
        ZonedDateTime sysCreatedOn = ZonedDateTime.parse(json.getString("sys_created_on") + " GMT", dateFormat);
        record.setUpdatedOn(sysUpdatedOn);
        record.setCreatedOn(sysCreatedOn);
        return record;
    }

}
