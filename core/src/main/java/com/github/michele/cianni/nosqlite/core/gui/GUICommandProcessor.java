package com.github.michele.cianni.nosqlite.core.gui;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.CommandResult;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.command.processor.CommandProcessor;

public final class GUICommandProcessor implements CommandProcessor {

    private final GUICommandParser parser;

    public GUICommandProcessor(Database database) {
        parser = new GUICommandParser(database);
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
