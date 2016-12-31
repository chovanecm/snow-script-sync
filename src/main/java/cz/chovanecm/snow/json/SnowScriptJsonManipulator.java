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
import cz.chovanecm.snow.tables.ScriptSnowTable;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.records.SnowScript;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang.ArrayUtils;

public class SnowScriptJsonManipulator extends JsonManipulator {

    private final ScriptSnowTable table;

    public SnowScriptJsonManipulator(ScriptSnowTable table) {
        this.table = table;
    }

    public ScriptSnowTable getTable() {
        return table;
    }

    @Override
    public SnowRecord readFromJson(JsonObject json) throws ParseException {
        return setMyFields(json, new SnowScript(getTable()));
    }

    protected SnowRecord setMyFields(JsonObject json, SnowScript record) throws ParseException {
        super.setMyFields(json, record);

        record.setScript(json.getString(getTable().getScriptField()));
        record.setScriptName(json.getString(getTable().getNameField()));
        record.setActive("true".equals(json.getString("active")));
        Set<String> attributes = json.keySet();
        attributes.forEach((String attribute) -> {
            //Skip script field - we already have it.
            //NOTE: Perhaps we should store it anyway and just exclude it when e.g. storing as file?
            if (!attribute.equals(getTable().getScriptField())) {
                if (json.get(attribute).isPrimitive()) {
                    record.setAttributeValue(attribute, json.getString(attribute));
                } else {
                    record.setAttributeValue(attribute, "[sss:debug:non-primitive-value]");
                }
            }
        });
        return record;
    }

}
