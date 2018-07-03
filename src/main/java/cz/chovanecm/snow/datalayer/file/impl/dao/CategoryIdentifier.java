package cz.chovanecm.snow.datalayer.file.impl.dao;

import java.io.IOException;

public interface CategoryIdentifier {
    String getCategoryById(String id) throws IOException;
}
