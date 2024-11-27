package com.github.michele.cianni.nosqlite.core.transaction;

@FunctionalInterface
public interface TransactionManager {

    Transaction startTransaction();

}
