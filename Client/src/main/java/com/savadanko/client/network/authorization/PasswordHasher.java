package com.savadanko.client.network.authorization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    private static final String PEPPER = "fixedSecretPepper"; // Фиксированный секретный перец

    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        if (salt != null) {
            md.update(salt);
        }
        md.update(PEPPER.getBytes());
        return md.digest(password.getBytes());
    }
}
