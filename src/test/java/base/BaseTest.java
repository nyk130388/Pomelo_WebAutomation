package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.Random;

@SuppressWarnings("ALL")
public class BaseTest extends BasePage {

    public BaseTest(){
        super();
    }

    @BeforeMethod
    public void setUp() {
        initialization();
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

    public static String getRandomKey() {
        Random rand = new Random();
        String numbers = "0123456789";
        String key = "";
        for(int i=0; i<8; i++) {
            key += numbers.charAt(rand.nextInt(numbers.length()));
        }
        return key;
    }

    public static String getRandomKey(int length) {
        Random rand = new Random();
        String numbers = "0123456789";
        String key = "";
        for(int i=0; i<length; i++) {
            key += numbers.charAt(rand.nextInt(numbers.length()));
        }
        return key;
    }

}

