package org.acme.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.LocalDateTime;

@MongoEntity(collection = "citas")
public class Cita extends PanacheMongoEntity {
    public String pacienteCedula;
    public String nombrePaciente;
    public String tipoSeguro;
    public String numeroSeguro;
    public String especialidad;
    public String tipoCita;
    public String modalidad;
    public LocalDateTime fecha;
    public Object recursoFHIR; // Guarda la estructura mapeada a HL7 FHIR
}