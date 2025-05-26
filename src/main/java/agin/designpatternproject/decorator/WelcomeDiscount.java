package agin.designpatternproject.decorator;

public class WelcomeDiscount extends DiscountDecorator {
    public WelcomeDiscount(Discount discount) {
        super(discount);
    }

    @Override
    public double applyDiscount(double price) {
        return super.applyDiscount(price) * 0.9;
    }

    @Override
    public String getName() {
        return "Discount for yours first order";
    }
}

