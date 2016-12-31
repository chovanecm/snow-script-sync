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

package cz.chovanecm.snow.records;

import cz.chovanecm.snow.RecordAccessor;
import cz.chovanecm.snow.tables.SnowTable;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class SnowRecord {
    private final SnowTable table;
    private String sysId;
    private Date updatedOn;
    private Date createdOn;
    private final Map<String, String> attributes = new HashMap<>();
    public SnowRecord(SnowTable table, String sysId) {
        this.table = table;
        this.sysId = sysId;
    }

    public SnowRecord(SnowTable table) {
        this.table = table;
    }
    
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public SnowTable getTable() {
        return table;
    }

    public String getSysId() {
        return sysId;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    public Set<String> getAttributes() {
        return this.attributes.keySet();
    }
    public String getAttributeValue(String attribute) {
        return this.attributes.get(attribute);
    }

    public String setAttributeValue(String attribute, String value) {
        return attributes.put(attribute, value);
    }
    
    public abstract void save(RecordAccessor destination) throws IOException;
    
}
