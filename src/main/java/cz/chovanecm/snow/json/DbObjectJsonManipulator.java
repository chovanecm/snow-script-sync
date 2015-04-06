package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowRecord;
import java.text.ParseException;

/**
 *
 * @author martin
 */
public class DbObjectJsonManipulator extends JsonManipulator{

    @Override
    public SnowRecord readFromJson(JsonObject json) throws ParseException {
        return this.setMyFields(json, new DbObject());
    }

    protected SnowRecord setMyFields(JsonObject json, DbObject record) throws ParseException {
        super.setMyFields(json, record); //To change body of generated methods, choose Tools | Templates.
        record.setName(json.getString("name"));
        record.setSuperClassId(json.getString("super_class", "value"));
        if (record.getSuperClassId() == null) {
            record.setSuperClassId("");
        }
        return record;
    }
    
    
}
