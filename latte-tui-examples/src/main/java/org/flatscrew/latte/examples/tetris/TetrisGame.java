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
import org.flatscrew.latte.message.WindowSizeMessage;

public class TetrisGame implements Model {

    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 20;

    private final String grid;
    private final int score;
    private final String nextBlock;

    private WindowSizeMessage windowSizeMessage = new WindowSizeMessage(0, 0);

    private Style borderStyle = Style.newStyle()
            .border(StandardBorder.RoundedBorder)
            .borderForeground(Color.color("63"));

    private Style gridStyle = borderStyle.copy()
            .width(GRID_WIDTH)
            .height(GRID_HEIGHT)
            .align(Position.Center, Position.Center);

    private Style scoreStyle = borderStyle.copy()
            .width(20)
            .height(5)
            .align(Position.Center, Position.Center);

    private Style nextBlockStyle = borderStyle.copy()
            .width(20)
            .height(8)
            .align(Position.Center, Position.Center);

    private Style rightPanelStyle = Style.newStyle()
            .marginLeft(2);

    public TetrisGame(String grid, int score, String nextBlock) {
        this.grid = grid;
        this.score = score;
        this.nextBlock = nextBlock;
    }

    @Override
    public Command init() {
        return CheckWindowSizeMessage::new;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage sizeMsg) {
            this.windowSizeMessage = sizeMsg;
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        String grid = gridStyle.render(this.grid);

        String scoreText = "Score:\n%d".formatted(score);
        String scorePanel = scoreStyle.render(scoreText);

        String nextBlockPanel = nextBlockStyle.render(nextBlock);

        String rightPanel = VerticalJoinDecorator.joinVertical(
                Position.Left,
                scorePanel,
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
        new Program(new TetrisGame("Game Grid\nWill be here", 0, "Next Block\nPreview")).run();
    }
}