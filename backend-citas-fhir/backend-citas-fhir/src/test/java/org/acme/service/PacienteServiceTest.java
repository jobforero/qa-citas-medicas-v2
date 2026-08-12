package org.acme.service;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.dto.LoginRequestDTO;
import org.acme.dto.PacienteRegistroDTO;
import org.acme.dto.PacienteResponseDTO;
import org.acme.model.Paciente;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PacienteServiceTest {

    @Test
    public void testCoversPacienteServiceAndDTOs() {
        PacienteService service = new PacienteService();

        // 1. Probar validaciones de registro e inicio de sesión
        assertThrows(IllegalArgumentException.class, () -> service.registrarPaciente(new PacienteRegistroDTO()));
        assertThrows(IllegalArgumentException.class, () -> service.autenticarPaciente(new LoginRequestDTO()));

        // 2. Probar PacienteResponseDTO.fromEntity (con id y sin id)
        Paciente p1 = new Paciente();
        p1.id = new ObjectId();
        p1.cedula = "8-123-4567";
        p1.nombre = "Prueba";
        p1.correo = "test@correo.com";

        PacienteResponseDTO dto1 = PacienteResponseDTO.fromEntity(p1);
        assertNotNull(dto1);
        assertNotNull(dto1.id);
        assertEquals("8-123-4567", dto1.cedula);

        p1.id = null;
        PacienteResponseDTO dto2 = PacienteResponseDTO.fromEntity(p1);
        assertNull(dto2.id);
    }
}