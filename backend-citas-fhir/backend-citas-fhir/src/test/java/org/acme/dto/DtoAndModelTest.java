package org.acme.dto;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.model.Cita;
import org.acme.model.Paciente;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class DtoAndModelTest {

    @Test
    public void testPacienteResponseDTOFromEntityConId() {
        Paciente paciente = new Paciente();
        paciente.id = new ObjectId(); // Cubre id != null
        paciente.cedula = "8-123-4567";
        paciente.nombre = "Juan Perez";
        paciente.correo = "juan@example.com";

        PacienteResponseDTO responseDTO = PacienteResponseDTO.fromEntity(paciente);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.id);
        assertEquals("8-123-4567", responseDTO.cedula);
        assertEquals("Juan Perez", responseDTO.nombre);
        assertEquals("juan@example.com", responseDTO.correo);
    }

    @Test
    public void testPacienteResponseDTOFromEntitySinId() {
        Paciente paciente = new Paciente();
        paciente.id = null; // Cubre id == null
        paciente.cedula = "8-000-0000";

        PacienteResponseDTO responseDTO = PacienteResponseDTO.fromEntity(paciente);

        assertNotNull(responseDTO);
        assertNull(responseDTO.id);
    }

    @Test
    public void testInstanciacionTodasLasDTOsYModelos() {
        assertNotNull(new CitaRequestDTO());
        assertNotNull(new LoginRequestDTO());
        assertNotNull(new PacienteRegistroDTO());
        assertNotNull(new PacienteResponseDTO());
        assertNotNull(new Cita());
    }
}