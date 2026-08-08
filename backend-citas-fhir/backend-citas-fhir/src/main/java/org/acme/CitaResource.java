package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import org.acme.dto.CitaRequestDTO;
import org.acme.model.Cita;
import org.acme.service.FhirMapperService;
import org.jboss.logging.Logger;

@Path("/api/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaResource {

    private static final Logger LOG = Logger.getLogger(CitaResource.class);

    @Inject
    FhirMapperService fhirMapperService;

    @GET
    public List<Cita> listarTodas() {
        LOG.info("Consultando catálogo completo de citas en MongoDB");
        return Cita.listAll();
    }

    @POST
    public Response registrarCita(CitaRequestDTO dto) {
        LOG.infof("Registrando cita para paciente cédula: %s", dto.pacienteCedula);

        if (dto.pacienteCedula == null || dto.nombrePaciente == null) {
            LOG.warn("Incapaz de registrar cita: Cédula o nombre faltantes");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "La cédula y el nombre del paciente son obligatorios")).build();
        }

        Cita cita = new Cita();
        cita.pacienteCedula = dto.pacienteCedula;
        cita.nombrePaciente = dto.nombrePaciente;
        cita.tipoSeguro = dto.tipoSeguro;
        cita.numeroSeguro = dto.numeroSeguro;
        cita.especialidad = dto.especialidad;
        cita.tipoCita = dto.tipoCita;
        cita.modalidad = dto.modalidad;
        cita.fecha = dto.fecha;

        // Mapeo normalizado HL7 FHIR
        cita.recursoFHIR = fhirMapperService.construirAppointmentFHIR(cita);
        cita.persist();

        LOG.infof("Cita registrada y mapeada a HL7 FHIR con éxito para: %s", dto.nombrePaciente);
        return Response.status(Response.Status.CREATED).entity(cita).build();
    }
}