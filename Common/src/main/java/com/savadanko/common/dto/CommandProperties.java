package com.savadanko.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class CommandProperties implements Serializable {
    private final int argsCount;
    private final boolean object;
}
