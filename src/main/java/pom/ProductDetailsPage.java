package pom;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductDetailsPage extends BasePage {

    private By ALLSIZE = By.cssSelector("div.pdp__options-container>div>button");
    private By ADDTOBAGBUTTON = By.xpath("//span[contains(text(),'Add To Bag')]");
    private By SHOPPINGBAGBUTTON = By.xpath("//span[contains(text(),'View My Shopping Bag')]");
    private By QUANTITYOPTIONS = By.cssSelector(".cart-item-info__quantity .pml-dropdown__select");
    private By CHECKOUTBUTTON = By.xpath("//span[contains(text(),'proceed to Checkout')]");

    public ProductDetailsPage addItemsToBag(String size) {
        List<WebElement> allSize = waitAndReturnElementListIfDisplayed(ALLSIZE);
        for (WebElement desiredSize : allSize) {
            if (desiredSize.getText().equals(size)) {
                clickOnElement(desiredSize);
                break;
            }
        }
        clickOnElement(ADDTOBAGBUTTON);
        return this;
    }

    public ProductDetailsPage adjustProductQtyInBag(String quantity) {
        clickOnElement(SHOPPINGBAGBUTTON);
        selectDropDownByTitle(waitAndReturnElementIfDisplayed(QUANTITYOPTIONS),quantity);
        return this;
    }

    public CheckoutPage proceedToCheckout() {
        clickOnElement(CHECKOUTBUTTON);
        return new CheckoutPage();
    }

}
