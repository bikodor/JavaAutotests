import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class VisibleElements {
    @Test
    public void visibleElement(){
        WebDriver driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/hidden");

        driver.switchTo().frame("frame_examples");


        WebElement buttonOk = driver.findElement(By.id("okButton"));

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].scrollIntoView()", buttonOk);


        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", buttonOk);



        WebElement textWelcome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='awesome']/h1")));

        Assert.assertEquals("Thanks!", textWelcome.getText());

    }
}
