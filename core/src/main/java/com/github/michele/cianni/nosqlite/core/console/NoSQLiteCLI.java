package com.github.michele.cianni.nosqlite.core.console;

import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.CommandResult;
import com.github.michele.cianni.nosqlite.core.factory.NoSQLiteDatabaseFactory;

import java.util.Scanner;

public final class NoSQLiteCLI {

    private final ConsoleCommandProcessor processor;

    private final ConsoleCommandResultPrinter printer;

    public NoSQLiteCLI() {
        NoSQLiteDatabaseFactory factory = new NoSQLiteDatabaseFactory();
        this.processor = new ConsoleCommandProcessor(factory.createDatabase("database.json"));
        this.printer = new ConsoleCommandResultPrinter();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("NoSQLite CLI");
        System.out.println("Type 'help' for a list of commands");
        System.out.println("Type 'exit' to quit");
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                break;
            }
            CommandResult commandResult = processor.process(command);
            if (commandResult != null) {
                printer.print(commandResult);
            }
        }
        scanner.close();
    }

    public static void launchCLI() {
        new NoSQLiteCLI().start();
    }
}
