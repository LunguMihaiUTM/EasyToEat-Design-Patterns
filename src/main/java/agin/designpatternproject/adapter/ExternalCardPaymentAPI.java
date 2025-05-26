package agin.designpatternproject.adapter;

public class ExternalCardPaymentAPI {
    public boolean makePayment(String cardNumber, String expiry, String cvv, double amount) {
        return cardNumber != null && amount > 0;
    }
}
