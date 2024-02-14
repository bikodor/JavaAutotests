import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Set;

public class GetCookieStart {
    @Test
    public void cookieTest(){

        WebDriver driver = new ChromeDriver();

        driver.get("http://google.com");
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("ChromeDriver");
        searchBox.submit();

        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println("cookies->" + cookies);

//        Cookie authCookie = new Cookie("", "");
//        driver.manage().addCookie(authCookie);
//        driver.navigate().refresh();

//        driver.manage().getCookieNamed("user_name");
//        driver.manage().deleteCookieNamed("user_name");

        driver.manage().deleteAllCookies();
        System.out.println("deleteAllCookies->" + driver.manage().getCookies());

    }

}
