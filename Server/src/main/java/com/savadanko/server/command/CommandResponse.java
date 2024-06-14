package com.savadanko.server.command;

import com.savadanko.common.models.Flat;
import lombok.Getter;

import java.net.Socket;
import java.util.LinkedHashMap;

@Getter
public class CommandResponse {
    private Socket socket;
    private final String message;
    private LinkedHashMap<Long, Object> flatMap;

    public CommandResponse(String message) {
        this.message = message;
    }

    public CommandResponse(String message, LinkedHashMap<Long, Object> flatMap) {
        this.message = message;
        this.flatMap = flatMap;
    }

    public CommandResponse(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }

    public CommandResponse(Socket socket, String message, LinkedHashMap<Long, Object> flatMap) {
        this.socket = socket;
        this.message = message;
        this.flatMap = flatMap;
    }
}
