package com.github.michele.cianni.nosqlite.core.console;

import com.github.michele.cianni.nosqlite.core.command.CommandResult;

public final class ConsoleCommandResultPrinter {

    public void print(CommandResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(result.message());
        Object[] values = result.values();
        if (values != null && values.length > 0) {
            sb.append(": \n");
            int length = values.length;
            for (int i = 0; i < length; i++) {
                sb.append(values[i]);
                if (i < length - 1) {
                    sb.append("\n");
                }
            }
        }
        System.out.println(sb);
    }


}
