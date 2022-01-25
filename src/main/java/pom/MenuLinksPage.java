package pom;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MenuLinksPage extends BasePage {

    private By MENULINKS = By.cssSelector("ul.menu-links>li.menu-link>div.dropdown>div.dropdown_wrapper>button.dropdown_header>span.body2");

    public void clickParentCategoryLink(String parentCategory) {
        List<WebElement> menuLinks = waitAndReturnElementListIfDisplayed(MENULINKS);
        for (WebElement menuLink : menuLinks) {
            if (menuLink.getText().equalsIgnoreCase(parentCategory)) {
                clickOnElement(menuLink);
                break;
            }
        }
    }
}
