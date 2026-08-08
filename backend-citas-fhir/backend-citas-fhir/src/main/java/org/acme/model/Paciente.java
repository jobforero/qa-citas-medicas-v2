package org.acme.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "pacientes")
public class Paciente extends PanacheMongoEntity {
    public String cedula;
    public String nombre;
    public String correo;
    public String password; // Se almacena Hash Bcrypt

    public static Paciente findByCedula(String cedula) {
        return find("cedula", cedula).firstResult();
    }
}