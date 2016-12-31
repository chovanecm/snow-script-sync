/*
 * Snow Script Synchroniser is a tool helping developers to write scripts for ServiceNow
 *     Copyright (C) 2015-2017  Martin Chovanec <chovamar@fit.cvut.cz>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.chovanecm.snow.files;

import cz.chovanecm.snow.RecordAccessor;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.records.ClientScript;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Date;

public class FileRecordAccessor implements RecordAccessor {

    private final DbObjectRegistry objectRegistry;
    private final DirectoryTreeBuilder dirBuilder;
    private final Path root;
    private String instanceURL = "";
    public FileRecordAccessor(DbObjectRegistry objectRegistry, Path root) {
        this.objectRegistry = objectRegistry;
        this.dirBuilder = new DirectoryTreeBuilder(objectRegistry);
        this.root = root;
    }

    public String getInstanceURL() {
        return instanceURL;
    }

    public void setInstanceURL(String instanceURL) {
        this.instanceURL = instanceURL;
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
        writeFile(file, (buildScriptHeader(script) + script.getScript()).getBytes(), script.getUpdatedOn());
    }

    @Override
    public void saveBusinessRule(BusinessRuleSnowScript script) throws IOException {
        Path file = root.resolve(script.getTable().getTableName())
                .resolve(getDirBuilder().getPathForTableBasedObject(script))
                .resolve(getDirBuilder().getPathForDeactivableSnowRecord(script));
        file = file.resolve(getSafeFileName(script.getScriptName() + "_" + script.getSysId() + ".js"));
        writeFile(file, (buildScriptHeader(script) + script.getScript()).getBytes(), script.getUpdatedOn());
    }

    @Override
    public void saveClientScript(ClientScript script) throws IOException {
        Path file = root.resolve(script.getTable().getTableName())
                .resolve(getDirBuilder().getPathForTableBasedObject(script))
                .resolve(getDirBuilder().getPathForDeactivableSnowRecord(script));
        file = file.resolve(getSafeFileName(script.getScriptName() + "_" + script.getSysId() + ".js"));
        writeFile(file, (buildScriptHeader(script) + script.getScript()).getBytes(), script.getUpdatedOn());
    }

    private String getSafeFileName(String filename) {
        return filename.replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_");
    }
    
    /**
     * Create a script header that will be appended to the beginning of the file.
     * It will contain all attributes and the URL of the script.
     * @param script
     * @return 
     */
    private String buildScriptHeader(SnowScript script) {
        StringBuilder builder = new StringBuilder();
         builder.append("/** [sss:snow_sync_header]").append(System.lineSeparator()).append("@snowURL ").append(getInstanceURL())
                .append("/").append(script.getTable().getTableName()).append(".do?sys_id=").append(script.getSysId())
                .append(System.lineSeparator());
         
         script.getAttributes().forEach((attribute) -> {
             builder.append("@").append(attribute).append(" ").append(script.getAttributeValue(attribute).replace("*/", "* /"));
             builder.append(System.lineSeparator());
         });
         builder.append("*/").append(System.lineSeparator());
         return builder.toString();
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
