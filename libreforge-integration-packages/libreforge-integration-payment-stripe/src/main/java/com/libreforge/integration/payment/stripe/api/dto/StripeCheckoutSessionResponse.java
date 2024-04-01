package com.libreforge.integration.payment.stripe.api.dto;

public class StripeCheckoutSessionResponse {

    private String sessionId;

    public StripeCheckoutSessionResponse(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
