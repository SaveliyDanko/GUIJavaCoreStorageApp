package com.savadanko.server.command;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class CommandExecutor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);
    private final BlockingQueue<CommandRequest> requests;
    private final BlockingQueue<SocketCommandResponse> responses;
    private final CommandFactory commandFactory;
    CommandHistory commandHistory;

    public CommandExecutor(BlockingQueue<CommandRequest> requests,
                           BlockingQueue<SocketCommandResponse> responses,
                           CommandFactory commandFactory) {

        this.requests = requests;
        this.responses = responses;
        this.commandFactory = commandFactory;
        this.commandHistory = CommandHistory.getInstance();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                CommandRequest commandRequest = requests.take();
                Class<?> commandClass = commandFactory
                        .getCommandMap()
                        .get(commandRequest.getRequest().getCommand());

                if (commandClass != null) {
                    try {
                        Object object = commandClass
                                .getDeclaredConstructor(
                                        String[].class,
                                        Flat.class,
                                        String.class)
                                .newInstance(
                                        commandRequest.getRequest().getArgs(),
                                        commandRequest.getRequest().getFlat(),
                                        commandRequest.getRequest().getUserLogin());

                        Command command = (Command) object;
                        CommandResponse commandResponse = command.execute();
                        SocketCommandResponse socketCommandResponse = new SocketCommandResponse(
                                commandRequest.getSocket(),
                                commandResponse.getMessage(),
                                commandResponse.getFlatMap());

                        commandHistory.addCommand(commandRequest.getRequest().getCommand());

                        responses.put(socketCommandResponse);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        log.error("Error instantiating command: {}", e.getMessage(), e);
                        SocketCommandResponse socketCommandResponse = new SocketCommandResponse(
                                commandRequest.getSocket(),
                                "Error executing command: " + e.getMessage(),
                                null);
                        responses.put(socketCommandResponse);
                    }
                } else {
                    log.warn("Command not found: {}", commandRequest.getRequest().getCommand());
                    SocketCommandResponse socketCommandResponse = new SocketCommandResponse(
                            commandRequest.getSocket(),
                            "Command not found: " + commandRequest.getRequest().getCommand(),
                            null);
                    responses.put(socketCommandResponse);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("CommandExecutor interrupted");
        }
    }
}
