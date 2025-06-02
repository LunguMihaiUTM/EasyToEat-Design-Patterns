package agin.designpatternproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/menu")
    public String menuPage() {
        return "menu";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/bookings")
    public String bookingsPage() {
        return "bookings";
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "checkout";
    }

    @GetMapping("/finalize-order")
    public String finalizeOrderPage() {
        return "finalize-order";
    }

}
