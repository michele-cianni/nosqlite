package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.github.michele.cianni.nosqlite.core.command.CommandResult.*;

public final class BeginCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(BeginCommand.class);

    private final Database database;

    public BeginCommand(Database database) {
        this.database = database;
    }

    public static CommandParser parser(Database database) {
        return commandLine -> {
            LOGGER.info("Parsing begin command");

            List<String> args = List.of(commandLine.trim().split(" "));
            if (args.isEmpty()) {
                throw new CommandParsingException(ErrorMessages.EMPTY_COMMAND);
            }

            if (args.size() != 1) {
                throw new CommandParsingException("Invalid number of arguments: " + commandLine);
            }

            String commandName = args.getFirst();
            if (TypeCommand.BEGIN.check(commandName)) {
                return new BeginCommand(database);
            }
            throw new CommandParsingException("Command is not a begin command: " + commandName);
        };
    }

    @Override
    public CommandResult execute() {
        LOGGER.info("Executing begin command");
        return database.beginTransaction() ?
                success("Transaction started") :
                failure("Error starting transaction");
    }
}
