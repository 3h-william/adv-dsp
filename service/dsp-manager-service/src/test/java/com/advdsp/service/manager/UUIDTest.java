package com.advdsp.service.manager;

import org.apache.commons.lang.RandomStringUtils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 */
public class UUIDTest {
    public static void main(String[] args) {
       /* UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        System.out.println(randomUUIDString);*/

//        SecureRandom random = new SecureRandom();
//        byte bytes[] = new byte[20];
//        random.nextBytes(bytes);
//        String token = String.valueOf(bytes);
//        System.out.println(token);

        System.out.println(generateToken("system"));
    }

    protected static SecureRandom random = new SecureRandom();

    public static synchronized String generateToken(String username) {
        long longToken = Math.abs( random.nextLong() );
        return  Long.toString( longToken, 16 );
    }

}
