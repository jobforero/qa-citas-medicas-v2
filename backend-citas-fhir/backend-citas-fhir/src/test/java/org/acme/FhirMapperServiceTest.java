package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.model.Cita;
import org.acme.service.FhirMapperService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class FhirMapperServiceTest {

    @Inject
    FhirMapperService fhirMapperService;

    @Test
    public void testConstruirAppointmentFHIRConTodosLosDatos() {
        Cita cita = new Cita();
        cita.pacienteCedula = "8-888-8888";
        cita.nombrePaciente = "Juan Perez";
        cita.tipoSeguro = "CSS";
        cita.numeroSeguro = "CSS-12345";
        cita.especialidad = "Medicina General";
        cita.tipoCita = "Consulta General";
        cita.modalidad = "PRESENCIAL";
        cita.fecha = LocalDateTime.parse("2026-08-15T10:00:00");

        Map<String, Object> fhirMap = fhirMapperService.construirAppointmentFHIR(cita);

        assertNotNull(fhirMap);
        assertEquals("Appointment", fhirMap.get("resourceType"));
        assertEquals("proposed", fhirMap.get("status"));
        assertNotNull(fhirMap.get("participant"));
    }

    @Test
    public void testConstruirAppointmentFHIRSinSeguro() {
        Cita cita = new Cita();
        cita.pacienteCedula = "8-999-9999";
        cita.nombrePaciente = "Maria Lopez";
        cita.especialidad = "Pediatría";

        Map<String, Object> fhirMap = fhirMapperService.construirAppointmentFHIR(cita);

        assertNotNull(fhirMap);
        assertEquals("Appointment", fhirMap.get("resourceType"));
    }
}