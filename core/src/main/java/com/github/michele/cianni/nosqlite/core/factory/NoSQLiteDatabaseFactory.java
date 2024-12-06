package com.github.michele.cianni.nosqlite.core.factory;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.api.NoSQLiteDatabase;
import com.github.michele.cianni.nosqlite.core.json.JacksonJsonHandler;
import com.github.michele.cianni.nosqlite.core.json.JsonHandler;
import com.github.michele.cianni.nosqlite.core.query.NoSQLiteQueryProcessor;
import com.github.michele.cianni.nosqlite.core.query.QueryProcessor;
import com.github.michele.cianni.nosqlite.core.storage.DataStorage;
import com.github.michele.cianni.nosqlite.core.storage.FileDataStorage;
import com.github.michele.cianni.nosqlite.core.transaction.NoSQLiteTransactionManager;
import com.github.michele.cianni.nosqlite.core.transaction.TransactionManager;

import java.io.IOException;

public class NoSQLiteDatabaseFactory {

    public Database createDatabase(String filePath) {
        JsonHandler jsonHandler = new JacksonJsonHandler();
        DataStorage dataStorage = createDataStorage(jsonHandler, filePath);
        QueryProcessor queryProcessor = new NoSQLiteQueryProcessor(dataStorage);
        TransactionManager transactionManager = new NoSQLiteTransactionManager();
        return new NoSQLiteDatabase(dataStorage, queryProcessor, transactionManager);
    }

    private DataStorage createDataStorage(JsonHandler jsonHandler, String filePath) {
        DataStorage dataStorage;
        try {
            dataStorage = new FileDataStorage(jsonHandler, filePath);
        } catch (IOException e) {
            throw new IllegalStateException("Error while creating the database", e);
        }
        return dataStorage;
    }

}
