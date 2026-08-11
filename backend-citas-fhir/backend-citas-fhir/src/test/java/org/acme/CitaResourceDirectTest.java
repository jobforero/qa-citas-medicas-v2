package org.acme;

import jakarta.ws.rs.core.Response;
import org.acme.dto.CitaRequestDTO;
import org.acme.model.Cita;
import org.acme.repository.CitaRepository;
import org.acme.service.FhirMapperService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class CitaResourceDirectTest {

    private CitaResource citaResource;
    private CitaRepository citaRepository;
    private FhirMapperService fhirMapperService;

    @BeforeEach
    public void setup() {
        citaResource = new CitaResource();
        citaRepository = Mockito.mock(CitaRepository.class);
        fhirMapperService = Mockito.mock(FhirMapperService.class);

        citaResource.citaRepository = citaRepository;
        citaResource.fhirMapperService = fhirMapperService;
    }

    // Prueba la consulta completa del catalogo de citas registradas
    @Test
    @SuppressWarnings("unchecked")
    public void testListarTodasDirecto() {
        Mockito.when(citaRepository.listAll()).thenReturn(List.of(new Cita()));

        Response response = citaResource.listarTodas();

        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());

        List<Cita> resultado = (List<Cita>) response.getEntity();
        assertEquals(1, resultado.size());
    }

    // Prueba la obtencion del historial de citas filtrado por la cedula del paciente
    @Test
    public void testObtenerCitasPorPacienteDirecto() {
        Cita cita = new Cita();
        cita.pacienteCedula = "8-888-8888";
        Mockito.when(citaRepository.buscarPorPacienteCedula("8-888-8888")).thenReturn(List.of(cita));

        Response response = citaResource.obtenerCitasPorPaciente("8-888-8888");
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
    }

    // Prueba el registro exitoso de una cita y la generacion del recurso HL7 FHIR
    @Test
    public void testRegistrarCitaExitosoDirecto() {
        CitaRequestDTO dto = new CitaRequestDTO();
        dto.pacienteCedula = "8-888-8888";
        dto.nombrePaciente = "Juan Perez";
        dto.especialidad = "Medicina General";

        Mockito.when(fhirMapperService.construirAppointmentFHIR(any()))
                .thenReturn(new HashMap<>(Map.of("resourceType", "Appointment", "status", "proposed")));

        Response response = citaResource.registrarCita(dto);
        assertEquals(201, response.getStatus());
        assertNotNull(response.getEntity());
    }

    // Prueba la validacion de error HTTP 400 cuando faltan la cedula o el nombre del paciente
    @Test
    public void testRegistrarCitaErrorDirecto() {
        CitaRequestDTO dto = new CitaRequestDTO(); // Sin pacienteCedula ni nombrePaciente

        Response response = citaResource.registrarCita(dto);
        assertEquals(400, response.getStatus());
    }

    // Prueba la cancelacion exitosa de una cita y la actualizacion de su estado FHIR a 'cancelled'
    @Test
    public void testCancelarCitaExitosaDirecto() {
        ObjectId id = new ObjectId();
        Cita cita = new Cita();
        cita.id = id;
        Map<String, Object> fhirMap = new HashMap<>();
        fhirMap.put("status", "proposed");
        cita.recursoFHIR = new Document(fhirMap);

        Mockito.when(citaRepository.findById(id)).thenReturn(cita);

        Response response = citaResource.cancelarCita(id.toHexString());
        assertEquals(200, response.getStatus());
        assertEquals("cancelled", cita.recursoFHIR.get("status"));
    }

    // Prueba la respuesta HTTP 404 cuando se intenta cancelar un ID de cita que no existe en MongoDB
    @Test
    public void testCancelarCitaNoEncontradaDirecto() {
        ObjectId id = new ObjectId();
        Mockito.when(citaRepository.findById(id)).thenReturn(null);

        Response response = citaResource.cancelarCita(id.toHexString());
        assertEquals(404, response.getStatus());
    }

    // Prueba la respuesta HTTP 400 cuando el formato del ObjectId enviado en la URL es invalido
    @Test
    public void testCancelarCitaIdInvalidoDirecto() {
        Response response = citaResource.cancelarCita("id-invalido");
        assertEquals(400, response.getStatus());
    }
}