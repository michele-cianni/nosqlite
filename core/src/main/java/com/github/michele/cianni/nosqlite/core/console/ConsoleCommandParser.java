package com.github.michele.cianni.nosqlite.core.console;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.*;
import com.github.michele.cianni.nosqlite.core.command.parser.AbstractCommandParser;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParsingException;
import com.github.michele.cianni.nosqlite.core.command.parser.CommandParser;
import com.github.michele.cianni.nosqlite.core.utils.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ConsoleCommandParser extends AbstractCommandParser {


    public ConsoleCommandParser(Database database) {
        super();
        parsers.putAll(
                Map.of(
                        TypeCommand.GET, GetCommand.parser(database),
                        TypeCommand.INSERT, InsertCommand.parser(database),
                        TypeCommand.DELETE, DeleteCommand.parser(database),
                        TypeCommand.BEGIN, BeginCommand.parser(database),
                        TypeCommand.COMMIT, CommitCommand.parser(database),
                        TypeCommand.ROLLBACK, RollbackCommand.parser(database),
                        TypeCommand.QUERY, QueryCommand.parser(database),
                        TypeCommand.HELP, HelpCommand.parser(this)
                )
        );
    }

    /**
     * Get the supported commands.
     *
     * @return the supported commands
     */
    public List<TypeCommand> getSupportedCommands() {
        return List.copyOf(parsers.keySet());
    }
}
