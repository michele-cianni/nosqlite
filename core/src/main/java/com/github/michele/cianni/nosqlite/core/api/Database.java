package com.github.michele.cianni.nosqlite.core.api;

import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Database {

    /**
     * Get an entry by key.
     *
     * @param key the key of the entry
     * @return the entry if it exists, otherwise an empty optional
     */
    Optional<Entry> get(String key);

    /**
     * Get all entries in the database.
     *
     * @return the list of all entries
     */
    Collection<Entry> getAll();

    /**
     * Put an entry in the database.
     *
     * @param key the key of the entry
     * @param entry the entry to put
     * @return true if the entry was put, false otherwise
     */
    boolean put(String key, Entry entry);

    /**
     * Delete an entry by key.
     *
     * @param key the key of the entry
     * @return true if the entry was deleted, false otherwise
     */
    boolean delete(String key);

    /**
     * Query the database.
     *
     * @param filter the filter to apply
     * @param sortFields the fields to sort by
     * @return the list of entries that satisfy the filter
     */
    List<Entry> query(Predicate<Entry> filter, List<String> sortFields);

    /**
     * Begin a transaction.
     *
     * @return true if the transaction was started, false otherwise
     */
    boolean beginTransaction();

    /**
     * Commit a transaction.
     *
     * @return true if the transaction was committed, false otherwise
     */
    boolean commitTransaction();

    /**
     * Rollback a transaction.
     *
     * @return true if the transaction was rolled back, false otherwise
     */
    boolean rollbackTransaction();
}
