package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import org.acme.dto.LoginRequestDTO;
import org.acme.dto.PacienteRegistroDTO;
import org.acme.dto.PacienteResponseDTO;
import org.acme.service.PacienteService;

@Path("/api/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    @Inject
    PacienteService pacienteService;

    @POST
    @Path("/login")
    public Response login(LoginRequestDTO loginDTO) {
        try {
            PacienteResponseDTO response = pacienteService.autenticarPaciente(loginDTO);
            return Response.ok(response).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", e.getMessage())).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("/registro")
    public Response registrar(PacienteRegistroDTO registroDTO) {
        try {
            PacienteResponseDTO response = pacienteService.registrarPaciente(registroDTO);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", e.getMessage())).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}