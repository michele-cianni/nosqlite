package com.github.michele.cianni.nosqlite.core.command;

public sealed interface Command permits
        BeginCommand, CommitCommand, DeleteCommand, GetCommand, InsertCommand, RollbackCommand {

    /**
     * Execute the command.
     *
     * @return the result of the command
     */
    CommandResult execute();
}
