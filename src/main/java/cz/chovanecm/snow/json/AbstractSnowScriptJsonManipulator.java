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
import cz.chovanecm.snow.records.SnowScript;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public abstract class AbstractSnowScriptJsonManipulator<T extends SnowScript> extends JsonManipulator<T> {

    private String scriptFieldName;
    private String nameFieldName;

    public AbstractSnowScriptJsonManipulator(String scriptFieldName, String nameFieldName) {
        this.scriptFieldName = scriptFieldName;
        this.nameFieldName = nameFieldName;
    }

    public AbstractSnowScriptJsonManipulator() {
        this("script", "name");
    }

    @Override
    protected T setMyFields(JsonObject json, T record) {
        super.setMyFields(json, record);
        String script = json.getString(getScriptFieldName());
        record.setScript(script == null ? "" : script);
        String scriptName = json.getString(getNameFieldName());
        record.setScriptName(scriptName);
        record.setActive(!"false".equals(json.getString("active")));
        Set<String> attributes = json.keySet();
        attributes.forEach((String attribute) -> {
            //Skip script field - we already have it.
            //NOTE: Perhaps we should store it anyway and just exclude it when e.g. storing as file?
            if (!attribute.equals(getScriptFieldName())) {
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
