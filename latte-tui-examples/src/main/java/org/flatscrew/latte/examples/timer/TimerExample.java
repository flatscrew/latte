package org.flatscrew.latte.examples.timer;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.spice.timer.StartStopMessage;
import org.flatscrew.latte.spice.timer.TickMessage;
import org.flatscrew.latte.spice.timer.Timer;

public class TimerExample implements Model {

    private Model timer;

    public TimerExample(Timer timer) {
        this.timer = timer;
    }

    @Override
    public Command init() {
        return timer.init();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TickMessage) {
            UpdateResult<? extends Model> updateResult = timer.update(msg);
            this.timer = updateResult.model();
            return UpdateResult.from(this, updateResult.command());
        } else if (msg instanceof StartStopMessage) {
            UpdateResult<? extends Model> updateResult = timer.update(msg);
            this.timer = updateResult.model();
        }
        return null;
    }

    @Override
    public String view() {
        return "";
    }

    public static void main(String[] args) {
        new Program(new TimerExample(
                new Timer()
        )).run();
    }
}
