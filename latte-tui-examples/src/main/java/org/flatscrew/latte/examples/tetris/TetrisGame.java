package org.flatscrew.latte.examples.tetris;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Position;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.border.StandardBorder;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.cream.join.HorizontalJoinDecorator;
import org.flatscrew.latte.cream.join.VerticalJoinDecorator;
import org.flatscrew.latte.cream.placement.PlacementDecorator;
import org.flatscrew.latte.message.CheckWindowSizeMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.message.WindowSizeMessage;

public class TetrisGame implements Model {

    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;

    private final Grid grid;

    private WindowSizeMessage windowSizeMessage = new WindowSizeMessage(0, 0);

    private Style borderStyle = Style.newStyle()
            .border(StandardBorder.RoundedBorder)
            .borderForeground(Color.color("63"));

    private Style gridStyle = borderStyle.copy();

    private Style scoreStyle = borderStyle.copy()
            .width(20)
            .align(Position.Center, Position.Center);

    private Style nextBlockStyle = borderStyle.copy()
            .width(20)
            .height(8)
            .align(Position.Center, Position.Center);

    private Style rightPanelStyle = Style.newStyle()
            .marginLeft(2);

    public TetrisGame(int score) {
        this.grid = new Grid(GRID_WIDTH, GRID_HEIGHT);
    }

    @Override
    public Command init() {
        return Command.batch(grid.init(), CheckWindowSizeMessage::new);
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage sizeMsg) {
            this.windowSizeMessage = sizeMsg;
        } else if (msg instanceof GameOverMessage) {
            return UpdateResult.from(this, QuitMessage::new);
        }
        UpdateResult<? extends Model> updateResult = grid.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    @Override
    public String view() {
        if (grid.gameOver()) {
            return "Final score: %d".formatted(grid.score());
        }

        String grid = gridStyle.render(this.grid.view());

        String nextBlockPanel = nextBlockStyle.render(
                VerticalJoinDecorator.joinVertical(
                        Position.Center,
                        "Next block:\n\n",
                        this.grid.nextBlockPreview()
                )
        );

        String rightPanel = VerticalJoinDecorator.joinVertical(
                Position.Left,
                scoreStyle.render("Level:\n%d".formatted(this.grid.level())),
                scoreStyle.render("Score:\n%d".formatted(this.grid.score())),
                nextBlockPanel
        );

        String gameView = HorizontalJoinDecorator.joinHorizontal(
                Position.Top,
                grid,
                rightPanelStyle.render(rightPanel)
        );

        if (windowSizeMessage == null || windowSizeMessage.width() == 0) {
            return gameView;
        }

        return PlacementDecorator.placeHorizontal(
                windowSizeMessage.width(),
                Position.Center,
                gameView
        );
    }

    public static void main(String[] args) {
        new Program(new TetrisGame(0))
//                .withAltScreen()
                .run();
    }

}