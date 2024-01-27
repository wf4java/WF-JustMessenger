package wf.spring.justmessenger.utils;


import java.security.SecureRandom;
import java.util.Base64;

public class RefreshTokenGenerator {


    public static String generateToken(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[length];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

}
