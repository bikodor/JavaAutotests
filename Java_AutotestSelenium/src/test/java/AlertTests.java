import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AlertTests {
    @Test
    public void testAlert(){
        WebDriver driver = new ChromeDriver();

        driver.get("http://google.com");

        //Alert alert = driver.switchTo().alert();
        //alert.accept();
        //alert.dismiss();
        //alert.getText();
        //alert.sendKeys("some text");

    }
    @Test
    public void testFrames(){

//        driver.switchTo().frame(0);
//        driver.switchTo().frame("frameName");
//        driver.switchTo().frame(webElement);
//

//        driver.switchTo().frame("main_frame")
//                .switchTo().frame(0)
//                .switchTo().frame("sub_frame");
//
//        driver.switchTo()
//                .frame("main_frame.0.sub_frame");
//
//        driver.switchTo().defaultContent();
//
//        wait.until(ExpectedConditions
//                .frameToBeAvailableAndSwitchToIt(locatorOfFrame));


    }
}
