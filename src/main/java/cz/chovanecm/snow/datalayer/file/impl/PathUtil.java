package cz.chovanecm.snow.datalayer.file.impl;

import cz.chovanecm.snow.records.DeactivableSnowRecord;
import cz.chovanecm.snow.records.TableAwareObject;
import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;

@Setter
public class PathUtil {

    public static Path getPathForTableBasedObject(TableAwareObject object, DirectoryTreeBuilder treeBuilder) {
        return treeBuilder.getPathInTableHierarchy(object.getAssignedTableName());
    }

    public static Path getPathForDeactivableSnowRecord(DeactivableSnowRecord record) {
        return record.isActive() ? Paths.get(".") : Paths.get("_inactive_");
    }

    public static String getSafeFileName(String filename) {
        return filename.replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_");
    }
}
