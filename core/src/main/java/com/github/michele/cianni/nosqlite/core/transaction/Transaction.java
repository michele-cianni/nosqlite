package com.github.michele.cianni.nosqlite.core.transaction;

public interface Transaction {

    /**
     * Commit the transaction.
     */
    void commit() throws TransactionException;

    /**
     * Rollback the transaction.
     */
    void rollback() throws TransactionException;
}
