package org.flatscrew.latte.examples.mouse;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.input.MouseMessage;

public class MouseExample implements Model {
    private MouseMessage mouseMessage;

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        return null;
    }

    @Override
    public String view() {
        return "";
    }

    public static void main(String[] args) {
        new Program(new MouseExample()).run();
    }
}
