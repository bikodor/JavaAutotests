package pro.test;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import pro.test.configuration.EnvironmentConfiguration;
import pro.test.configuration.EnvironmentConfigurationParameter;
import pro.test.http.HttpBaseSpecification;

import java.util.Random;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("Test for test-framework")
@Feature("Http tests")
@Tag("First")
@Tag("All")
@Owner("Osipov Semen")
@ExtendWith({HttpBaseSpecification.class,
        EnvironmentConfigurationParameter.class})
@DisplayName("Проверка petstore API")
class ApiTests {
    private final EnvironmentConfiguration configuration;
    private int petId;
    private final String petStatus = "available";
    private final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze())
            .freeze();

    public ApiTests(EnvironmentConfiguration configuration) {
        this.configuration = configuration;
    }

    @BeforeEach
    void createPet() {
        petId = new Random().ints(0, 100000).findFirst().orElse(0);
        String petName = "Varya";
        String newPetJson = String.format("{\"id\": %d,\"name\":\"%s\",\"status\":\"%s\"}", petId, petName, petStatus);
        Response newPetResponse = Allure.step("Послать запрос на создание нового питомца", () -> given()
                .baseUri(configuration.getHttpHostUrl("pet"))
                .header("Content-type", "application/json")
                .body(newPetJson)
                .post());
        Allure.step("Проверить статус ответа", () -> newPetResponse
                .then()
                .statusCode(200));
    }

    @Test
    @Tag("Api")
    @Story("Test Pets By Active Status")
    @DisplayName("Проверка findByStatus со статусом available")
    void testPetsByActiveStatusWithSchema() {
        Response response = Allure.step("Отправить запрос", () -> given()
                .baseUri(configuration.getHttpHostUrl("pets_by_status"))
                .param("status", "available")
                .get());
        Allure.step("Проверить статус ответа", () -> response
                .then()
                .statusCode(200));
        Allure.step("Проверить по json схеме", () -> response
                .then()
                .body(matchesJsonSchemaInClasspath("pet/schema.json").using(jsonSchemaFactory)));
    }

    @Test
    @Tag("Api")
    @Story("Test update pet")
    @DisplayName("Проверка обновления данных о питомце")
    void testUpdatePets() {
        String newPetName = "Volt";
        String updatePetNameJson = String.format("{\"id\": %d,\"name\":\"%s\",\"status\":\"%s\"}", petId, newPetName, petStatus);
        Response updatePetNameResponse = Allure.step("Послать запрос на изменение имени", () -> given()
                .header("Content-type", "application/json")
                .baseUri(configuration.getHttpHostUrl("pet"))
                .body(updatePetNameJson)
                .put());
        Allure.step("Проверить статус ответа", () -> updatePetNameResponse
                .then()
                .statusCode(200));
        Allure.step("Проверить, что имя изменено",()->{
        Response searchUpdatedPetResponse = Allure.step("Послать запрос на получение питомца по id", () -> given()
                .pathParam("petId",petId)
                .get(configuration.getHttpHostUrl("petId")));
        Allure.step("Проверить статус ответа", () -> searchUpdatedPetResponse
                .then()
                .statusCode(200));
        Allure.step("Проверить имя в теле ответа", () -> { searchUpdatedPetResponse
                .then()
                .body("name", Matchers.equalTo(newPetName));
        });});
    }

    @Test
    @Tag("Api")
    @Story("Test delete pet")
    @DisplayName("Проверка удаления питомца")
    void testDeletePets() {
        Response deletePet = Allure.step("Послать запрос на удаление питомца", () -> given()
                .pathParam("petId",petId)
                .delete(configuration.getHttpHostUrl("petId")));
        Allure.step("Проверить статус ответа", () -> deletePet
                .then()
                .statusCode(200));
        Allure.step("Проверить, что питомец не найден",()->{
        Response searchPetResponse = Allure.step("Послать запрос на получение питомца по id", () -> given()
                .baseUri(configuration.getHttpHostUrl("pet") + "/" + petId)
                .get());
        Allure.step("Проверить статус ответа", () -> searchPetResponse
                .then()
                .statusCode(404));
        Allure.step("Проверить сообщение, что питомец не найден", () -> {searchPetResponse
                .then()
                .body("message", Matchers.equalTo("Pet not found"));
        });});
    }
}
