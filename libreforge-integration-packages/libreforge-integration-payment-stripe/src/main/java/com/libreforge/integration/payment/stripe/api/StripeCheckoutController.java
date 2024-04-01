package com.libreforge.integration.payment.stripe.api;

import com.libreforge.integration.payment.stripe.api.dto.StripeCheckoutSessionRequest;
import com.libreforge.integration.payment.stripe.api.dto.StripeCheckoutSessionResponse;
import com.libreforge.integration.payment.stripe.api.dto.StripeCheckoutSessionStatusResponse;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class StripeCheckoutController {

    private static final Logger LOG = LoggerFactory.getLogger(StripeCheckoutController.class);

    @Value("${payment.providers.stripe.product}")
    public String stripeProductCd;

    @Value("${payment.providers.stripe.callback.success}")
    public String stripeCallbackSuccess;

    @Value("${payment.providers.stripe.callback.cancel}")
    public String stripeCallbackCancel;

    @PostMapping(value = "/api/integration/payment/providers/stripe/sessions", produces = "application/json")
    public ResponseEntity<StripeCheckoutSessionResponse> createCheckoutSession(
        @Valid @RequestBody StripeCheckoutSessionRequest request
    ) throws Exception {

        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setProduct(stripeProductCd)
                                .setUnitAmount(request.getAmountCents())
                                .setCurrency("usd")
                                .build())
                            .setQuantity(1L)
                            .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeCallbackSuccess)
                .setCancelUrl(stripeCallbackCancel)
                .putAllMetadata(Map.of("invoiceId", request.getInvoiceId()))
                .build();

        Session session = Session.create(params);
        LOG.info("Checkout session {} created", session.getId());

        return ResponseEntity.ok()
            .body(new StripeCheckoutSessionResponse(session.getId()));
    }

    @GetMapping(value = "/api/integration/payment/providers/stripe/sessions/{id}", produces = "application/json")
    public StripeCheckoutSessionStatusResponse StripeCheckoutSessionStatusResponse(@PathVariable("id") String checkoutSessionId)
            throws Exception {

        Session session = Session.retrieve(checkoutSessionId);

        StripeCheckoutSessionStatusResponse response = new StripeCheckoutSessionStatusResponse();
        response.setStatus(session.getStatus());
        response.setPaymentStatus(session.getPaymentStatus());
        response.setPaymentIntent(session.getPaymentIntent());
        response.setPaymentLink(session.getPaymentLink());
        response.setMetadata(session.getMetadata());

        return response;
    }
}
