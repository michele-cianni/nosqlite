package com.github.michele.cianni.nosqlite.core.transaction;

public interface TransactionManager extends Transaction {

    void startTransaction() throws TransactionException;

    void addOperation(Runnable operation, Runnable rollback) throws TransactionException;
}
