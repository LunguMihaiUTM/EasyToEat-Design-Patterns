package agin.designpatternproject.factory;

import agin.designpatternproject.decorator.BaseDiscount;
import agin.designpatternproject.decorator.Discount;
import agin.designpatternproject.decorator.EasyToEatDiscount;
import agin.designpatternproject.decorator.WelcomeDiscount;

public class DiscountFactory {

    public static Discount getDiscountByPromoCode(String promoCode) {
        Discount base = new BaseDiscount();

        switch (promoCode.toUpperCase()) {
            case "WELCOME":
                return new WelcomeDiscount(base);
            case "EASYTOEAT":
                return new EasyToEatDiscount(base);
            default:
                return base;
        }
    }
}
