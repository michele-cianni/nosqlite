package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

public final class CommitCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(CommitCommand.class);

    private final Database database;

    public CommitCommand(Database database) {
        this.database = Objects.requireNonNull(database, ErrorMessages.DATABASE_MUST_NOT_BE_NULL);
    }

    public static CommandParser parser(Database database) {
        return commandLine -> {
            LOGGER.info("Parsing commit command: {}", commandLine);

            List<String> args = List.of(commandLine.trim().split(" "));
            if (args.isEmpty()) {
                throw new CommandParsingException(ErrorMessages.EMPTY_COMMAND);
            }

            if (args.size() != 1) {
                throw new CommandParsingException("Invalid command must is: \"commit\" but found: " + commandLine);
            }

            String commandName = args.getFirst();
            if (TypeCommand.COMMIT.check(commandName)) {
                return new CommitCommand(database);
            }
            throw new CommandParsingException("Command is not a commit command: " + commandName);
        };
    }

    @Override
    public CommandResult execute() {
        LOGGER.info("Executing commit command");
        return database.commitTransaction() ?
                CommandResult.success("Transaction committed") :
                CommandResult.failure("Error committing transaction");
    }
}
