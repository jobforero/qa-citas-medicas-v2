package org.acme;

import org.acme.dto.CitaRequestDTO;
import org.acme.dto.LoginRequestDTO;
import org.acme.dto.PacienteRegistroDTO;
import org.acme.dto.PacienteResponseDTO;
import org.acme.model.Cita;
import org.acme.model.Paciente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DtoAndModelTest {

    @Test
    public void testInstanciacionModelosYDTOs() {
        CitaRequestDTO citaDto = new CitaRequestDTO();
        citaDto.pacienteCedula = "8-123-456";
        citaDto.nombrePaciente = "Test User";

        LoginRequestDTO loginDto = new LoginRequestDTO();
        loginDto.cedula = "8-123-456";
        loginDto.password = "123456";

        PacienteRegistroDTO regDto = new PacienteRegistroDTO();
        regDto.cedula = "8-123-456";

        PacienteResponseDTO resDto = new PacienteResponseDTO();
        resDto.cedula = "8-123-456";

        Cita citaModel = new Cita();
        citaModel.pacienteCedula = "8-123-456";

        Paciente pacienteModel = new Paciente();
        pacienteModel.cedula = "8-123-456";

        assertNotNull(citaDto);
        assertNotNull(loginDto);
        assertNotNull(regDto);
        assertNotNull(resDto);
        assertNotNull(citaModel);
        assertNotNull(pacienteModel);
    }
}