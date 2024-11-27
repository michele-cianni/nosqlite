package com.github.michele.cianni.nosqlite.core.transaction;

public interface Transaction {

    void addOperation(Runnable operation, Runnable rollback);

    void commit();

    void rollback();
}
