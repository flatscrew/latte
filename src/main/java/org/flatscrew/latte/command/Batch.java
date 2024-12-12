package org.flatscrew.latte.command;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.message.BatchMessage;

public class Batch {

    public static Command batch(Command... commands) {
        return () -> new BatchMessage(commands);
    }
}
