package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/api/especialidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EspecialidadResource {

    @OPTIONS
    @Path("{path: .*}")
    public Response options() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "https://frontend-citas-fhir.vercel.app")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .build();
    }

    @GET
    public Response obtenerEspecialidades() {
        List<Map<String, Object>> especialidades = List.of(
                Map.of("id", "MED-GEN", "nombre", "Medicina General", "medicos", List.of("Dr. Carlos Gómez", "Dra. Ana Martínez")),
                Map.of("id", "PED", "nombre", "Pediatría", "medicos", List.of("Dr. Luis Rodríguez")),
                Map.of("id", "ODO", "nombre", "Odontología", "medicos", List.of("Dra. Elena Torres", "Dr. Roberto Silva")),
                Map.of("id", "CAR", "nombre", "Cardiología", "medicos", List.of("Dr. Javier Fernández"))
        );

        return Response.ok(especialidades)
                .header("Access-Control-Allow-Origin", "https://frontend-citas-fhir.vercel.app")
                .build();
    }
}