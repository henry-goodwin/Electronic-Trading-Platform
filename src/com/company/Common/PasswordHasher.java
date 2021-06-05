package com.company.Common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordHasher class containing method for hashing a string
 */
public class PasswordHasher {

    /**
     * SHA-256 hashes a string
     * @param inputString input string
     * @return a hashed string
     * @throws Exception throws exception if inputString is invalid
     */
    public static String hashString(String inputString) throws Exception {
        if (!inputString.matches("^[a-zA-Z0-9]{3,}$")) throw new Exception("Error, password is invalid, please ensure it is between 3-10 characters");

        MessageDigest digest;
        StringBuilder stringBuffer = new StringBuilder();

        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(inputString.getBytes());
            byte[] hash = digest.digest();

            for (byte bytes : hash) {
                stringBuffer.append(String.format("%02x", bytes & 0xff));
            }
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }

        return stringBuffer.toString();
    }

}
