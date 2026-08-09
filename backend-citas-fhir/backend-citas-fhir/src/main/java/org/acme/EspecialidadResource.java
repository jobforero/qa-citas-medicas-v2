package org.acme;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/api/especialidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EspecialidadResource {

    @GET
    public List<Map<String, Object>> listarEspecialidades() {
        return List.of(
                Map.of("id", "MED-GEN", "nombre", "Medicina General", "medicos", List.of("Dr. Carlos Gómez", "Dra. Ana Martínez")),
                Map.of("id", "PED", "nombre", "Pediatría", "medicos", List.of("Dr. Luis Rodríguez")),
                Map.of("id", "ODO", "nombre", "Odontología", "medicos", List.of("Dra. Elena Torres", "Dr. Roberto Silva")),
                Map.of("id", "CAR", "nombre", "Cardiología", "medicos", List.of("Dr. Javier Fernández"))
        );
    }
}