package com.savadanko.server.network;

import com.savadanko.server.command.CommandFactory;
import com.savadanko.server.command.CommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionManager implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private final ServerSocket serverSocket;
    private final ExecutorService acceptManager;
    private final BlockingQueue<CommandRequest> requests;
    private final Map<Socket, ObjectStreams> objectStreamMap;
    private final CommandFactory commandFactory;

    public ConnectionManager(int port, BlockingQueue<CommandRequest> requests, Map<Socket, ObjectStreams> objectStreamMap, CommandFactory commandFactory) throws IOException {
        this.objectStreamMap = objectStreamMap;
        this.requests = requests;
        this.serverSocket = new ServerSocket(port);
        this.acceptManager = Executors.newCachedThreadPool();
        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try {
            while (!serverSocket.isClosed()) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectStreams objectStreams = new ObjectStreams(new ObjectInputStream(socket.getInputStream()), new ObjectOutputStream(socket.getOutputStream()));
                    objectStreamMap.put(socket, objectStreams);
                    acceptManager.execute(new ClientHandler(socket, objectStreamMap, requests, commandFactory));
                } catch (IOException e) {
                    if (serverSocket.isClosed()) {
                        log.info("Server socket is closed, stopping ConnectionManager.");
                        break;
                    }
                    log.error("Error accepting connection: {}", e.getMessage(), e);
                }
            }
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            serverSocket.close();
            acceptManager.shutdown();
            log.info("ConnectionManager has been shut down.");
        } catch (IOException e) {
            log.error("Error shutting down ConnectionManager: {}", e.getMessage(), e);
        }
    }
}
