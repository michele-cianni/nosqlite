package com.github.michele.cianni.nosqlite.core.command;

public sealed interface Command permits BeginCommand, CommitCommand, DeleteCommand, GetAllCommand, GetCommand, HelpCommand, InsertCommand, QueryCommand, RollbackCommand {

    /**
     * Execute the command.
     *
     * @return the result of the command
     */
    CommandResult execute();
}
