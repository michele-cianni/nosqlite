package com.github.michele.cianni.nosqlite.core.command;

import java.util.Arrays;

public record CommandResult(boolean status, String message, Object... values) {

    /**
     * Create a successful command result.
     *
     * @param message the message
     * @param values the values
     * @return a successful command result
     */
    public static CommandResult success(String message, Object... values) {
        return new CommandResult(true, message, values);
    }

    /**
     * Create a failed command result.
     *
     * @param message the message
     * @return a failed command result
     */
    public static CommandResult failure(String message) {
        return new CommandResult(false, message);
    }

    /**
     * Create an error command result.
     *
     * @param message the message
     * @return an error command result
     */
    public static CommandResult error(String message) {
        return new CommandResult(false, message);
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
