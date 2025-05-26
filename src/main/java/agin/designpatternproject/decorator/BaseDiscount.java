package agin.designpatternproject.decorator;

public class BaseDiscount implements Discount {
    @Override
    public double applyDiscount(double price) {
        return price;
    }

    @Override
    public String getName() {
        return "No Discount";
    }
}
