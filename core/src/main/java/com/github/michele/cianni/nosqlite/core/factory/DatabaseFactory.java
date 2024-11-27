package com.github.michele.cianni.nosqlite.core.factory;

import com.github.michele.cianni.nosqlite.core.api.NoSQLDatabaseAPI;
import com.github.michele.cianni.nosqlite.core.json.JacksonJsonHandler;
import com.github.michele.cianni.nosqlite.core.json.JsonHandler;
import com.github.michele.cianni.nosqlite.core.query.NoSQLiteQueryProcessor;
import com.github.michele.cianni.nosqlite.core.query.QueryProcessor;
import com.github.michele.cianni.nosqlite.core.storage.DataStorage;
import com.github.michele.cianni.nosqlite.core.storage.FileDataStorage;
import com.github.michele.cianni.nosqlite.core.transaction.NoSQLiteTransaction;
import com.github.michele.cianni.nosqlite.core.transaction.TransactionManager;

public class DatabaseFactory {

    public static NoSQLDatabaseAPI createDatabase(String filePath) {
        JsonHandler jsonHandler = new JacksonJsonHandler();
        DataStorage dataStorage = new FileDataStorage(jsonHandler, filePath);
        QueryProcessor queryProcessor = new NoSQLiteQueryProcessor(dataStorage);
        TransactionManager transactionManager = NoSQLiteTransaction.MANAGER;
        return new NoSQLDatabaseAPI(dataStorage, queryProcessor, transactionManager);
    }

}
