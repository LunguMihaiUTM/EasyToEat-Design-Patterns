package agin.designpatternproject.factory;

import agin.designpatternproject.adapter.CardPaymentAdapter;
import agin.designpatternproject.adapter.PaymentProcessor;
import agin.designpatternproject.exception.RestaurantResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    public PaymentProcessor getPaymentProcessor(String paymentType) {
        return switch (paymentType.toLowerCase()) {
            case "card" -> new CardPaymentAdapter();
            case "cash" -> new CashPayment();
            default -> throw new RestaurantResourceException("Invalid payment type");
        };
    }
}
