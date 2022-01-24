package pom;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CategoryPage extends BasePage {

    private By PRODUCTITEMS = By.cssSelector("div.product-item");

    public ProductDetailsPage addProductToBag(int index) {
        List<WebElement> productItems = waitAndReturnElementListIfDisplayed(PRODUCTITEMS);
        clickOnElement(productItems.get(index));
        return new ProductDetailsPage();
    }

}
