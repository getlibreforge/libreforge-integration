package com.libreforge.integration.payment.stripe.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:payment-stripe.properties")
@ComponentScan("com.libreforge.integration.payment.stripe")
public class PaymentStripeConfig {

    @Value("${payment.providers.stripe.private.key}")
    public String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }
}
