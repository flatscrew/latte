package org.flatscrew.latte;

import org.flatscrew.latte.message.BatchMessage;
import org.flatscrew.latte.message.PrintLineMessage;
import org.flatscrew.latte.message.SequenceMessage;
import org.flatscrew.latte.message.SetWindowTitleMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Command {

    Message execute();

    static Command batch(Command... commands) {
        return () -> new BatchMessage(commands);
    }

    static Command sequence(Command... commands) {
        return () -> new SequenceMessage(commands);
    }

    static Command tick(Duration duration, Function<LocalDateTime, Message> fn) {
        return () -> {
            BlockingQueue<LocalDateTime> queue = new ArrayBlockingQueue<>(1);
            Timer timer = new Timer(true);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    queue.offer(LocalDateTime.now());
                }
            }, duration.toMillis());

            try {
                LocalDateTime time = queue.take();
                timer.cancel();
                return fn.apply(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static Command println(Object... arguments) {
        return () -> new PrintLineMessage(Arrays.stream(arguments)
                .map(String::valueOf)
                .collect(Collectors.joining(" ")));
    }

    static Command printf(String template, Object... arguments) {
        return () -> new PrintLineMessage(template.formatted(arguments));
    }
    static Command setWidowTitle(String title) {
        return () -> new SetWindowTitleMessage(title);
    }
}
