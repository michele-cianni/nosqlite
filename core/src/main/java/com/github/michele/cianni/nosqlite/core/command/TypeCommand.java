package com.github.michele.cianni.nosqlite.core.command;

import java.util.Arrays;
import java.util.Optional;

public enum TypeCommand {
    GET("get", "<key>", "Get a value from the database"),
    GET_ALL("getAll", null, "Get all values from the database"),
    INSERT("insert", "<key> <value>", "Insert a value in the database"),
    DELETE("delete", "<key>", "Delete a value from the database"),
    BEGIN("begin", null, "Begin a transaction"),
    COMMIT("commit", null, "Commit a transaction"),
    ROLLBACK("rollback", null, "Rollback a transaction"),
    HELP("help", null, "Show the list of available commands"),
    QUERY("query", "filter [SORT field ASC|DESC]", "Execute a query on the database with the given filter and sort fields");

    private final String nameCommand;
    private final String parameterDescription;
    private final String description;

    TypeCommand(String nameCommand, String parameterDescription, String description) {
        this.nameCommand = nameCommand;
        this.parameterDescription = parameterDescription;
        this.description = description;
    }

    public boolean check(String value) {
        return this.nameCommand.equalsIgnoreCase(value);
    }

    public static Optional<TypeCommand> fromString(String value) {
        return Arrays.stream(values())
                .filter(typeCommand -> typeCommand.check(value))
                .findFirst();
    }

    public String getDescription() {
        String result = name();
        if (parameterDescription != null) {
            result += " " + parameterDescription;
        }
        return result + " - " + description;

    }
}
