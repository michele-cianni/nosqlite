package com.github.michele.cianni.nosqlite.core.gui;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.*;
import com.github.michele.cianni.nosqlite.core.command.parser.AbstractCommandParser;

import java.util.Map;

public final class GUICommandParser extends AbstractCommandParser {

    public GUICommandParser(Database database) {
        super();
        parsers.putAll(
                Map.of(
                        TypeCommand.GET, GetCommand.parser(database),
                        TypeCommand.GET_ALL, GetAllCommand.parser(database),
                        TypeCommand.DELETE, DeleteCommand.parser(database),
                        TypeCommand.INSERT, InsertCommand.parser(database),
                        TypeCommand.BEGIN, BeginCommand.parser(database),
                        TypeCommand.COMMIT, CommitCommand.parser(database),
                        TypeCommand.ROLLBACK, RollbackCommand.parser(database)
                )
        );
    }
}
