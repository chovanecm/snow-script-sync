package cz.chovanecm.snow.records;

import cz.chovanecm.snow.tables.SnowTable;

/**
 *
 * @author Martin
 */
public class SnowScript extends SnowRecord {

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
        this.script = script;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    
}
