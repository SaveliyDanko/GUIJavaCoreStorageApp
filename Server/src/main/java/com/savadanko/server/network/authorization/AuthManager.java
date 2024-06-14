package com.savadanko.server.network.authorization;

import com.savadanko.common.dto.AuthDTO;
import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.dto.Status;
import com.savadanko.common.models.User;
import com.savadanko.server.command.CommandFactory;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AuthManager {
    private static final Logger log = LoggerFactory.getLogger(AuthManager.class);
    private final DataBaseHandler dataBaseHandler;

    public AuthManager() {
        dataBaseHandler = DataBaseHandler.getInstance();
    }

    public AuthResponse authorization(ObjectInputStream ois, CommandFactory commandFactory) {
        try {
            AuthDTO authDTO = (AuthDTO) ois.readObject();
            for (Object o : dataBaseHandler.readAll(Tables.USERS).values()) {
                User user = (User) o;
                if (user.getLogin().equals(authDTO.getLogin())) {
                    byte[] hashedPassword = PasswordHasher.hashPassword(authDTO.getHashedPassword(), user.getSalt());
                    if (Arrays.equals(user.getPasswordHash(), hashedPassword)) {
                        log.info("User {} authenticated successfully", user.getLogin());
                        return new AuthResponse(
                                "Valid auth",
                                commandFactory.getCommandPropertiesMap(),
                                dataBaseHandler.readAll(Tables.FLATS),
                                Status.STATUS_200);
                    } else {
                        log.warn("Invalid password for user {}", user.getLogin());
                        return new AuthResponse("Invalid auth", Status.STATUS_400);
                    }
                }
            }
            register(authDTO.getLogin(), authDTO.getHashedPassword());
            log.info("New user {} registered successfully", authDTO.getLogin());
            return new AuthResponse(
                    "Registration",
                    commandFactory.getCommandPropertiesMap(),
                    dataBaseHandler.readAll(Tables.FLATS),
                    Status.STATUS_200);
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
            log.error("Error during authorization: {}", e.getMessage(), e);
            return new AuthResponse("Authorization error", Status.STATUS_400);
        }
    }

    private void register(String login, byte[] password) throws NoSuchAlgorithmException {
        byte[] salt = PasswordHasher.generateSalt();
        byte[] hashedPassword = PasswordHasher.hashPassword(password, salt);
        User user = new User(login, hashedPassword, salt);
        dataBaseHandler.create(user, Tables.USERS);
        log.info("User {} added to the database", user.getLogin());
    }
}
