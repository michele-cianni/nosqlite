package com.github.michele.cianni.nosqlite.core.command;

public sealed interface Command permits GetAllCommand, BeginCommand, CommitCommand, DeleteCommand, GetCommand, HelpCommand, InsertCommand, RollbackCommand {

    /**
     * Execute the command.
     *
     * @return the result of the command
     */
    CommandResult execute();
}
