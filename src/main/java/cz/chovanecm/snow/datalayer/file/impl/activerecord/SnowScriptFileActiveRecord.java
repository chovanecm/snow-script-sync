package cz.chovanecm.snow.datalayer.file.impl.activerecord;

import cz.chovanecm.snow.datalayer.file.impl.DirectoryTreeBuilder;
import cz.chovanecm.snow.datalayer.file.impl.PathUtil;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.records.TableAwareObject;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public class SnowScriptFileActiveRecord extends AbstractScriptFileActiveRecord {

    private DirectoryTreeBuilder builderForTableBasedObject;
    private TableAwareObject tableAwareObject;
    private SnowScript snowScript;
    private String baseDirectoryName;
    private Path root;

    public SnowScriptFileActiveRecord(SnowScript snowScript, Path root, String baseDirectoryName) {
        this.snowScript = snowScript;
        this.baseDirectoryName = baseDirectoryName;
        this.root = root;
    }

    public SnowScriptFileActiveRecord(SnowScript snowScript, TableAwareObject tableAwareObject, DirectoryTreeBuilder builderForTableBasedObject, Path root, String baseDirectoryName) {
        this.snowScript = snowScript;
        this.baseDirectoryName = baseDirectoryName;
        this.root = root;
        this.tableAwareObject = tableAwareObject;
        this.builderForTableBasedObject = builderForTableBasedObject;
    }


    @Override
    public Path getFilePath() {
        Path path = getRoot().resolve(getBaseDirectoryName());
        if (hasAssignedTable()) {
            path = path.resolve(PathUtil.getPathForTableBasedObject(this.getTableAwareObject(), getBuilderForTableBasedObject()));
        }
        return path.resolve(PathUtil.getPathForDeactivableSnowRecord(getRecord()))
                .resolve(PathUtil.getSafeFileName(getRecord().getScriptName()) + "_" + getRecord().getSysId() + ".js");
    }

    public boolean hasAssignedTable() {
        return this.getTableAwareObject() != null && getBuilderForTableBasedObject() != null;
    }

    @Override
    public SnowScript getRecord() {
        return snowScript;
    }
}
