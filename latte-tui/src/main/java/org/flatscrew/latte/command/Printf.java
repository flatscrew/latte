package org.flatscrew.latte.command;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.message.PrintLineMessage;

public class Printf {

    public static Command printf(String template, Object... arguments) {
        return () -> new PrintLineMessage(template.formatted(arguments));
    }
}
