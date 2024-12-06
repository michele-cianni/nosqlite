package com.github.michele.cianni.nosqlite.core.command.parser;

import com.github.michele.cianni.nosqlite.core.command.Command;

public interface CommandParser {

    /**
     * Parse a command line.
     *
     * @param commandLine the command line
     * @return the command
     * @throws CommandParsingException if the command line cannot be parsed
     */
    Command parse(String commandLine) throws CommandParsingException;
}
