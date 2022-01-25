package base;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.WebEventListener;
import util.wait.WaitHelper;
import util.wait.WaitTime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class BasePage {

    public static WebDriver driver;
    public static Properties prop;
    public  static EventFiringWebDriver e_driver;
    public static WebEventListener eventListener;
    private FluentWait wait;

    public BasePage() {
        try{
            prop = new Properties();
            FileInputStream ip = new FileInputStream("src/main/resources/config/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void initialization(){
        String browserName = prop.getProperty("browser");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("incognito");
        options.addArguments("start-maximized");
        options.addArguments("enable-automation");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if (browserName.equals("chrome")){
            driver = new ChromeDriver(options);
        } else if (browserName.equals("FF")){
            driver = new FirefoxDriver();
        } else if (browserName.equals("headless")){
            options.addArguments("window-size=1400,800");
            options.addArguments("headless");
            driver = new ChromeDriver(options);
        }

        e_driver = new EventFiringWebDriver(driver);
        // Now create object of EventListerHandler to register it with EventFiringWebDriver
        eventListener = new WebEventListener();
        e_driver.register(eventListener);
        driver = e_driver;

        driver.get(prop.getProperty("url"));
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(WaitTime.XL, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(WaitTime.M, TimeUnit.SECONDS);
    }

    public void timeout(long timeout) {
        try {
            Thread.sleep(timeout);
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void clickOnElement(WebElement element){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public void clickOnElement(By locator){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        WebElement element = driver.findElement(locator);
        element.click();
    }

    public void clickCenterOfScreen() {
        Dimension center = driver.manage().window().getSize();
        Actions builder = new Actions(driver);
        builder.moveByOffset(center.width/2, center.height/2).click().build().perform();
    }

    public void sendKeyToElement(By locator, String text){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.findElement(locator).sendKeys(text);
    }

    public void selectDropDownByTitle(WebElement element, String text){
        WebDriverWait wait = new WebDriverWait(driver, WaitTime.M);
        Select select = new Select(wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element)));
        select.selectByVisibleText(text);
    }

    public WebElement waitAndReturnElementIfDisplayed(By locator) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            return (WebElement) wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not displayed:" + locator.toString());
        }
    }

    public List<WebElement> waitAndReturnElementListIfDisplayed(By locator) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            List<WebElement> elementList =driver.findElements(locator);
            return  elementList;
        }
        catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not displayed:" + locator.toString());
        }
    }

}
