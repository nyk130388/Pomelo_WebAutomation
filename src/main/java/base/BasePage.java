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
import org.sikuli.script.Finder;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import util.WebEventListener;
import util.wait.WaitHelper;
import util.wait.WaitTime;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    public static void openNewWindow(String url){
        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get(url);
//        System.out.println(driver.getTitle());
    }

    public void switchToParentWindow(){
        Set<String> handles = driver.getWindowHandles();
        List<String> ls = new ArrayList<String>(handles);
        driver.switchTo().window(ls.get(0));
        timeout(2000);
    }

    public void switchToChildWindow(int index){
        Set<String> handles = driver.getWindowHandles();
        List<String> ls = new ArrayList<String>(handles);
        driver.switchTo().window(ls.get(index));
    }

    public void openMoreTabInChildWindow(String text){
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(text);
    }

    public void openNewTabInSameWindow(String text){
        String url= text;
        ((JavascriptExecutor) driver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.get(url);

    }

    public void switchToTabByIndex(int index){
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(index));
    }

    public void waitForElement(WebDriver driver, WebElement element) {
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(Exception.class);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void timeout(long timeout) {
        try {
            Thread.sleep(timeout);
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void scrollDown(){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,250)");
    }

    public void scrollDownToElement(WebElement element){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].scrollIntoView(true);",element);
    }

    public void scrollDownToElement(By locator){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].scrollIntoView(true);",locator);
    }

    public void scrollUp(){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,-250)");
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

    public void mouseHover(WebElement element){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    public void sendKeyToElement(WebElement element, String text){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(text);
    }

    public void sendKeyToElement(WebElement element,Keys keys){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(keys);
    }

    public void sendKeyToElement(By locator, String text){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.findElement(locator).sendKeys(text);
    }

    public void clearText(WebElement element){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
    }

    public void clearTextByBackSpace(WebElement element){
        FluentWait wait = new FluentWait(driver);
        wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
        wait.pollingEvery(Duration.ofMillis(300));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.visibilityOf(element));
        while(!element.getAttribute("value").equals("")){
            element.sendKeys(Keys.BACK_SPACE);
        }
    }

    public void uploadFile(WebElement element, String text){
        String filePath = System.getProperty("user.dir") + "/src/test/resources/" + text;
        driver.findElement(toByVal(element)).sendKeys(filePath);
    }

    public void uploadFile(By locator, String text){
        String filePath = System.getProperty("user.dir") + "/src/test/resources/" + text;
        driver.findElement(locator).sendKeys(filePath);
    }

    public void selectDropDownByTitle(WebElement element, String text){
        WebDriverWait wait = new WebDriverWait(driver, WaitTime.M);
        Select select = new Select(wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element)));
        select.selectByVisibleText(text);
    }

    public void takeScreenShotByElement(WebElement element, String ImageName, String format) throws IOException {
        Screenshot screenshot = new AShot().takeScreenshot(driver,element);
        ImageIO.write(screenshot.getImage(), format, new File(System.getProperty("user.dir")+ "/src/test/resources/" + ImageName));
    }

    public void selectFrameByTitle(String text, long timeout) {
        try {
            Thread.sleep(timeout);
            driver.switchTo().frame(text);
        } catch (Exception e) {
            System.out.println("Error: Cannot found expected iFrame: " + e.toString());
        }
    }

    public void selectFrameByIndex(int index, long timeout) {
        try {
            Thread.sleep(timeout);
            driver.switchTo().frame(index);
        } catch (Exception e) {
            System.out.println("Error: Cannot found expected iFrame: " + e.toString());
        }
    }

    public void refreshPage(){
        driver.navigate().refresh();
//        FluentWait wait = new FluentWait(driver);
//        wait.withTimeout(Duration.ofSeconds(WaitTime.S));
//        wait.pollingEvery(Duration.ofMillis(300));
////        WebDriverWait wait = new WebDriverWait(driver, 5 /*timeout in seconds*/);
//        if(wait.until(ExpectedConditions.alertIsPresent())==null){
//            System.out.println("alert was not present");
//        } else {
//            Alert alert = driver.switchTo().alert();
//            alert.accept();
//        }
        try {
            FluentWait wait = new FluentWait(driver);
            wait.withTimeout(Duration.ofSeconds(WaitTime.XL));
            wait.pollingEvery(Duration.ofMillis(300));
            wait.ignoring(NoSuchElementException.class);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
        System.out.println("alert was not present");
        }
    }

    public void goBack(){
        driver.navigate().back();
    }

    public boolean waitForElementDisplayed(WebElement element) {
        wait = WaitHelper.getWait(WaitTime.XL);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementDisplayed(WebElement element, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementDisplayed(By locator) {
        wait = WaitHelper.getWait(WaitTime.XL);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementDisplayed(By locator, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementIsNotDisplayed(WebElement element) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            wait.until(ExpectedConditions.invisibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementIsNotLocated(By locator) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementLocated(By locator) {
        wait = WaitHelper.getWait(WaitTime.XL);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementLocated(By locator, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementNotLocated(By locator, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementClickAble(By locator) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementClickAble(WebElement element) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementClickAble(By locator, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementClickAble(WebElement element, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not clickable: " + element.toString());
        }
    }

    public WebElement waitAndReturnElementIfLocated(By locator) {
        wait = WaitHelper.getWait(WaitTime.XL);
        try {
            return (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not located:" + locator.toString());
        }
    }

    public WebElement waitAndReturnElementIfClickAble(By locator) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            return (WebElement) wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not clickable:" + locator.toString());
        }
    }

    public WebElement waitAndReturnElementIfClickAble(By locator, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            return (WebElement) wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not clickable:" + locator.toString());
        }
    }

    public WebElement waitAndReturnElementIfClickAble(WebElement element) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            return (WebElement) wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not clickable:" + element.toString());
        }
    }

    public WebElement waitAndReturnElementIfClickAble(WebElement element, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            return (WebElement) wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not clickable:" + element.toString());
        }
    }

    public boolean isChildElementPresentInParent(WebElement child, WebElement parent) {
        wait = WaitHelper.getWait(WaitTime.M);
        try {
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, toByVal(child)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isChildElementPresentInParent(By child, WebElement parent) {
        wait = WaitHelper.getWait(WaitTime.M);
        try {
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, child));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isChildElementNotPresentInParent(By child, WebElement parent){
        wait = WaitHelper.getWait(WaitTime.M);
        try {
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, child));
            return false;
        } catch (TimeoutException e) {
            return true;
        }
    }

    public boolean checkImageDisplayOnScreen(String imageName) throws IOException {
        Screen screen = new Screen();
        Pattern pa1 = new Pattern(System.getProperty("user.dir")+ "/src/test/resources/" + imageName);
        Finder finder = new Finder(screen.capture().getFile());
        finder.find(pa1);
        if (finder.hasNext()){
            Match m = finder.next();
            System.out.println("Match Found with "+(m.getScore())*100+"%");
            finder.destroy();
            return true;
        } else {
            System.err.println("No Match Found");
            return false;
        }
    }

    public WebElement waitAndReturnElementIfDisplayed(WebElement element) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            return (WebElement) wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not displayed:" + element.toString());
        }
    }

    public WebElement waitAndReturnElementIfDisplayed(WebElement element, long timeout) {
        wait = WaitHelper.getWait(timeout);
        try {
            return (WebElement) wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not displayed:" + element.toString());
        }
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

    public List<WebElement> waitAndReturnElementListIfDisplayed(WebElement element) {
        wait = WaitHelper.getWait(WaitTime.L);
        try {
            List<WebElement> elementList =driver.findElements(toByVal(element));
            return  elementList;
        }
        catch (TimeoutException e) {
            throw new TimeoutException("Expected Element was not displayed:" + toByVal(element).toString());
        }
    }

    public By toByVal(WebElement we) {
        // By format = "[foundFrom] -> locator: term"
        // see RemoteWebElement toString() implementation
        String[] data = we.toString().split(" -> ")[1].replaceAll("]$", "").split(": ");
        String locator = data[0];
        String term = data[1];

        switch (locator) {
            case "xpath":
                return By.xpath(term);
            case "css selector":
                return By.cssSelector(term);
            case "id":
                return By.id(term);
            case "tag name":
                return By.tagName(term);
            case "name":
                return By.name(term);
            case "link text":
                return By.linkText(term);
            case "class name":
                return By.className(term);
        }
        return (By) we;
    }

    public int convertToInt(String number){
        int i=Integer.parseInt(number);
        return i;
    }

    public static String getFutureDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date futureDateTime = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("d MMM, YYYY, h:mm:00 a");
        String dateFormat = format1.format(futureDateTime);
        String[] key = dateFormat.toString().split(" ");
        String date = key[0];
        return date;
    }

    public static String getFutureDate(int index){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, index);
        Date futureDateTime = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("d MMM, YYYY, h:mm:00 a");
        String dateFormat = format1.format(futureDateTime);
        String[] key = dateFormat.toString().split(" ");
        String date = key[0];
        return date;
    }

    public static String getFutureDateTime(String format){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String dateFormat = format1.format(date);
        return dateFormat;
    }

    public static String getFutureDateTime( int day, String format){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String dateFormat = format1.format(date);
        return dateFormat;
    }

    public static String getCustomDateTime( int day, int minute, String format){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minute);
        cal.add(Calendar.DATE, day);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String dateFormat = format1.format(date);
        return dateFormat;
    }

    public static String getDateTime(String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        String dateFormat = formatter.format(date);
        return dateFormat;
    }


}
