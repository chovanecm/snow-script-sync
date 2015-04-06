package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.SnowRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author martin
 */
public abstract class JsonManipulator {

    public abstract SnowRecord readFromJson(JsonObject json) throws ParseException;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    protected SnowRecord setUpdated(JsonObject json, SnowRecord record) throws ParseException {
        record.setUpdatedOn(dateFormat.parse(json.getString("sys_updated_on") + " GMT"));
        return record;
    }

}
