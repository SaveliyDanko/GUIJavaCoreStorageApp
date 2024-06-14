package com.savadanko.server.command;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class CommandHistory {

    private static final AtomicReference<CommandHistory> INSTANCE = new AtomicReference<>();

    private final ConcurrentLinkedQueue<String> history;

    private CommandHistory() {
        history = new ConcurrentLinkedQueue<>();
    }

    public static CommandHistory getInstance() {
        while (true) {
            CommandHistory current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new CommandHistory();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    public void addCommand(String command) {
        history.add(command);
    }

    public ConcurrentLinkedQueue<String> getHistory() {
        return new ConcurrentLinkedQueue<>(history);
    }

    public void clearHistory() {
        history.clear();
    }

    // Метод для получения последней команды
    public String getLastCommand() {
        return history.peek();
    }
}

