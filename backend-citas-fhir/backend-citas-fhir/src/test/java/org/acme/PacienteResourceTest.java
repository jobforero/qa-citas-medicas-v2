package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PacienteResourceTest {

    @Test
    public void testFlujoCompletoPacienteHTTP() {
        String cedulaUnica = "8-REST-" + System.currentTimeMillis();

        // 1. Probar registro exitoso (HTTP 201)
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "cedula", cedulaUnica,
                        "nombre", "Usuario Cobertura",
                        "correo", "cobertura@correo.com",
                        "password", "123456"
                ))
                .when().post("/api/pacientes/registro")
                .then()
                .statusCode(201)
                .body("cedula", is(cedulaUnica));

        // 2. Probar registro duplicado (HTTP 409 Conflict)
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "cedula", cedulaUnica,
                        "nombre", "Usuario Cobertura",
                        "correo", "cobertura@correo.com",
                        "password", "123456"
                ))
                .when().post("/api/pacientes/registro")
                .then()
                .statusCode(409);

        // 3. Probar Login Exitoso (HTTP 200)
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("cedula", cedulaUnica, "password", "123456"))
                .when().post("/api/pacientes/login")
                .then()
                .statusCode(200)
                .body("cedula", is(cedulaUnica));

        // 4. Probar Login con Contraseña Incorrecta (HTTP 401 Unauthorized)
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("cedula", cedulaUnica, "password", "clave-erronea"))
                .when().post("/api/pacientes/login")
                .then()
                .statusCode(401);

        // 5. Probar Login con Cédula Inexistente (HTTP 401 Unauthorized)
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("cedula", "0-000-0000", "password", "123456"))
                .when().post("/api/pacientes/login")
                .then()
                .statusCode(401);
    }
}