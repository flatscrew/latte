package org.flatscrew.latte;

import org.flatscrew.latte.message.BatchMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.message.SequenceMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IgnoredMessage implements Message {
}

class TestModel implements Model {
    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        return new UpdateResult<>(this, null);
    }

    @Override
    public String view() {
        return "";
    }
}

class ProgramTest {

    @Test
    void test_ShouldExecuteCommandsInParallel_ForBatchMessage() {
        // given
        TestModel testModel = new TestModel();
        Program program = new Program(testModel);

        List<String> executionOrder = new ArrayList<>();

        Command slow = () -> {
            sleep(300);
            executionOrder.add("slow");
            return new IgnoredMessage();
        };

        Command fast = () -> {
            sleep(100);  // Faster command
            executionOrder.add("fast");
            return new IgnoredMessage();
        };

        Thread programThread = new Thread(program::run);
        programThread.start();

        // when
        program.send(new BatchMessage(
                slow,
                fast,
                () -> {
                    sleep(500);
                    return new QuitMessage();
                }
        ));

        try {
            programThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // then
        assertEquals(Arrays.asList("fast", "slow"), executionOrder,
                "Fast command should complete before slow command when running in parallel");
    }

    @Test
    void test_ShouldExecuteCommandsInOrder_ForSequenceMessage() {
        // given
        TestModel testModel = new TestModel();
        Program program = new Program(testModel);

        List<String> executionOrder = new ArrayList<>();

        Command first = () -> {
            sleep(300);  // Longer delay
            executionOrder.add("first");
            return new IgnoredMessage();
        };

        Command second = () -> {
            sleep(100);  // Shorter delay
            executionOrder.add("second");
            return new IgnoredMessage();
        };

        Thread programThread = new Thread(program::run);
        programThread.start();

        // when
        program.send(new SequenceMessage(
                first,
                second,
                QuitMessage::new
        ));

        try {
            programThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // then
        assertEquals(Arrays.asList("first", "second"), executionOrder,
                "Commands should execute in sequence despite different delays");
    }

    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}