import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


@Tag("All")
@DisplayName("Первые UI тесты")
class UiTests {
    static String baseUrl = "http://the-internet.herokuapp.com";

    @Test
    @Tag("UI")
    @DisplayName("Проверка успешной авторизации")
    void testLogin() {
        String username = "tomsmith";
        String password = "SuperSecretPassword";
        open(baseUrl + "/login");
        $("#username").setValue(username);
        $("#password").setValue(password);
        $(By.tagName("button")).click();
        $(By.id("flash")).shouldHave(text("You logged into a secure area!"));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @Tag("UI")
    @DisplayName("Проверка работы Checkboxes")
    void testCheckboxes(int checkboxNumber) {
        open(baseUrl + "/checkboxes");
        $x("//h3").shouldHave(text("Checkboxes"));
        $x(String.format("//*[@id='checkboxes']/input[%d]", checkboxNumber))
                .setSelected(false)
                .shouldNotBe(checked);
        $x(String.format("//*[@id='checkboxes']/input[%d]", checkboxNumber))
                .setSelected(true)
                .shouldBe(checked);
    }

    @Test
    @Tag("UI")
    @DisplayName("Проверка работы Dropdown")
    void testDropdown() {
        open(baseUrl + "/dropdown");
        $x("//h3").shouldHave(text("Dropdown List"));
        $(By.id("dropdown")).selectOption("Option 1");
        $(By.id("dropdown")).shouldHave(text("Option 1"));
        $(By.id("dropdown")).selectOption("Option 2");
        $(By.id("dropdown")).shouldHave(text("Option 2"));
    }

    @Test
    @Tag("UI")
    @DisplayName("Проверка работы DragAndDrop")
    void testDragAndDrop() {
        open(baseUrl + "/drag_and_drop");
        $x("//h3").shouldHave(text("Drag and Drop"));
        $("#column-a header").shouldHave(text("A"));
        $("#column-b header").shouldHave(text("B"));
        $("#column-a header").dragAndDrop(DragAndDropOptions.to($("#column-b header")));
        $("#column-a header").shouldHave(text("B"));
        $("#column-b header").shouldHave(text("A"));
    }

    @Test
    @Tag("UI")
    @DisplayName("Проверка работы с ContextMenu")
    void testContextMenu()  {
        open(baseUrl + "/context_menu");
        SelenideElement header = $x("//h3");
        header.shouldHave(text("Context Menu"));
        $(By.id("hot-spot")).contextClick();
        Assertions.assertEquals(switchTo().alert().getText(), "You selected a context menu");
        switchTo().alert().accept();
    }


    @Test
    @Tag("UI")
    @DisplayName("Проверка работы c подгружаемыми элементами")
    void testRendered() {
        int waitingTime = 30;
        open(baseUrl + "/dynamic_loading/2");
        $x("//h4").shouldHave(text("Example 2: Element rendered after the fact"));
        $("#start button").click();
        $("#loading").shouldBe(visible);
        $("#finish h4").shouldBe(exist, Duration.ofSeconds(waitingTime)).shouldBe(visible);
    }

    @Test
    @Tag("UI")
    @DisplayName("Проверка работы c выгрузкой файлов")
    void testUpload() {
        String path = "src/test/resources/ui/";
        String fileName = "sample.txt";
        open(baseUrl + "/upload");
        $x("//h3").shouldHave(text("File Uploader"));
        $("#file-upload").uploadFile(new File(path, fileName));
        $("#file-submit").click();
        $("h3").shouldHave(text("File Uploaded!"));
        Assertions.assertTrue($("#uploaded-files").getText().contains(fileName), "Имена файлов не совпадают");
    }

    @Test
    @Tag("UI")
    @DisplayName("Проверка работы c загрузкой файлов")
    void testDownload() throws IOException {
        String filename = "sample.txt";
        String content = "123";
        open(baseUrl + "/download");
        $x("//h3").shouldHave(text("File Downloader"));
        File file = downloadFile(filename);
        String fileContent = Files.readString(file.toPath());
        Assertions.assertEquals(fileContent, content, "Содержимое файла не совпадает с эталонным значением");
    }

    @AfterEach
    public void after(){
        Selenide.closeWebDriver();
    }

    private File downloadFile(String fileName) {
        try {
            return $x(String.format("//a[text()='%s']", fileName)).download();
        } catch (FileNotFoundException e) {
            throw new AssertionError("Возникла ошибка при скачивании файла", e);
        }
    }
}

