package com.task_management.config;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class JwtConfig {
    private final String secretKey = genValidSecretKey("secret_key");
    private final long expiresInMs = hoursToMs(1);

    private long hoursToMs(long hours) {
        return hours * 60 * 60 * 1000;
    }

    private String genValidSecretKey(String secretKey){
        int repeats = 1;

        while(secretKey.length() < 22){
            secretKey = secretKey.repeat(repeats);
            repeats++;
        }
        return secretKey;
    }
}
