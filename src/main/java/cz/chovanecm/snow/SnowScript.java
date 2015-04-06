package cz.chovanecm.snow;

/**
 *
 * @author Martin
 */
public class SnowScript extends SnowRecord {

    private String scriptName;
    private String script = "";
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

    
}
