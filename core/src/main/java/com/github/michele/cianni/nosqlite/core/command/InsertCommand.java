package com.github.michele.cianni.nosqlite.core.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.console.TypeCommand;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

import static com.github.michele.cianni.nosqlite.core.command.CommandResult.*;

public final class InsertCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(InsertCommand.class);

    private final Database database;

    private final String key;

    private final Entry entry;

    public InsertCommand(Database database, String key, Entry entry) {
        this.database = Objects.requireNonNull(database, ErrorMessages.DATABASE_MUST_NOT_BE_NULL);
        this.key = Objects.requireNonNull(key, ErrorMessages.KEY_MUST_NOT_BE_NULL);
        this.entry = Objects.requireNonNull(entry, ErrorMessages.ENTRY_MUST_NOT_BE_NULL);
    }

    public static CommandParser parser(Database database) {
        return commandLine -> {
            LOGGER.info("Parsing insert command: {}", commandLine);

            List<String> args = List.of(commandLine.trim().split(" "));
            if (args.isEmpty()) {
                throw new CommandParsingException(ErrorMessages.EMPTY_COMMAND);
            }

            if (args.size() != 3) {
                throw new CommandParsingException("Invalid command must is: \"insert\" <key> <value> but found: " + commandLine);
            }

            String commandName = args.getFirst();
            if (TypeCommand.INSERT.is(commandName)) {
                List<String> parameters = args.subList(1, args.size());
                if (parameters.size() != 2) {
                    throw new CommandParsingException(String.format(ErrorMessages.INVALID_PARAMETERS, parameters));
                }

                String key = parameters.getFirst();
                String value = parameters.getLast();
                JsonNode jsonNode;
                try {
                    jsonNode = new ObjectMapper().readTree(value);
                } catch (JsonProcessingException e) {
                    throw new CommandParsingException("Invalid JSON value: " + value);
                }
                return new InsertCommand(database, key, new Entry(key, jsonNode));
            }
            throw new CommandParsingException("Command is not an insert command: " + commandName);
        };
    }

    @Override
    public CommandResult execute() {
        LOGGER.info("Executing insert command for entry: {}", entry);
        return database.put(key, entry) ?
                success( "Entry inserted successfully") :
                failure("Error inserting entry");
    }

}
