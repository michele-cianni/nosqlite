package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.github.michele.cianni.nosqlite.core.command.TypeCommand.*;

public final class RollbackCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(RollbackCommand.class);

    private final Database database;

    public RollbackCommand(Database database) {
        this.database = database;
    }

    public static CommandParser parser(Database database) {
        return commandLine -> {
            LOGGER.info("Parsing rollback command");
            if (commandLine.isEmpty()) {
                throw new CommandParsingException(ErrorMessages.EMPTY_COMMAND);
            }

            List<String> args = List.of(commandLine.trim().split(" "));
            if (args.size() != 1) {
                throw new CommandParsingException("Invalid command must is: \"rollback\" but found: " + commandLine);
            }

            String commandName = args.getFirst();
            if (ROLLBACK.check(commandName)) {
                return new RollbackCommand(database);
            }
            throw new CommandParsingException("Invalid command: " + commandName);
        };
    }

    @Override
    public CommandResult execute() {
        LOGGER.info("Executing rollback command");
        return database.rollbackTransaction() ?
                CommandResult.success("Transaction rolled back") :
                CommandResult.failure("Error rolling back transaction");
    }
}
