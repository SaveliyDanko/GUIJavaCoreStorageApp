package com.savadanko.server.network.authorization;

import lombok.Getter;

@Getter
public class AuthRequest {
    private final String message;

    public AuthRequest(String message) {
        this.message = message;
    }

}
