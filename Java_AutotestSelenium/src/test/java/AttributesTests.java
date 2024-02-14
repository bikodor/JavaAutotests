import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AttributesTests {
    @Test
    public void testGetText(){

        WebDriver driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.navigate().to("https://next.privat24.ua/?lang=en");

        WebElement buttonLogin = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-qa-node='login']")));

        String textContentButton = buttonLogin.getAttribute("textContent");
        System.out.println("textContentButton-> " + textContentButton);
        Assert.assertEquals("Sign in", textContentButton);

        driver.quit();


    }
    @Test
    public void testGetValue(){
        WebDriver driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.navigate().to("https://next.privat24.ua/?lang=en");

        WebElement inputFieldAmount = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[inputmode=\"decimal\"]")));

        String defaultValue = inputFieldAmount.getAttribute("placeholder");
        System.out.println("defaultValue-> " + defaultValue);
        Assert.assertEquals("200.00", defaultValue);

        driver.quit();
    }
    @Test
    public void testElementDisplay(){
        WebDriver driver = new ChromeDriver();
        WebElement element = driver.findElement((By.cssSelector("[inputmode=\"decimal\"]")));
        element.isDisplayed();
        element.isEnabled();
        element.isSelected();
    }

}
