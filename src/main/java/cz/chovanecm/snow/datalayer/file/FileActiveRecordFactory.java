package cz.chovanecm.snow.datalayer.file;

import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ActiveRecordFactory;
import cz.chovanecm.snow.datalayer.file.impl.DirectoryTreeBuilder;
import cz.chovanecm.snow.datalayer.file.impl.activerecord.SnowScriptFileActiveRecord;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.records.TableAwareSnowScript;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public class FileActiveRecordFactory implements ActiveRecordFactory {
    private Path root;
    private DbObjectRegistry objectRegistry;

    public FileActiveRecordFactory(Path root, DbObjectRegistry objectRegistry) {
        this.root = root;
        this.objectRegistry = objectRegistry;
    }

    @Override
    public ActiveRecord getActiveRecordForTableAwareSnowScript(TableAwareSnowScript tableAwareSnowScript) {
        return new SnowScriptFileActiveRecord(tableAwareSnowScript,
                tableAwareSnowScript,
                new DirectoryTreeBuilder(objectRegistry),
                getRoot(),
                tableAwareSnowScript.getCategory()
        );
    }

    @Override
    public ActiveRecord getActiveRecordForSnowScript(SnowScript snowScript) {
        return new SnowScriptFileActiveRecord(snowScript,
                getRoot(),
                snowScript.getCategory());
    }

}
