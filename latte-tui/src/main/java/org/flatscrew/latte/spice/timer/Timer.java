package org.flatscrew.latte.spice.timer;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;

public class Timer implements Model {
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
}
