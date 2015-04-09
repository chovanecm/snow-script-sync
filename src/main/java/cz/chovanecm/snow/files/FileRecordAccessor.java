package cz.chovanecm.snow.files;

import cz.chovanecm.snow.RecordAccessor;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Date;

/**
 *
 * @author martin
 */
public class FileRecordAccessor implements RecordAccessor {

    private final DbObjectRegistry objectRegistry;
    private final DirectoryTreeBuilder dirBuilder;
    private final Path root;

    public FileRecordAccessor(DbObjectRegistry objectRegistry, Path root) {
        this.objectRegistry = objectRegistry;
        this.dirBuilder = new DirectoryTreeBuilder(objectRegistry);
        this.root = root;
    }

    public DbObjectRegistry getObjectRegistry() {
        return objectRegistry;
    }

    public DirectoryTreeBuilder getDirBuilder() {
        return dirBuilder;
    }

    public Path getRoot() {
        return root;
    }

    @Override
    public void saveDbObject(DbObject dbObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveSnowScript(SnowScript script) throws IOException {
        Path file = root.resolve(script.getTable().getTableName()).resolve(getDirBuilder().getPathForDeactivableSnowRecord(script));
        file = file.resolve(getSafeFileName(script.getScriptName() + "_" + script.getSysId() + ".js"));
        writeFile(file, script.getScript().getBytes(), script.getUpdatedOn());
    }

    @Override
    public void saveBusinessRule(BusinessRuleSnowScript script) throws IOException {
        Path file = root.resolve(script.getTable().getTableName())
                .resolve(getDirBuilder().getPathForTableBasedObject(script))
                .resolve(getDirBuilder().getPathForDeactivableSnowRecord(script));
        file = file.resolve(getSafeFileName(script.getScriptName() + "_" + script.getSysId() + ".js"));
        writeFile(file, script.getScript().getBytes(), script.getUpdatedOn());
    }

    private String getSafeFileName(String filename) {
        return filename.replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_");
    }

    /**
     *
     * @param file
     * @param bytes
     * @param lastModified Default NOW
     * @throws IOException
     */
    private void writeFile(Path file, byte[] bytes, Date lastModified) throws IOException {
        if (lastModified == null) {
            lastModified = new Date();
        }
        Files.createDirectories(file.getParent());
        Files.write(file, bytes);
        Files.setLastModifiedTime(file, FileTime.fromMillis(lastModified.getTime()));
    }
}
