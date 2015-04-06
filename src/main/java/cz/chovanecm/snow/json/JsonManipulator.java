package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.records.SnowRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author martin
 */
public abstract class JsonManipulator {

    public abstract SnowRecord readFromJson(JsonObject json) throws ParseException;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    protected SnowRecord setMyFields(JsonObject json, SnowRecord record) throws ParseException {
        record.setSysId(json.getString("sys_id"));
        record.setUpdatedOn(dateFormat.parse(json.getString("sys_updated_on") + " GMT"));
        record.setCreatedOn(dateFormat.parse(json.getString("sys_created_on") + " GMT"));
        return record;
    }

}
