package org.acme;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.repository.CitaRepository;
import org.acme.service.FhirMapperService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class CitaResourceTest {

    @InjectMock
    CitaRepository citaRepository;

    @InjectMock
    FhirMapperService fhirMapperService;

    @Test
    public void testRegistrarCitaSinCedulaOError() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("especialidad", "Medicina General"))
                .when().post("/api/citas")
                .then()
                .statusCode(400)
                .body("error", is("La cédula y el nombre del paciente son obligatorios"));
    }

    @Test
    public void testRegistrarCitaExitosa() {
        Mockito.when(fhirMapperService.construirAppointmentFHIR(any()))
                .thenReturn(Map.of("resourceType", "Appointment", "status", "booked"));

        // Evita interactuar con MongoDB simulando la persistencia
        Mockito.doNothing().when(citaRepository).persist(any(org.acme.model.Cita.class));

        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "pacienteCedula", "8-888-8888",
                        "nombrePaciente", "Juan Perez",
                        "especialidad", "Medicina General",
                        "tipoCita", "Consulta General",
                        "modalidad", "PRESENCIAL"
                ))
                .when().post("/api/citas")
                .then()
                .statusCode(201)
                .body("pacienteCedula", is("8-888-8888"))
                .body("nombrePaciente", is("Juan Perez"));
    }
}