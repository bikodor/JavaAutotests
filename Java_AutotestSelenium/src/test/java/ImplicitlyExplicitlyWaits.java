import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ImplicitlyExplicitlyWaits {
    @Test
    public void testImplicitlyWait(){
        WebDriver driver = new ChromeDriver();

        driver.get("http://google.com");

        WebElement element = driver.findElement(By.name("q"));
        Assert.assertTrue(element.isDisplayed());
//        Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("///"))));

//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement someElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));


//        WebElement someElement = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));


        }
    @Test
    public void testDissapearedElement(){
        WebDriver driver = new ChromeDriver();

        driver.get("http://google.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement someElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));

        someElement.sendKeys("ChromeDriver");
        someElement.submit();
//        wait.until(ExpectedConditions.stalenessOf(someElement));
        Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(someElement)));
    }

    @Test
    public void otherTimeouts(){
        // wait.until(ExpectedConditions.titleIs("You live Online-Bank"));
        // wait.until(ExpectedConditions.urlContains("money-transfer/card"));
        // wait.until(ExpectedConditions.alertIsPresent());
        // wait.until(ExpectedConditions.numberOfWindowToBe(1));

        // wait.until(ExpectedConditions.attributeContains(By.xpath("///"), "class", "visible"));
        // wait.until(ExpectedConditions.textToBePresentInElement(buttonRefresh, "Refresh"));
        // wait.until(ExpectedConditions.elementToBeClickable(buttonRefresh));


    }

}
