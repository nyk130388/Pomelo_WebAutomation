package pom;

import base.BasePage;

public class CheckoutPage extends BasePage {

    public boolean isCheckoutPage() {
        timeout(5000);
        if (driver.getCurrentUrl().equals("https://www.pomelofashion.com/th/en/checkout")) {
            return true;
        }
        return false;
    }
}
