package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.command.parser.QueryCommandParser;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.function.Predicate;

import static com.github.michele.cianni.nosqlite.core.command.CommandResult.*;

public final class QueryCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(QueryCommand.class);

    private final Database database;

    private final Predicate<Entry> filter;

    private final String sortField;

    private final String order;

    public QueryCommand(Database database, Predicate<Entry> filter) {
        this(database, filter, null, null);
    }

    public QueryCommand(Database database, Predicate<Entry> filter, String sortField, String order) {
        this.database = Objects.requireNonNull(database, ErrorMessages.DATABASE_MUST_NOT_BE_NULL);
        this.filter = Objects.requireNonNull(filter, ErrorMessages.FILTER_MUST_NOT_BE_NULL);
        this.sortField = sortField;
        this.order = order;
    }

    public static CommandParser parser(Database database) {
        return new QueryCommandParser(database);
    }

    @Override
    public CommandResult execute() {
        LOGGER.info("Executing query command: filter={}, sortField={}, order={}", filter, sortField, order);

        Object[] values = database.query(filter, sortField, order).toArray();

        return values.length > 0 ?
                success("Entries found", values) :
                failure("No entries found");
    }
}
