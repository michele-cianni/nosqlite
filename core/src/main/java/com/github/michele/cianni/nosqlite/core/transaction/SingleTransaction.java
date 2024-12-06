package com.github.michele.cianni.nosqlite.core.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SingleTransaction implements Transaction {

    private static final Logger LOGGER = LogManager.getLogger(SingleTransaction.class);

    private final Runnable operation;

    private final Runnable rollback;

    public SingleTransaction(Runnable operation, Runnable rollback) {
        this.operation = operation;
        this.rollback = rollback;
    }

    @Override
    public void commit() throws TransactionException {
        LOGGER.info("Committing transaction");
        try {
            operation.run();
        } catch (Exception e) {
            LOGGER.error("Error committing transaction", e);
            rollback();
        }
    }

    @Override
    public void rollback() throws TransactionException {
        LOGGER.info("Rolling back transaction");
        try {
            rollback.run();
        } catch (Exception e) {
            LOGGER.error("Error rolling back transaction", e);
            throw new TransactionException("Error rolling back transaction", e);
        }
    }

}
