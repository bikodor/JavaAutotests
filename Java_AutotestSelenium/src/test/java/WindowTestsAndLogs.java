import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.util.Set;

public class WindowTestsAndLogs {
    @Test
    public void testWindow(){

        WebDriver driver = new ChromeDriver();

        driver.get("http://google.com");

        WebElement searchBox = driver.findElement(By.name("q"));

        searchBox.sendKeys("ChromeDriver");

        searchBox.submit();

        driver.manage().window().setSize(new Dimension(1000, 1000));
        driver.manage().window().maximize();
        driver.manage().window().fullscreen();
        System.out.println("getSize->" + driver.manage().window().getSize());
        System.out.println("getPosition->" + driver.manage().window().getPosition());
        driver.manage().window().setPosition(new Point(50, 100));

        driver.quit();

    }
    @Test
    public void testLogs(){
        WebDriver driver = new ChromeDriver();

        driver.get("http://google.com");

        Set<String> allLogsType = driver.manage().logs().getAvailableLogTypes();
        System.out.println("allLogsType-> " + allLogsType);

        LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
        for (
                LogEntry entry: logs
        )
            System.out.println("logs->" + entry);

    }

}
