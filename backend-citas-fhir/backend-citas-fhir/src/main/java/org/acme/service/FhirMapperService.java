package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.acme.model.Cita;

@ApplicationScoped
public class FhirMapperService {

    public Map<String, Object> construirAppointmentFHIR(Cita cita) {
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("resourceType", "Appointment");
        appointment.put("status", "proposed");
        appointment.put("start", cita.fecha != null ? cita.fecha.toString() : null);

        Map<String, Object> pacientePart = Map.of(
                "actor", Map.of("reference", "Patient/" + cita.pacienteCedula),
                "status", "accepted"
        );
        Map<String, Object> medicoPart = Map.of(
                "actor", Map.of("reference", "Practitioner/" + (cita.especialidad != null ? cita.especialidad : "GENERAL")),
                "status", "accepted"
        );

        appointment.put("participant", List.of(pacientePart, medicoPart));
        return appointment;
    }
}