package util.wait;

import base.BasePage;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitHelper extends BasePage {

        public static WebDriverWait getWait(long timeOutInSeconds){
            return new WebDriverWait(driver, timeOutInSeconds);
        }
}
