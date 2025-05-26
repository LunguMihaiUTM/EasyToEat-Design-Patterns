package agin.designpatternproject.decorator;

public class EasyToEatDiscount extends DiscountDecorator {
    public EasyToEatDiscount(Discount discount) {
        super(discount);
    }

    @Override
    public double applyDiscount(double price) {
        return super.applyDiscount(price) * 0.95;
    }

    @Override
    public String getName() {
        return "App special promo code";
    }
}
