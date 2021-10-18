package com.example.BlogAppApi.utils;

import com.auth0.jwt.algorithms.Algorithm;

public class Constants {
    public final String secret = "blogapp";
    public final Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public String getSecret() {
        return secret;
    }
}
