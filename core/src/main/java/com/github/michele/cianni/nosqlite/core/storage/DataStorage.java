package com.github.michele.cianni.nosqlite.core.storage;

import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.util.Collection;

public interface DataStorage {

    void save(String key, Entry entry);

    Entry load(String key);

    void delete(String key);

    Collection<Entry> loadAll();
}
