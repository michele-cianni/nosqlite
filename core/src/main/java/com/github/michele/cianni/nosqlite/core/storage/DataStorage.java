package com.github.michele.cianni.nosqlite.core.storage;

import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.io.IOException;
import java.util.Collection;

public interface DataStorage {

    void save(String key, Entry entry) throws IOException;

    void delete(String key) throws IOException;

    Entry load(String key);

    Collection<Entry> loadAll();
}
