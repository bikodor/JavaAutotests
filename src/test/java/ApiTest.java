import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ApiTest {

    private int petId;

    @BeforeAll
    static void defaultSpec() {
        RestAssured.requestSpecification = given()
                .baseUri("https://petstore.swagger.io/v2")
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeEach
    void createPet() {
        petId = new Random().ints(0, 100000)
                .findFirst().orElse(0);
        String petName = "Varya";
        String petStatus = "available";
        String newPetJson = String.format("{\"id\": %d,\"name\":\"%s\",\"status\":\"%s\"}",
                petId, petName, petStatus);

       given()
                .when()
                .contentType(ContentType.JSON)
                .body(newPetJson)
                .post("/pet")
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    public void renamePetQueryParams() {
        String newPetName = "George";
        given()
                .when()
                .pathParam("petId", petId)
                .queryParam("name", newPetName)
                .contentType(ContentType.URLENC)
                .post("/pet/{petId}")
                .then()
                .statusCode(200);

        given()
                .when()
                .pathParam("petId", petId)
                .get("/pet/{petId}")
                .then()
                .statusCode(200)
                .assertThat().body("name", Matchers.equalTo(newPetName));

    }

    private final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze())
            .freeze();

    @Test
    public void setPetStatusByPut() {
        String newPetStatus = "sold";
        String petName = "Varya";
        String updatePetStatusJson = String.format("{\"id\": %d,\"name\":\"%s\",\"status\":\"%s\"}", petId, petName, newPetStatus);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(updatePetStatusJson)
                .put("/pet")
                .then()
                .statusCode(200)
                .assertThat().body("status", Matchers.equalTo(newPetStatus))
                .assertThat().body(matchesJsonSchemaInClasspath("pet/schema.json").using(jsonSchemaFactory));
    }

    @Test
    public void deletePet() {
        given()
                .when()
                .delete("/pet/{Id}", petId)
                .then()
                .statusCode(200);

        given()
                .when()
                .pathParam("petId", petId)
                .get("/pet/{petId}")
                .then()
                .statusCode(404)
                .assertThat().body("message", Matchers.equalTo("Pet not found"));

    }
}
