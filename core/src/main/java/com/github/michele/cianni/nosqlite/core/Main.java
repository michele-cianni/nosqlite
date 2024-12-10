package com.github.michele.cianni.nosqlite.core;

import com.github.michele.cianni.nosqlite.core.console.NoSQLiteCLI;
import com.github.michele.cianni.nosqlite.core.gui.NoSQLiteGUI;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "gui".equalsIgnoreCase(args[0])) {
            NoSQLiteGUI.launchGUI();
        } else {
            NoSQLiteCLI.launchCLI();
        }
    }
}
