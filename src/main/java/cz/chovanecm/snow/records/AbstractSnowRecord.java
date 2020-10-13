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

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode
public class AbstractSnowRecord implements SnowRecord {
    private final Map<String, String> attributes = new HashMap<>();
    private String sysId;
    private String updatedBy;
    private ZonedDateTime updatedOn;
    private ZonedDateTime createdOn;
    private String category = "";

    public AbstractSnowRecord(String sysId) {
        this.sysId = sysId;
    }

    public AbstractSnowRecord() {
    }

    @Override
    public Set<String> getAttributes() {
        return this.attributes.keySet();
    }

    @Override
    public String getAttributeValue(String attribute) {
        return this.attributes.get(attribute);
    }

    @Override
    public String setAttributeValue(String attribute, String value) {
        return attributes.put(attribute, value);
    }

}
