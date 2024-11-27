package com.github.michele.cianni.nosqlite.core.storage;

import com.github.michele.cianni.nosqlite.core.json.JsonHandler;
import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDataStorage implements DataStorage  {

    private final Map<String, Entry> dataStore;

    private final JsonHandler jsonHandler;

    private final String filePath;

    protected AbstractDataStorage(JsonHandler jsonHandler, String filePath) {
        this(new ConcurrentHashMap<>(), jsonHandler, filePath);
    }

    protected AbstractDataStorage(Map<String, Entry> dataStore, JsonHandler jsonHandler, String filePath) {
        this.dataStore = dataStore;
        this.jsonHandler = jsonHandler;
        this.filePath = filePath;
        loadFromDisk();
    }

    @Override
    public void save(String key, Entry entry) {
        dataStore.put(
                Objects.requireNonNull(key, "Key must not be null"),
                entry
        );
        persistToDisk();
    }

    @Override
    public Entry load(String key) {
        return dataStore.get(key);
    }

    @Override
    public void delete(String key) {
        dataStore.remove(key);
        persistToDisk();
    }

    @Override
    public Collection<Entry> loadAll() {
        return dataStore.values();
    }

    private void loadFromDisk() {
        try {
            jsonHandler.deserializeEntries(getFile()).forEach(entry -> dataStore.put(entry.key(), entry));
        } catch (IOException e) {
            System.out.println("Error loading data from disk");
        }
    }

    private void persistToDisk() {
        try {
            jsonHandler.serializeEntries(dataStore.values(), getFile());
        } catch (IOException e) {
            System.out.println("Error persisting data to disk");
        }
    }

    private File getFile() {
        return new File(filePath);
    }
}
