package com.github.michele.cianni.nosqlite.core.api;

import com.github.michele.cianni.nosqlite.core.models.Entry;
import com.github.michele.cianni.nosqlite.core.query.QueryProcessor;
import com.github.michele.cianni.nosqlite.core.storage.DataStorage;
import com.github.michele.cianni.nosqlite.core.transaction.Transaction;
import com.github.michele.cianni.nosqlite.core.transaction.TransactionManager;

import java.util.List;
import java.util.function.Predicate;

public class NoSQLDatabaseAPI {

    private final DataStorage dataStorage;

    private final QueryProcessor queryProcessor;

    private final TransactionManager transactionManager;

    public NoSQLDatabaseAPI(DataStorage dataStorage,
                            QueryProcessor queryProcessor,
                            TransactionManager transactionManager) {
        this.dataStorage = dataStorage;
        this.queryProcessor = queryProcessor;
        this.transactionManager = transactionManager;
    }

    public void put(String key, Entry entry) {
        dataStorage.save(key, entry);
    }

    public Entry get(String key) {
        return dataStorage.load(key);
    }

    public void delete(String key) {
        dataStorage.delete(key);
    }

    public List<Entry> query(Predicate<Entry> filter, List<String> sortFields) {
        return queryProcessor.executeQuery(filter, sortFields);
    }

    public Transaction beginTransaction() {
        return transactionManager.startTransaction();
    }
}
