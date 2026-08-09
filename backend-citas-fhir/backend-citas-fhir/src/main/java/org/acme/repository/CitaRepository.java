package org.acme.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Cita;

@ApplicationScoped
public class CitaRepository implements PanacheMongoRepository<Cita> {
}