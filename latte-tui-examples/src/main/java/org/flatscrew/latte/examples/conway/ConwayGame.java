package org.flatscrew.latte.examples.conway;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.WindowSizeMessage;

import static org.flatscrew.latte.Command.checkWindowSize;

public class ConwayGame implements Model {
    private Conway conway;

    @Override
    public Command init() {
        return checkWindowSize();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            this.conway = Conway.createRandom(
                    windowSizeMessage.width(),
                    windowSizeMessage.height(),
                    0.5
            );
            return UpdateResult.from(this, conway.init());
        }
        if (conway != null) {
            UpdateResult<? extends Model> updateResult = conway.update(msg);
            return UpdateResult.from(this, updateResult.command());
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        if (conway != null) {
            return conway.view();
        }
        return "";
    }

    public static void main(String[] args) {
        new Program(new ConwayGame())
                .withAltScreen()
                .run();
    }
}
