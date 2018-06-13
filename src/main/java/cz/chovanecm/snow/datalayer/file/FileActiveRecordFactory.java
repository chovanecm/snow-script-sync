package cz.chovanecm.snow.datalayer.file;

import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ActiveRecordFactory;
import cz.chovanecm.snow.datalayer.file.impl.DirectoryTreeBuilder;
import cz.chovanecm.snow.datalayer.file.impl.SnowScriptFileActiveRecord;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.records.ClientScript;
import cz.chovanecm.snow.records.SnowScript;
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
    public ActiveRecord getActiveRecordFor(BusinessRuleSnowScript businessRule) {
        return new SnowScriptFileActiveRecord(businessRule,
                businessRule,
                new DirectoryTreeBuilder(objectRegistry),
                getRoot(),
                "business-script"
        );
    }

    @Override
    public ActiveRecord getActiveRecordFor(ClientScript clientScript) {
        return new SnowScriptFileActiveRecord(clientScript,
                clientScript,
                new DirectoryTreeBuilder(objectRegistry),
                getRoot(),
                "client-script"
        );
    }

    @Override
    public ActiveRecord getActiveRecordFor(SnowScript snowScript) {
        return new SnowScriptFileActiveRecord(snowScript,
                getRoot(),
                snowScript.getCategory());
    }
}
