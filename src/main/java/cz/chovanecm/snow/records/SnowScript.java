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
import cz.chovanecm.snow.tables.SnowTable;

import java.io.IOException;

public class SnowScript extends SnowRecord implements DeactivableSnowRecord {

    private String scriptName;
    private String script = "";
    private boolean active = true;

    public SnowScript(String sysId, String scriptName, String script, SnowTable table) {
        super(table, sysId);
        this.scriptName = scriptName;
        this.script = script;
    }

    public SnowScript(SnowTable table) {
        super(table);
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script != null ? script : "";
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void save(RecordAccessor destination) throws IOException {
        destination.saveSnowScript(this);
    }


}
