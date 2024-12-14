package com.github.michele.cianni.nosqlite.core.api;

import com.github.michele.cianni.nosqlite.core.models.Entry;
import com.github.michele.cianni.nosqlite.core.query.QueryProcessor;
import com.github.michele.cianni.nosqlite.core.storage.DataStorage;
import com.github.michele.cianni.nosqlite.core.transaction.TransactionException;
import com.github.michele.cianni.nosqlite.core.transaction.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class NoSQLiteDatabase implements Database {

    private static final Logger LOGGER = LogManager.getLogger(NoSQLiteDatabase.class);

    private final DataStorage dataStorage;

    private final QueryProcessor queryProcessor;

    private final TransactionManager transactionManager;


    public NoSQLiteDatabase(DataStorage dataStorage,
                            QueryProcessor queryProcessor,
                            TransactionManager transactionManager) {
        this.dataStorage = dataStorage;
        this.queryProcessor = queryProcessor;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<Entry> get(String key) {
        return Optional.ofNullable(dataStorage.load(key));
    }

    @Override
    public Collection<Entry> getAll() {
        return dataStorage.loadAll();
    }

    @Override
    public boolean put(String key, Entry entry) {
        try {
            transactionManager.addOperation(
                    () -> safeSave(key, entry),
                    () -> safeDelete(key)
            );
        } catch (TransactionException e) {
            LOGGER.warn("Warning adding put operation to transaction: {}", e.getMessage());
        }
        return safeSave(key, entry);
    }

    @Override
    public boolean delete(String key) {
        Entry oldEntry = dataStorage.load(key);
        try {
            transactionManager.addOperation(
                    () -> safeDelete(key),
                    () -> {
                        if (oldEntry != null) {
                            safeSave(key, oldEntry);
                        }
                    }
            );
        } catch (TransactionException e) {
            LOGGER.warn("Warning adding delete operation to transaction: {}", e.getMessage());
        }
        return safeDelete(key);
    }

    private boolean safeSave(String key, Entry entry) {
        try {
            dataStorage.save(key, entry);
            return true;
        } catch (IOException e) {
            LOGGER.error("Error saving entry with key: {}", key, e);
            return false;
        }
    }

    private boolean safeDelete(String key) {
        try {
            dataStorage.delete(key);
            return true;
        } catch (IOException e) {
            LOGGER.error("Error deleting entry with key: {}", key, e);
            return false;
        }
    }

    @Override
    public List<Entry> query(Predicate<Entry> filter, String sortField, String order) {
        return queryProcessor.executeQuery(filter, sortField, order);
    }

    @Override
    public boolean beginTransaction() {
        try {
            transactionManager.startTransaction();
        } catch (TransactionException e) {
            LOGGER.warn("Warning starting transaction: {}", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean commitTransaction() {
        try {
            transactionManager.commit();
        } catch (TransactionException e) {
            LOGGER.warn("Warning committing transaction: {}", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean rollbackTransaction() {
        try {
            transactionManager.rollback();
        } catch (TransactionException e) {
            LOGGER.warn("Warning rolling back transaction: {}", e.getMessage());
            return false;
        }
        return true;
    }

}
