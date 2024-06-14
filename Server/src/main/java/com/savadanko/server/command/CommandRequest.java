package com.savadanko.server.command;

import com.savadanko.common.dto.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.Socket;

@AllArgsConstructor
@Getter
public class CommandRequest {
    private final Socket socket;
    private final Request request;
}
