package agin.designpatternproject.adapter;

import agin.designpatternproject.dto.request.PaymentRequest;

public interface PaymentProcessor {
    String processPayment(PaymentRequest request);
}
