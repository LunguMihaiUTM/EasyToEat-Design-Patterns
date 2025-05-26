package agin.designpatternproject.adapter;

import agin.designpatternproject.dto.request.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentAdapter implements PaymentProcessor {

    private final ExternalCardPaymentAPI externalAPI = new ExternalCardPaymentAPI();

    @Override
    public String processPayment(PaymentRequest request) {
        boolean success = externalAPI.makePayment(
                request.getCardNumber(),
                request.getExpiry(),
                request.getCvv(),
                request.getAmount()
        );

        return success ? "Plata a fost procesată cu succes." : "Plata a eșuat.";
    }
}
