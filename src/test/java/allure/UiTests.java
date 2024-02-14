package pro.test;

import com.codeborne.selenide.Condition;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import pro.test.configuration.*;
import pro.test.ui.AllureUILogger;
import pro.test.ui.page.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@Epic("Test for test-framework ")
@Feature("UI tests")
@Owner("Osipov Semen")
@Tag("All")
@DisplayName("Первые UI тесты")
@ExtendWith({CredentialsParameter.class, AllureUILogger.class})
class UiTests {
    private static EnvironmentConfiguration configuration;
    private final Credentials credentials;

    UiTests(Credentials credentials) {
        this.credentials = credentials;
    }

    @BeforeAll
    static void before() throws IOException {
        configuration = new EnvironmentConfigurationProvider().get();
    }

    @Test
    @Link(type = "tms", name = "b9c6565c-3b25-40b1-a7b1-8f37bc31c1a5", value = "Тест из TMS")
    @Link(type = "manual", name = "1414895", value = "Ручной тест")
    @Tag("UI")
    @Story("Simple Login")
    @DisplayName("Проверка успешной авторизации")
    void testLogin() {
        LoginPage loginPage = Allure.step("Открыть страницу авторизации",
                () -> open(configuration.getHttpHostUrl("login"), LoginPage.class));
        User admin = credentials.getUserByRole("Admin");
        loginPage.login(admin);
        Allure.step("Проверить успешность авторизации", () -> {
            $(By.id("flash")).shouldHave(Condition.text("You logged into a secure area!"));
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @Tag("UI")
    @Story("Checkboxes")
    @DisplayName("Проверка работы Checkboxes")
    void testCheckboxes(int checkboxNumber) {
        Allure.step("Открыть страницу checkBoxes", () -> {
            open(configuration.getHttpHostUrl("checkboxes"));
            $x("//h3").shouldHave(text("Checkboxes"));
        });
        Allure.step("Проставить checkboxes в состояние false", () -> {
            $x(String.format("//*[@id='checkboxes']/input[%d]", checkboxNumber))
                    .setSelected(false)
                    .shouldNotBe(selected)
                    .shouldNotBe(checked);

        });
        Allure.step("Проставить checkboxes в состояние true", () -> {
            $x(String.format("//*[@id='checkboxes']/input[%d]", checkboxNumber))
                    .setSelected(true)
                    .shouldBe(selected)
                    .shouldBe(checked);

        });
    }

    @Test
    @Tag("UI")
    @Story("Dropdown List")
    @DisplayName("Проверка работы Dropdown")
    void testDropdown() {
        Allure.step("Открыть страницу dropdown", () -> {
            open(configuration.getHttpHostUrl("dropdown"));
            $x("//h3").shouldHave(text("Dropdown List"));
        });
        Allure.step("Заполнить dropdown элемент значением Option 1", () ->
                $(By.id("dropdown")).selectOption("Option 1"));
        Allure.step("Проверить, что текст соответствует Option 1", () ->
                $(By.id("dropdown")).shouldHave(text("Option 1")));
        Allure.step("Заполнить dropdown элемент значением Option 2", () ->
                $(By.id("dropdown")).selectOption("Option 2"));
        Allure.step("Проверить, что текст соответствует Option 2", () ->
                $(By.id("dropdown")).shouldHave(text("Option 2")));
    }

    @Test
    @Tag("UI")
    @Story("Drag and Drop")
    @DisplayName("Проверка работы DragAndDrop")
    void testDragAndDrop() {
        Allure.step("Открыть страницу dragAndDrop", () -> {
            open(configuration.getHttpHostUrl("drag_and_drop"));
            $x("//h3").shouldHave(text("Drag and Drop"));
        });
        Allure.step("Поменять местами сектора A и B", () -> {
            $("#column-a > header").shouldHave(text("A"));
            $("#column-b > header").shouldHave(text("B"));
            $("#column-a > header").dragAndDropTo($("#column-b > header"));
            $("#column-a > header").shouldHave(text("B"));
            $("#column-b > header").shouldHave(text("A"));
        });
    }

    @Test
    @Tag("UI")
    @Story("Context Menu")
    @DisplayName("Проверка работы с ContextMenu")
    void testContextMenu() {
        Allure.step("Открыть страницу ContextMenu", () -> {
            open(configuration.getHttpHostUrl("context_menu"));
            $x("//h3").shouldHave(text("Context Menu"));
        });
        Allure.step("Вызвать контекстное меню", () -> $(By.id("hot-spot")).contextClick());
        Allure.step("Проверить текст всплывающего окна", () -> Assertions.assertEquals(switchTo().alert().getText(), "You selected a context menu"));
        Allure.step("Закрыть всплывающее окно", () -> switchTo().alert().accept());
    }

    @Test
    @Tag("UI")
    @Story("Hidden Element")
    @DisplayName("Проверка работы со скрытыми элементами")
    void testHidden() {
        int waitingTime = 30;
        Allure.step("Открыть страницу HiddenDynamicallyElement", () -> {
            open(configuration.getHttpHostUrl("hidden_element"));
            $x("//h4").shouldHave(text("Example 1: Element on page that is hidden"));
        });
        Allure.step("Нажать на кнопку Старт", () -> {
            $("#start > button").click();
            $("#loading").shouldBe(visible);
        });
        Allure.step("После окончания загрузки, проверить наличие записи 'Hello World'", () -> {
            $("#finish > h4").shouldBe(visible, Duration.ofSeconds(waitingTime));
        });
    }

    @Test
    @Tag("UI")
    @Story("Rendered element")
    @DisplayName("Проверка работы c подгружаемыми элементами")
    void testRendered() {
        int waitingTime = 30;
        Allure.step("Открыть страницу RenderedDynamicallyElement", () -> {
            open(configuration.getHttpHostUrl("rendered_element"));
            $x("//h4").shouldHave(text("Example 2: Element rendered after the fact"));
        });
        Allure.step("Нажать на кнопку Старт", () -> {
            $("#start > button").click();
            $("#loading").shouldBe(visible);
        });
        Allure.step("После окончания загрузки, проверить наличие записи 'Hello World'", () -> {
            $("#finish > h4").shouldBe(exist, Duration.ofSeconds(waitingTime)).shouldBe(exist).shouldBe(visible);
        });
    }

    @Test
    @Tag("UI")
    @Story("Upload file")
    @DisplayName("Проверка работы c выгрузкой файлов")
    void testUpload() {
        String path = "src/test/resources/ui/";
        String fileName = "sample.txt";
        UploadPage uploadPage = page(UploadPage.class);
        Allure.step("Открыть страницу File Uploader", () -> {
            open(configuration.getHttpHostUrl("upload"));
            $x("//h3").shouldHave(text("File Uploader"));
        });
        uploadPage.uploadFile(path, fileName);
        Allure.step("Нажать на кнопку upload", () -> $("#file-submit").click());
        Allure.step("Проверить, что файл загружен успешно", () -> {
            $("h3").shouldHave(text("File Uploaded!"));
            Assertions.assertTrue($("#uploaded-files").getText().contains(fileName), "Имена файлов не совпадают");
        });
    }

    @Test
    @Tag("UI")
    @Story("Download file")
    @DisplayName("Проверка работы c загрузкой файлов")
    void testDownload() {
        String filename = "test-file.txt";
        String content = "Test file";
        DownloadPage downloadPage = page(DownloadPage.class);
        Allure.step("Открыть страницу File Downloader", () -> {
            open(configuration.getHttpHostUrl("download"));
            $x("//h3").shouldHave(text("File Downloader"));
        });
        File file = downloadPage.downloadFile(filename);
        Allure.step("Проверить содержимое скачанного файла", () -> {
            String fileContent = Files.readString(file.toPath());
            Assertions.assertEquals(fileContent, content, "Содержимое файла не совпадает с эталонным значением");
        });
    }
}

