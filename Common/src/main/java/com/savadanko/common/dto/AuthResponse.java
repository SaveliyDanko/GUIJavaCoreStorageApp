package com.savadanko.common.dto;


import com.savadanko.common.models.Flat;
import lombok.Getter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class AuthResponse implements Serializable {
    private Map<String, CommandProperties> commandPropertiesMap;
    private LinkedHashMap<Long, Object> flatMap;
    private final String message;
    private final Status status;

    public AuthResponse(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public AuthResponse(String message, Map<String, CommandProperties> commandPropertiesMap, LinkedHashMap<Long, Object> flatMap, Status status) {
        this.message = message;
        this.commandPropertiesMap = commandPropertiesMap;
        this.flatMap = flatMap;
        this.status = status;
    }

}
