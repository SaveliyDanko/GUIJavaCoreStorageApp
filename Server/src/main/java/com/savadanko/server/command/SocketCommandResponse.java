package com.savadanko.server.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.Socket;
import java.util.LinkedHashMap;

@AllArgsConstructor
@Getter
public class SocketCommandResponse {
    private final Socket socket;
    private final String message;
    private final LinkedHashMap<Long, Object> flatMap;
}
