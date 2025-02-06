package org.flatscrew.latte;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CommandExecutor {

    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors())
    );

    public void shutdown() {
        executorService.shutdown();
    }

    public CompletableFuture<Void> executeIfPresent(Command command,
                                                    Consumer<Message> messageConsumer,
                                                    Consumer<Throwable> errorConsumer) {
        if (command != null) {
            return CompletableFuture
                    .supplyAsync(command::execute, executorService)
                    .orTimeout(5, TimeUnit.SECONDS)
                    .thenAccept(messageConsumer)
                    .exceptionally(ex -> {
                        errorConsumer.accept(ex);
                        return null;
                    });
        }
        return CompletableFuture.completedFuture(null);
    }
}
