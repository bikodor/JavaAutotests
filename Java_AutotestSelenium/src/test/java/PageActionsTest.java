import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PageActionsTest {
    @Test
    public void navigate(){
        WebDriver driver = new ChromeDriver();

        driver.get("http://google.com");

        driver.navigate().to("https://selenium.dev");
        driver.navigate().back();
        driver.navigate().forward();
        driver.navigate().refresh();


        driver.quit();
    }

}
