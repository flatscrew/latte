package org.flatscrew.latte.command;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.message.BatchMessage;

public class Sequence {

    public static Command sequence(Command... commands) {
        return () -> new BatchMessage(commands);
    }
}
