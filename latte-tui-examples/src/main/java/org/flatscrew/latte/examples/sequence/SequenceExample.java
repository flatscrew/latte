package org.flatscrew.latte.examples.sequence;

import org.flatscrew.latte.*;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

import static org.flatscrew.latte.Command.*;

public class SequenceExample implements Model {

    @Override
    public Command init() {
        return sequence(
                sequence(
                        println("A"),
                        println("B"),
                        println("C")
                ),
                println("X"),
                println("Y"),
                println("Z"),
                QuitMessage::new
        );
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage key) {
            return UpdateResult.from(this, QuitMessage::new);
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return "";
    }

    public static void main(String[] args) {
        new Program(new SequenceExample()).run();
    }
}
