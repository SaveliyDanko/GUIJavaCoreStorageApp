package com.savadanko.server.network;

import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.dto.Request;
import com.savadanko.common.dto.Status;
import com.savadanko.server.command.CommandFactory;
import com.savadanko.server.command.CommandRequest;
import com.savadanko.server.network.authorization.AuthManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final CommandFactory commandFactory;
    private final BlockingQueue<CommandRequest> requests;
    private final AuthManager authManager;

    public ClientHandler(Socket socket, Map<Socket, ObjectStreams> objectStreamMap, BlockingQueue<CommandRequest> requests, CommandFactory commandFactory) throws IOException {
        this.socket = socket;
        this.authManager = new AuthManager();
        this.requests = requests;

        this.in = objectStreamMap.get(socket).getIn();
        this.out = objectStreamMap.get(socket).getOut();
        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try {
            if (authenticate()) {
                processRequests();
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            log.error("Error handling client: {}", e.getMessage(), e);
        } finally {
            closeResources();
        }
    }

    private boolean authenticate() throws IOException, ClassNotFoundException {
        while (true) {
            AuthResponse response = authManager.authorization(in, commandFactory);
            out.writeObject(response);
            out.flush();

            if (response.getStatus().equals(Status.STATUS_200)) {
                return true;
            }
        }
    }

    private void processRequests() throws IOException, ClassNotFoundException, InterruptedException {
        while (true) {
            Request request = (Request) in.readObject();
            CommandRequest commandRequest = new CommandRequest(socket, request);
            requests.put(commandRequest);
        }
    }

    private void closeResources() {
        try {
            in.close();
        } catch (IOException e) {
            log.error("Error closing input stream: {}", e.getMessage(), e);
        }
        try {
            out.close();
        } catch (IOException e) {
            log.error("Error closing output stream: {}", e.getMessage(), e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Error closing socket: {}", e.getMessage(), e);
        }
    }
}
