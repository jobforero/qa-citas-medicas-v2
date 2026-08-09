package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class EspecialidadResourceTest {

    @Test
    public void testListarEspecialidades() {
        given()
                .when().get("/api/especialidades")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("nombre", hasItems("Medicina General", "Pediatría", "Odontología"));
    }
}