package com.project.stephencao.mymobileguardian.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    /**
     * Encrypt the password with Md5
     * Cannot be reversed
     *
     * @param password
     * @return encrypted password
     */
    public static String encryption(String password, boolean isUserPassword) {
        if (isUserPassword) {
            password += "https://github.com/rayray199085"; // Increase the difficulty of decryption
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(password.getBytes());
            for (byte b : bytes) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = hexString.replaceAll("(\\w)", "0$1");
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
