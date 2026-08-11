package org.acme;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.model.Cita;
import org.acme.repository.CitaRepository;
import org.acme.service.FhirMapperService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
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
    public void testListarTodasLasCitas() {
        Mockito.when(citaRepository.listAll()).thenReturn(List.of(new Cita()));

        given()
                .when().get("/api/citas")
                .then()
                .statusCode(200);
    }

    @Test
    public void testObtenerCitasPorPaciente() {
        Cita cita = new Cita();
        cita.pacienteCedula = "8-888-8888";
        Mockito.when(citaRepository.buscarPorPacienteCedula("8-888-8888")).thenReturn(List.of(cita));

        given()
                .when().get("/api/citas/paciente/8-888-8888")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

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

        Mockito.doNothing().when(citaRepository).persist(any(Cita.class));

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

    @Test
    public void testCancelarCitaExistente() {
        ObjectId mockId = new ObjectId();
        Cita cita = new Cita();
        cita.id = mockId;

        Map<String, Object> fhirMap = new HashMap<>();
        fhirMap.put("status", "booked");
        cita.recursoFHIR = new Document(fhirMap);

        Mockito.when(citaRepository.findById(mockId)).thenReturn(cita);
        Mockito.doNothing().when(citaRepository).update(any(Cita.class));

        given()
                .when().put("/api/citas/" + mockId.toHexString() + "/cancelar")
                .then()
                .statusCode(200)
                .body("mensaje", is("Cita cancelada con éxito"));
    }

    @Test
    public void testCancelarCitaNoEncontrada() {
        ObjectId mockId = new ObjectId();
        Mockito.when(citaRepository.findById(mockId)).thenReturn(null);

        given()
                .when().put("/api/citas/" + mockId.toHexString() + "/cancelar")
                .then()
                .statusCode(404)
                .body("error", is("Cita no encontrada"));
    }

    @Test
    public void testCancelarCitaIdInvalido() {
        given()
                .when().put("/api/citas/id-invalido-123/cancelar")
                .then()
                .statusCode(400)
                .body("error", is("Formato de ID inválido"));
    }
}