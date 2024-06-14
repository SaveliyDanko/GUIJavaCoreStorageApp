package com.savadanko.common.dto;

import com.savadanko.common.models.Flat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class Request implements Serializable {
    private final String command;
    private final String[] args;
    private final Flat flat;
    private final String userLogin;
}
