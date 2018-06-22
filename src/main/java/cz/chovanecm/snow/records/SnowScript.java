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

import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ActiveRecordFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SnowScript extends AbstractSnowRecord implements DeactivableSnowRecord {

    private String scriptName;
    @NonNull
    private String script = "";
    private boolean active = true;

    public SnowScript(String sysId, String scriptName, String script) {
        super(sysId);
        this.scriptName = scriptName;
        this.script = script;
    }

    public SnowScript() {
    }


    @Override
    public ActiveRecord getActiveRecord(ActiveRecordFactory factory) {
        return factory.getActiveRecordForSnowScript(this);
    }
}
