package com.github.michele.cianni.nosqlite.core.command.processor;

import com.github.michele.cianni.nosqlite.core.command.CommandResult;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;

public interface CommandProcessor {

    /**
     * Process a command line.
     *
     * @param commandLine the command line
     * @return the result of the command
     */
    CommandResult process(String commandLine);

}
