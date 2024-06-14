package com.savadanko.server;

import com.savadanko.server.command.CommandExecutor;
import com.savadanko.server.command.CommandFactory;
import com.savadanko.server.command.CommandRequest;
import com.savadanko.server.command.SocketCommandResponse;
import com.savadanko.server.network.ConnectionManager;
import com.savadanko.server.network.ObjectStreams;
import com.savadanko.server.network.ResponseManager;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

public class ServerManager {
    private final BlockingQueue<CommandRequest> requests;
    private final BlockingQueue<SocketCommandResponse> responses;
    private final Map<Socket, ObjectStreams> objectStreamMap;

    private final ExecutorService commandExecutorService;
    private final ExecutorService responseExecutorService;
    private final ExecutorService connectionExecutorService;

    private final ConnectionManager connectionManager;
    private final CommandFactory commandFactory;

    public ServerManager(int port) throws IOException {
        this.requests = new LinkedBlockingQueue<>();
        this.responses = new LinkedBlockingQueue<>();
        this.objectStreamMap = new ConcurrentHashMap<>();

        this.commandExecutorService = Executors.newFixedThreadPool(10);
        this.responseExecutorService = Executors.newFixedThreadPool(10);
        this.connectionExecutorService = Executors.newSingleThreadExecutor();

        this.commandFactory = new CommandFactory();

        this.connectionManager = new ConnectionManager(port, requests, objectStreamMap, commandFactory);
    }

    public void start() {
        connectionExecutorService.execute(connectionManager);
        commandExecutorService.execute(new CommandExecutor(requests, responses, commandFactory));
        responseExecutorService.execute(new ResponseManager(responses, objectStreamMap));
    }

    public void stop() {
        try {
            connectionManager.shutdown();
            shutdownExecutorService(commandExecutorService);
            shutdownExecutorService(responseExecutorService);
            shutdownExecutorService(connectionExecutorService);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("ServerManager was interrupted during shutdown: " + e.getMessage());
        }
    }

    private void shutdownExecutorService(ExecutorService executorService) throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("Executor service did not terminate");
            }
        }
    }
}
