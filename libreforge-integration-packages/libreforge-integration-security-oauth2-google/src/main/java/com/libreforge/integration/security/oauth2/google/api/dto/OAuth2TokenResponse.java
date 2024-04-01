package com.libreforge.integration.security.oauth2.google.api.dto;

import com.libreforge.integration.security.oauth2.google.api.ProviderOAuth2TokenIssue;

public class OAuth2TokenResponse {

    private String sessionId;
    private String accessToken;
    private Integer expiresIn;
    private String tokenType;

    public OAuth2TokenResponse() {
    }

    public OAuth2TokenResponse(ProviderOAuth2TokenIssue providerResponse, String sessionId) {
        this.accessToken = providerResponse.getAccessToken();
        this.expiresIn = providerResponse.getExpiresIn();
        this.tokenType = providerResponse.getTokenType();
        this.sessionId = sessionId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
