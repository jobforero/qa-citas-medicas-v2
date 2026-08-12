package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class CitaResourceTest {

    // 1. Probar GET /api/especialidades y su método OPTIONS
    @Test
    public void testEspecialidadesEndpoints() {
        // Prueba de lectura de especialidades
        given()
                .when().get("/api/especialidades")
                .then()
                .statusCode(200)
                .body("size()", is(4));

        // Prueba del preflight OPTIONS para subir cobertura de EspecialidadResource
        given()
                .when().options("/api/especialidades")
                .then()
                .statusCode(200)
                .header("Access-Control-Allow-Origin", "https://frontend-citas-fhir.vercel.app");
    }

    // 2. Probar GET /api/citas y su método OPTIONS
    @Test
    public void testCitasGeneralAndOptions() {
        given()
                .when().get("/api/citas")
                .then()
                .statusCode(200);

        given()
                .when().options("/api/citas")
                .then()
                .statusCode(200)
                .header("Access-Control-Allow-Origin", "https://frontend-citas-fhir.vercel.app");
    }

    // 3. Probar GET /api/citas/paciente/{cedula} (Cubre CitaRepository.buscarPorPacienteCedula)
    @Test
    public void testObtenerCitasPorPaciente() {
        given()
                .pathParam("cedula", "8-888-8888")
                .when().get("/api/citas/paciente/{cedula}")
                .then()
                .statusCode(200);
    }

    // 4. Probar POST /api/citas exitoso y la rama BAD_REQUEST (campos nulos)
    @Test
    public void testRegistrarCitaExitoYError() {
        // Caso de error (sin cédula) -> Cubre las condiciones y líneas de BAD_REQUEST
        Map<String, Object> payloadInvalido = new HashMap<>();
        payloadInvalido.put("especialidad", "Medicina General");

        given()
                .contentType(ContentType.JSON)
                .body(payloadInvalido)
                .when().post("/api/citas")
                .then()
                .statusCode(400)
                .body("error", is("La cédula y el nombre del paciente son obligatorios"));

        // Caso de éxito
        Map<String, Object> payloadValido = new HashMap<>();
        payloadValido.put("pacienteCedula", "8-999-9999");
        payloadValido.put("nombrePaciente", "Test Automation User");
        payloadValido.put("tipoSeguro", "CSS");
        payloadValido.put("numeroSeguro", "CSS-9999");
        payloadValido.put("especialidad", "Medicina General");
        payloadValido.put("tipoCita", "Consulta General");
        payloadValido.put("modalidad", "PRESENCIAL");
        payloadValido.put("fecha", "2026-09-10T10:00:00");

        given()
                .contentType(ContentType.JSON)
                .body(payloadValido)
                .when().post("/api/citas")
                .then()
                .statusCode(201)
                .body("pacienteCedula", is("8-999-9999"));
    }

    // 5. Probar PUT /api/citas/{id}/cancelar en caso NOT_FOUND e ID inválido
    @Test
    public void testCancelarCitaErrores() {
        // ID no existente en la base de datos
        given()
                .pathParam("id", "60d5ec49f1b2c82d8c8e4b99")
                .when().put("/api/citas/{id}/cancelar")
                .then()
                .statusCode(404)
                .body("error", is("Cita no encontrada"));

        // ID con formato inválido (excepción)
        given()
                .pathParam("id", "123-invalido")
                .when().put("/api/citas/{id}/cancelar")
                .then()
                .statusCode(400)
                .body("error", is("Formato de ID inválido"));
    }
}