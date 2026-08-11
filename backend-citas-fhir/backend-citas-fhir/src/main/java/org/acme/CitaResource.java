package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import org.acme.dto.CitaRequestDTO;
import org.acme.model.Cita;
import org.acme.repository.CitaRepository;
import org.acme.service.FhirMapperService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

@Path("/api/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaResource {

    private static final Logger LOG = Logger.getLogger(CitaResource.class);

    @Inject
    CitaRepository citaRepository;

    @Inject
    FhirMapperService fhirMapperService;

    @GET
    @Path("/todas")
    public Response listarTodas() {
        LOG.info("Consultando catálogo completo de citas en MongoDB");
        List<Cita> citas = citaRepository.listAll();
        return Response.ok(citas).build();
    }

    // CASO DE USO 1: Consultar historial por cédula
    @GET
    @Path("/paciente/{cedula}")
    public Response obtenerCitasPorPaciente(@PathParam("cedula") String cedula) {
        LOG.infof("Consultando historial de citas para paciente cédula: %s", cedula);
        List<Cita> citas = citaRepository.buscarPorPacienteCedula(cedula);
        return Response.ok(citas).build();
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

        // Mapeo normalizado HL7 FHIR convertido a Document BSON para MongoDB
        Map<String, Object> fhirMap = fhirMapperService.construirAppointmentFHIR(cita);
        cita.recursoFHIR = fhirMap != null ? new Document(fhirMap) : null;

        citaRepository.persist(cita);

        LOG.infof("Cita registrada y mapeada a HL7 FHIR con éxito para: %s", dto.nombrePaciente);
        return Response.status(Response.Status.CREATED).entity(cita).build();
    }

    // CASO DE USO 2: Cancelación de cita
    @PUT
    @Path("/{id}/cancelar")
    public Response cancelarCita(@PathParam("id") String id) {
        LOG.infof("Solicitud de cancelación para la cita con ID: %s", id);

        try {
            Cita cita = citaRepository.findById(new ObjectId(id));
            if (cita == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Cita no encontrada")).build();
            }

            // Actualizar directamente la propiedad 'status' en el Document BSON
            if (cita.recursoFHIR != null) {
                cita.recursoFHIR.put("status", "cancelled");
            }

            citaRepository.update(cita);

            LOG.infof("Cita ID %s cancelada correctamente", id);
            return Response.ok(Map.of("mensaje", "Cita cancelada con éxito", "citaId", id)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Formato de ID inválido")).build();
        }
    }
}