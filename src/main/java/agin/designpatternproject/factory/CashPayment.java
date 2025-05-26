package agin.designpatternproject.factory;

import agin.designpatternproject.adapter.PaymentProcessor;
import agin.designpatternproject.dto.request.BookingDTO;
import agin.designpatternproject.dto.request.PaymentRequest;
import agin.designpatternproject.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class CashPayment implements PaymentProcessor {
    @Override
    public String processPayment(PaymentRequest paymentRequest) {
        return "Plata va fi efectuată cu bani cash" ;
    }
}
