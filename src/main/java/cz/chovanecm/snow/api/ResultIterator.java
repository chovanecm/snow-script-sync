package cz.chovanecm.snow.api;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.tables.SnowTable;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param <T>
 * @deprecated use Dao objects where available
 */
@Deprecated
public class ResultIterator<T extends SnowRecord> implements Iterator<T> {
    private JsonResultIterator iterator;
    private SnowTable table;

    public ResultIterator(SnowClient snowClient, SnowTable table, SnowApiGetResponse response) throws IOException {
        this.table = table;
        iterator = new JsonResultIterator(snowClient, response);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            return null;
        }
        JsonObject object = iterator.next();
        try {
            return (T) table.getJsonManipulator().readFromJson(object);
        } catch (ParseException ex) {
            //FIXME
            Logger.getLogger(SnowClient.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
