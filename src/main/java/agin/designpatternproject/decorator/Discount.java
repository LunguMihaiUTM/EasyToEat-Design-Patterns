package agin.designpatternproject.decorator;

public interface Discount {
    double applyDiscount(double price);
    String getName();
}
