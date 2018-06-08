/*
 * Snow Script Synchronizer is a tool helping developers to write scripts for ServiceNow
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
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.ZonedDateTime;

public class FileRecordAccessor implements RecordAccessor {

    public static final String CHARSET_NAME = "UTF-8";
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
        String tableName = script.getTable().getTableName();
        Path file = getRoot().resolve(tableName).resolve(getDirBuilder().getPathForDeactivableSnowRecord(script));
        String scriptName = script.getScriptName();
        file = file.resolve(getSafeFileName(scriptName + "_" + script.getSysId() + ".js"));
        writeFile(file, getScriptAsBytes(script), script.getUpdatedOn());
    }

    public byte[] getScriptAsBytes(SnowScript script) throws UnsupportedEncodingException {
        String scriptString = script.getScript();
        return scriptString.getBytes(CHARSET_NAME);
    }

    @Override
    public void saveBusinessRule(BusinessRuleSnowScript script) throws IOException {
        Path file = getRoot().resolve(script.getTable().getTableName())
                .resolve(getDirBuilder().getPathForTableBasedObject(script))
                .resolve(getDirBuilder().getPathForDeactivableSnowRecord(script));
        file = file.resolve(getSafeFileName(script.getScriptName() + "_" + script.getSysId() + ".js"));
        writeFile(file, getScriptAsBytes(script), script.getUpdatedOn());
    }

    @Override
    public void saveClientScript(ClientScript script) throws IOException {
        Path file = getRoot().resolve(script.getTable().getTableName())
                .resolve(getDirBuilder().getPathForTableBasedObject(script))
                .resolve(getDirBuilder().getPathForDeactivableSnowRecord(script));
        file = file.resolve(getSafeFileName(script.getScriptName() + "_" + script.getSysId() + ".js"));
        writeFile(file, getScriptAsBytes(script), script.getUpdatedOn());
    }

    private String getSafeFileName(String filename) {
        return filename.replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_");
    }


    /**
     * @param file
     * @param bytes
     * @param lastModified Default NOW
     * @throws IOException
     */
    private void writeFile(Path file, byte[] bytes, ZonedDateTime lastModified) throws IOException {
        if (lastModified == null) {
            lastModified = ZonedDateTime.now();
        }
        createParentDirectories(file);
        Files.write(file, bytes);
        Files.setLastModifiedTime(file, FileTime.from(lastModified.toInstant()));
    }

    private void createParentDirectories(Path file) throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

}
