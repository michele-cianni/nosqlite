package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

import static com.github.michele.cianni.nosqlite.core.command.CommandResult.*;
import static com.github.michele.cianni.nosqlite.core.command.CommandResult.success;

public final class GetAllCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(GetAllCommand.class);

    private final Database database;

    public GetAllCommand(Database database) {
        this.database = Objects.requireNonNull(database, ErrorMessages.DATABASE_MUST_NOT_BE_NULL);
    }

    public static CommandParser parser(Database database) {
        return commandLine -> {
            LOGGER.info("Parsing getAll command");

            List<String> args = List.of(commandLine.trim().split(" "));
            if (args.isEmpty()) {
                throw new CommandParsingException(ErrorMessages.EMPTY_COMMAND);
            }

            if (args.size() != 1) {
                throw new CommandParsingException("Invalid command must is: \"getAll\" but found: " + commandLine);
            }

            String commandName = args.getFirst();
            if (TypeCommand.GET_ALL.check(commandName)) {
                return new GetAllCommand(database);
            }
            throw new CommandParsingException("Command is not a getAll command: " + commandName);
        };
    }

    @Override
    public CommandResult execute() {
        LOGGER.info("Executing getAll command");

        Object[] values = database.getAll().toArray();

        return values.length > 0 ?
                success("Entries found", values) :
                failure("No entries found");
    }
}
