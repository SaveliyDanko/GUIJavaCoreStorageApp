package com.savadanko.common.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User implements Serializable {
    private long id;
    private String login;
    private byte[] passwordHash;
    private byte[] salt; // Добавлено поле для соли

    public User(String login, byte[] passwordHash, byte[] salt) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public User(long id, String login, byte[] passwordHash) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
    }
}
