package com.savadanko.server.network.authorization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHasher {
    private static final String PEPPER = "fixedSecretPepper"; // Фиксированный секретный перец

    // Генерация случайного соли
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(byte[] password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt); // Добавление соли
        md.update(PEPPER.getBytes()); // Добавление перца
        return md.digest(password);
    }
}
