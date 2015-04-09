package cz.chovanecm.snow;

import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowScript;
import java.io.IOException;

/**
 *
 * @author martin
 */
public interface RecordAccessor {

    public void saveDbObject(DbObject dbObject) throws IOException;

    public void saveSnowScript(SnowScript script) throws IOException;

    public void saveBusinessRule(BusinessRuleSnowScript businessRule) throws IOException;
}
