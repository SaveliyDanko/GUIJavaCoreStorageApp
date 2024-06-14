package com.savadanko.common.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthDTO implements Serializable {
    private String login;
    private byte[] hashedPassword;
    private byte[] salt; // Добавлено поле для соли
}
