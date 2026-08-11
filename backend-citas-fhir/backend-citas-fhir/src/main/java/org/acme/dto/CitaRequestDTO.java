package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class CitaRequestDTO {
    public String pacienteCedula;
    public String nombrePaciente;
    public String tipoSeguro;
    public String numeroSeguro;
    public String especialidad;
    public String tipoCita;
    public String modalidad;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime fecha;
}