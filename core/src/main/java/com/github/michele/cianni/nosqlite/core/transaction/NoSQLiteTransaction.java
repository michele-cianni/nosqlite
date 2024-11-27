package com.github.michele.cianni.nosqlite.core.transaction;

import java.util.ArrayList;
import java.util.List;

public class NoSQLiteTransaction implements Transaction {

    public static final TransactionManager MANAGER = NoSQLiteTransaction::new;

    private final List<Runnable> operations;

    private final List<Runnable> rollbacks;

    private boolean active;

    private NoSQLiteTransaction() {
        this(new ArrayList<>(), new ArrayList<>(), true);
    }

    protected NoSQLiteTransaction(List<Runnable> operations,
                                  List<Runnable> rollbacks,
                                  boolean active) {
        this.operations = operations;
        this.rollbacks = rollbacks;
        this.active = active;
    }

    @Override
    public void addOperation(Runnable operation, Runnable rollback) {
        if (!active) throw new IllegalStateException("Transaction is not active");
        operations.add(operation);
        rollbacks.add(rollback);
    }

    @Override
    public void commit() {
        try {
            operations.forEach(Runnable::run);
            active = false;
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    @Override
    public void rollback() {
        rollbacks.forEach(Runnable::run);
        active = false;
    }
}
