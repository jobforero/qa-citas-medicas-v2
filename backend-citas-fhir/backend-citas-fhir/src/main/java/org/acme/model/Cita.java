package org.acme.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.Map;

@MongoEntity(collection = "citas")
public class Cita {
    public ObjectId id;
    public String pacienteCedula;
    public String nombrePaciente;
    public String tipoSeguro;
    public String numeroSeguro;
    public String especialidad;
    public String tipoCita;
    public String modalidad;
    public LocalDateTime fecha;
    public Map<String, Object> recursoFHIR; // Mantener Map<String, Object>
}