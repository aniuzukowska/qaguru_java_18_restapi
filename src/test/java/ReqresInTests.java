import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class ReqresInTests {
    @Test
    void successfulSingleUserTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.first_name", is("Janet"))
                .body(matchesJsonSchemaInClasspath("schemes/singleUserGetResponseScheme.json"));
    }

    @Test
    void unSuccessfulSingleUserTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/абв")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    void successfulUpdateUserTest() {
        String requestBody = "{     \"name\": \"morpheus\",     \"job\": \"zion resident\" }";
        given()
                .log().uri()
                .body(requestBody)
                .contentType(JSON)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body(matchesJsonSchemaInClasspath("schemes/updateUserPutResponseScheme.json"));
    }

    @Test
    void successfulLoginTest() {
        String requestBody = "{     \"email\": \"eve.holt@reqres.in\",     \"password\": \"cityslicka\" }";
        given()
                .log().uri()
                .body(requestBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/successfulLoginResponseScheme.json"));
    }

    @Test
    void unSuccessfulLoginTest() {
        String requestBody = "{     \"email\": \"eve.holt@reqres.in\",     \"password\": \"\" }";
        given()
                .log().uri()
                .body(requestBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"))
                .body(matchesJsonSchemaInClasspath("schemes/unSuccessfulLoginResponseScheme.json"));
    }


}
