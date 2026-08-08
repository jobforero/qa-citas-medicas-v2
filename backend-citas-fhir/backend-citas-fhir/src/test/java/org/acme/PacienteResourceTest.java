package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PacienteResourceTest {

    @Test
    public void testLoginInvalido() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("cedula", "00-000-0000", "password", "claveInexistente"))
                .when().post("/api/pacientes/login")
                .then()
                .statusCode(401);
    }
}