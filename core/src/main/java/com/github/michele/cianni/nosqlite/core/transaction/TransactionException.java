package com.github.michele.cianni.nosqlite.core.transaction;

public class TransactionException extends Exception {
    static final TransactionException ALREADY_STARTED = new TransactionException("Transaction already started");
    static final TransactionException NOT_STARTED = new TransactionException("Transaction not started");

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

}
