package com.savadanko.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.LinkedHashMap;

@AllArgsConstructor
@Getter
public class Response implements Serializable {
    private final String message;
    private final LinkedHashMap<Long, Object> flatMap;
}
