package cz.chovanecm.snow;

import java.util.Date;

/**
 *
 * @author Martin
 */
public class SnowScript {
    private String sysId;
    private String scriptName;
    private String script = "";
    private SnowScriptTable table;
    private Date updated;
    
    public SnowScript(String sysId, String scriptName, String script, SnowScriptTable table) {
        this.sysId = sysId;
        this.scriptName = scriptName;
        this.script = script;
        this.table = table;
    }

    public SnowScript(String sysId, String scriptName, SnowScriptTable table) {
        this.sysId = sysId;
        this.scriptName = scriptName;
        this.table = table;
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

    public String getSysId() {
        return sysId;
    }

    public SnowScriptTable getTable() {
        return table;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
}
