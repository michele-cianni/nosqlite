package com.github.michele.cianni.nosqlite.core.console;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.CommandResult;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.command.processor.CommandProcessor;

public final class ConsoleCommandProcessor implements CommandProcessor {

    private final ConsoleCommandParser parser;

    public ConsoleCommandProcessor(Database database) {
        parser = new ConsoleCommandParser(database);
    }

    @Override
    public CommandResult process(String commandLine) {
        try {
            return parser.parse(commandLine).execute();
        } catch (CommandParsingException e) {
            return CommandResult.error(e.getMessage());
        }
    }
}
