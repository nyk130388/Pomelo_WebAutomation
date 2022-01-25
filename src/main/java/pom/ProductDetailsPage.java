package pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductDetailsPage extends MenuLinksPage {

    private By ALLSIZE = By.cssSelector("div.pdp__options-container>div>button");
    private By ADDTOBAGBUTTON = By.xpath("//span[contains(text(),'Add To Bag')]");
    private By SHOPPINGBAGBUTTON = By.xpath("//span[contains(text(),'View My Shopping Bag')]");
    private By QUANTITYOPTIONS = By.cssSelector(".cart-item-info__quantity .pml-dropdown__select");
    private By CHECKOUTBUTTON = By.xpath("//span[contains(text(),'proceed to Checkout')]");
    private By REMOVEICONS = By.cssSelector(".cart-remove");

    public ProductDetailsPage addItemsToBag(String size) {
        List<WebElement> allSize = waitAndReturnElementListIfDisplayed(ALLSIZE);
        for (WebElement desiredSize : allSize) {
            if (desiredSize.getText().equals(size)) {
                clickOnElement(desiredSize);
                break;
            }
        }
        clickOnElement(ADDTOBAGBUTTON);
        clickOnElement(SHOPPINGBAGBUTTON);
        return this;
    }

    public CategoryPage gotoCategoryPage(String parentCategory, String childCategory) {
        clickCenterOfScreen();
        clickParentCategoryLink(parentCategory);
        clickOnElement(By.xpath("//figcaption[contains(text(),'"+childCategory+"')]"));
        return new CategoryPage();
    }

    public ProductDetailsPage removeItemsFromBag(int index) {
        List<WebElement> allRemoveIcons = waitAndReturnElementListIfDisplayed(REMOVEICONS);
        clickOnElement(allRemoveIcons.get(index));
        return this;
    }

    public ProductDetailsPage adjustProductQtyInBag(String quantity) {
        selectDropDownByTitle(waitAndReturnElementIfDisplayed(QUANTITYOPTIONS),quantity);
        return this;
    }

    public CheckoutPage proceedToCheckout() {
        clickOnElement(CHECKOUTBUTTON);
        return new CheckoutPage();
    }

}
