package org.flatscrew.latte;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CommandExecutor {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void shutdown() {
        executorService.shutdown();
    }

    public void executeIfPresent(Command command,
                                 Consumer<Message> messageConsumer,
                                 Consumer<Throwable> errorConsumer) {
        if (command != null) {
            CompletableFuture
                    .supplyAsync(command::execute, executorService)
                    .thenAccept(messageConsumer)
                    .exceptionally(ex -> {
                        errorConsumer.accept(ex);
                        return null;
                    });
        }
    }
}
