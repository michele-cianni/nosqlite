package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.console.TypeCommand;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

import static com.github.michele.cianni.nosqlite.core.command.CommandResult.*;

public final class GetCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(GetCommand.class);

    private final Database database;

    private final String key;

    public GetCommand(Database database, String key) {
        this.database = Objects.requireNonNull(database, ErrorMessages.DATABASE_MUST_NOT_BE_NULL);
        this.key = Objects.requireNonNull(key, ErrorMessages.KEY_MUST_NOT_BE_NULL);
    }

    public static CommandParser parser(Database database) {
        return commandLine -> {
            LOGGER.info("Parsing get command: {}", commandLine);

            List<String> args = List.of(commandLine.trim().split(" "));
            if (args.isEmpty()) {
                throw new CommandParsingException(ErrorMessages.EMPTY_COMMAND);
            }

            if (args.size() != 2) {
                throw new CommandParsingException("Invalid command must is: \"get\" <key> but found: " + commandLine);
            }

            String commandName = args.getFirst();
            if (TypeCommand.GET.is(commandName)) {
                List<String> parameters = args.subList(1, args.size());
                if (parameters.size() != 1) {
                    throw new CommandParsingException(String.format(ErrorMessages.INVALID_PARAMETERS, parameters));
                }

                String key = parameters.getFirst();
                return new GetCommand(database, key);
            }
            throw new CommandParsingException("Command is not a get command: " + commandName);
        };
    }

    @Override
    public CommandResult execute() {
        LOGGER.info("Executing get command for key: {}", key);
        return database.get(key)
                .map(entry -> success("Entry found: " + entry, entry))
                .orElseGet(() -> failure("Entry not found"));
    }
}
