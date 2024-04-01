package com.libreforge.integration.security.oauth2.google.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class JwtParser {

    private static final JWTParser PARSER = new JWTParser();

    private JwtParser() {}

    public static String getSubClaim(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String payloadJson = new String(Base64.getUrlDecoder().decode(jwt.getPayload()), StandardCharsets.UTF_8);
        Payload payload = PARSER.parsePayload(payloadJson);

        return payload.getSubject();
    }
}
