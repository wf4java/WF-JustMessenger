package wf.spring.justmessenger.utils;


import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class RefreshTokenGenerator {

    private static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateToken(int length) {
        StringBuilder token = new StringBuilder(length);
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(SYMBOLS.length());
            token.append(SYMBOLS.charAt(randomIndex));
        }

        return token.toString();
    }


}
