package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.console.ConsoleCommandParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

public final class HelpCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(HelpCommand.class);

    private final ConsoleCommandParser parser;

    public HelpCommand(ConsoleCommandParser parser) {
        this.parser = Objects.requireNonNull(parser, "Parser must not be null");
    }

    public static CommandParser parser(ConsoleCommandParser consoleCommandParser) {
        return commandLine -> {
            LOGGER.info("Parsing help command");

            List<String> args = List.of(commandLine.trim().split(" "));
            if (args.isEmpty()) {
                throw new CommandParsingException("Empty command");
            }

            if (args.size() != 1) {
                throw new CommandParsingException("Invalid number of arguments: " + commandLine);
            }

            String commandName = args.getFirst();
            if (TypeCommand.HELP.check(commandName)) {
                return new HelpCommand(consoleCommandParser);
            }
            throw new CommandParsingException("Command is not a help command: " + commandLine);
        };
    }

    @Override
    public CommandResult execute() {
        return CommandResult.success("Supported commands",
                parser.getSupportedCommands()
                        .stream()
                        .map(TypeCommand::getDescription)
                        .sorted()
                        .toArray()
        );
    }
}
