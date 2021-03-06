package pom;

import org.openqa.selenium.By;

public class Homepage extends MenuLinksPage {

    private By REGISTERBUTTON = By.xpath("//*[contains(@data-cy,\"nav_sign_up\")]");
    private By EMAILFIELD = By.xpath("//input[contains(@name,\"email\")]");
    private By FIRSTNAMEFIELD = By.xpath("//input[contains(@name,\"firstName\")]");
    private By LASTNAMEFIELD = By.xpath("//input[contains(@name,\"lastName\")]");
    private By PASSWORDFIELD = By.xpath("//input[contains(@name,\"password\")]");
    private By CREATEACCOUNT = By.xpath("//span[contains(text(),'Create an Account')]");
    private By WELCOMETEXT = By.xpath("//body/div[@id='modal']/section[1]/section[1]/div[1]/span[1]");
    private By CROSSBUTTON = By.xpath("//*[@id=\"modal\"]/section/section/button/span/img");

    public Homepage clickRegister() {
        clickOnElement(waitAndReturnElementIfDisplayed(REGISTERBUTTON));
        return this;
    }

    public Homepage createAccount(String email, String firstName, String lastName, String password) {
        sendKeyToElement(EMAILFIELD, email);
        sendKeyToElement(FIRSTNAMEFIELD, firstName);
        sendKeyToElement(LASTNAMEFIELD, lastName);
        sendKeyToElement(PASSWORDFIELD, password);
        clickOnElement(CREATEACCOUNT);
        return this;
    }

    public boolean isAccountCreated(String firstName) {
        timeout(5000);
        if (waitAndReturnElementIfDisplayed(WELCOMETEXT).getText().equals("Welcome " +firstName+"!")) {
            clickOnElement(CROSSBUTTON);
            return true;
        }
        return false;
    }

    public CategoryPage gotoCategoryPage(String firstName, String parentCategory, String childCategory) {
        isAccountCreated(firstName);
        clickParentCategoryLink(parentCategory);
        clickOnElement(By.xpath("//figcaption[contains(text(),'"+childCategory+"')]"));
        return new CategoryPage();
    }

}
