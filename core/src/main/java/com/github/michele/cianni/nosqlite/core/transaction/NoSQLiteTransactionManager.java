package com.github.michele.cianni.nosqlite.core.transaction;

import java.util.ArrayList;
import java.util.List;

public final class NoSQLiteTransactionManager implements TransactionManager {

    private final List<Transaction> operations;

    private boolean started;

    public NoSQLiteTransactionManager() {
        operations = new ArrayList<>();
        started = false;
    }

    @Override
    public void startTransaction() throws TransactionException {
        if (started) {
            throw TransactionException.ALREADY_STARTED;
        }
        operations.clear();
        started = true;
    }

    @Override
    public void addOperation(Runnable operation, Runnable rollback) throws TransactionException {
        if (started) {
            operations.add(new SingleTransaction(operation, rollback));
        } else {
            throw TransactionException.NOT_STARTED;
        }
    }


    @Override
    public void commit() throws TransactionException {
        if (started) {
            for (Transaction operation : operations) {
                operation.commit();
            }
            operations.clear();
            started = false;
        } else {
            throw TransactionException.NOT_STARTED;
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if (started) {
            for (Transaction operation : operations) {
                operation.rollback();
            }
            operations.clear();
            started = false;
        } else {
            throw TransactionException.NOT_STARTED;
        }
    }
}
