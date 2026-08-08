package org.acme.dto;

public class PacienteResponseDTO {
    public String id;
    public String cedula;
    public String nombre;
    public String correo;

    public static PacienteResponseDTO fromEntity(org.acme.model.Paciente paciente) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.id = paciente.id != null ? paciente.id.toString() : null;
        dto.cedula = paciente.cedula;
        dto.nombre = paciente.nombre;
        dto.correo = paciente.correo;
        return dto;
    }
}