package agin.designpatternproject.decorator;

public abstract class DiscountDecorator implements Discount {
    protected final Discount decoratedDiscount;

    public DiscountDecorator(Discount decoratedDiscount) {
        this.decoratedDiscount = decoratedDiscount;
    }

    @Override
    public double applyDiscount(double price) {
        return decoratedDiscount.applyDiscount(price);
    }

    @Override
    public String getName() {
        return decoratedDiscount.getName();
    }
}

