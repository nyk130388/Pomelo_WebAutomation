package test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pom.Homepage;

public class MyShoppingBagTest extends BaseTest {

    Homepage homepage;

    @Test
    public void createAccountTest() {
        String email = "nyk130388+"+getRandomKey(3)+"@gmail.com";
        String firstName = email.split("@")[1];
        String lastName = "Khan";
        String password = getRandomKey();
        String category = "New Arrivals";

        homepage = new Homepage();
        boolean isCheckoutSuccessful = homepage
                .clickRegister()
                .createAccount(email,firstName,lastName,password)
                .gotoCategoryPage(firstName, category, category)
                .addProductToBag(0)
                .addItemsToBag("L")
                .adjustProductQtyInBag("2")
                .proceedToCheckout()
                .isCheckoutPage();

        Assert.assertTrue(isCheckoutSuccessful);
    }

}
