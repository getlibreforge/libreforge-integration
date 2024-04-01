package com.libreforge.integration.payment.stripe.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StripeCheckoutSessionRequest {

    private BigDecimal amount;
    private String invoiceId;


    public BigDecimal getAmount() {
        return amount;
    }

    public long getAmountCents() {
        return amount.longValue() * 100;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }
}
