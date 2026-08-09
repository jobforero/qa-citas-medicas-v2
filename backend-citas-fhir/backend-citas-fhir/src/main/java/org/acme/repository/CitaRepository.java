package org.acme.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Cita;
import java.util.List;

@ApplicationScoped
public class CitaRepository implements PanacheMongoRepository<Cita> {

    public List<Cita> buscarPorPacienteCedula(String pacienteCedula) {
        return list("pacienteCedula", pacienteCedula);
    }
}