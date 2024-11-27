package com.github.michele.cianni.nosqlite.core.storage;

import com.github.michele.cianni.nosqlite.core.json.JsonHandler;

public class FileDataStorage extends AbstractDataStorage {

    public FileDataStorage(JsonHandler jsonHandler, String filePath) {
        super(jsonHandler, filePath);
    }
}
