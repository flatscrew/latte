package org.flatscrew.latte.examples.error;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;

public class ErrorExample implements Model {

    @Override
    public Command init() {
        return this::failingCommand;
    }

    private Message failingCommand() {
        throw new RuntimeException("Error!");
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return "Error?";
    }

    public static void main(String[] args) {
        new Program(new ErrorExample()).run();
    }
}
