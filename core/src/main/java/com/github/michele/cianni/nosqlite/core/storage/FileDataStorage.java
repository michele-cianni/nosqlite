package com.github.michele.cianni.nosqlite.core.storage;

import com.github.michele.cianni.nosqlite.core.json.JsonHandler;
import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FileDataStorage implements DataStorage {

    private final Map<String, Entry> dataStore;
    private final JsonHandler jsonHandler;
    private final String filePath;

    public FileDataStorage(JsonHandler jsonHandler, String filePath) throws IOException {
        this(new ConcurrentHashMap<>(), jsonHandler, filePath);
    }

    private FileDataStorage(Map<String, Entry> dataStore, JsonHandler jsonHandler, String filePath) throws IOException {
        this.dataStore = dataStore;
        this.jsonHandler = jsonHandler;
        this.filePath = filePath;
        loadFromDisk();
    }

    @Override
    public void save(String key, Entry entry) throws IOException {
        dataStore.put(
                Objects.requireNonNull(key, "Key must not be null"),
                entry
        );
        persistToDisk();
    }

    @Override
    public void delete(String key) throws IOException {
        dataStore.remove(key);
        persistToDisk();
    }

    @Override
    public Entry load(String key) {
        return dataStore.get(key);
    }

    @Override
    public Collection<Entry> loadAll() {
        return dataStore.values();
    }

    private void loadFromDisk() throws IOException {
        jsonHandler.deserializeEntries(craeteFile()).forEach(entry -> dataStore.put(entry.key(), entry));
    }

    private void persistToDisk() throws IOException {
        jsonHandler.serializeEntries(dataStore.values(), craeteFile());
    }

    private File craeteFile() {
        return new File(filePath);
    }
}
