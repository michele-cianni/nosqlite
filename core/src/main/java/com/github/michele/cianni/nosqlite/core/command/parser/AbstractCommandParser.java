package com.github.michele.cianni.nosqlite.core.command.parser;

import com.github.michele.cianni.nosqlite.core.command.Command;
import com.github.michele.cianni.nosqlite.core.command.TypeCommand;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCommandParser implements CommandParser {
    private static final Logger LOGGER = LogManager.getLogger(AbstractCommandParser.class);

    protected final Map<TypeCommand, CommandParser> parsers;

    protected AbstractCommandParser() {
        this(new HashMap<>());
    }

    private AbstractCommandParser(Map<TypeCommand, CommandParser> parsers) {
        this.parsers = parsers;
    }

    @Override
    public Command parse(String commandLine) throws CommandParsingException {
        LOGGER.info("Parsing line: {}", commandLine);

        List<String> args = List.of(commandLine.trim().split(" "));
        if (args.isEmpty()) {
            throw new CommandParsingException(ErrorMessages.EMPTY_COMMAND);
        }

        String commandName = args.getFirst();
        Optional<TypeCommand> typeCommandFound = TypeCommand.fromString(commandName);
        if (typeCommandFound.isPresent()) {
            CommandParser parser = parsers.get(typeCommandFound.get());
            return parser.parse(commandLine);
        }
        throw new CommandParsingException("Command not found: " + commandName);
    }
}
