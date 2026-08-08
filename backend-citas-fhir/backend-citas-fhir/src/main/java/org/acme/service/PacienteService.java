package org.acme.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.LoginRequestDTO;
import org.acme.dto.PacienteRegistroDTO;
import org.acme.dto.PacienteResponseDTO;
import org.acme.model.Paciente;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PacienteService {

    private static final Logger LOG = Logger.getLogger(PacienteService.class);

    public PacienteResponseDTO registrarPaciente(PacienteRegistroDTO dto) {
        LOG.infof("Procesando registro de paciente con cédula: %s", dto.cedula);

        if (dto.cedula == null || dto.nombre == null || dto.correo == null || dto.password == null) {
            LOG.warn("Intento de registro con datos incompletos");
            throw new IllegalArgumentException("Todos los campos (cédula, nombre, correo, contraseña) son obligatorios.");
        }

        if (Paciente.findByCedula(dto.cedula) != null) {
            LOG.warnf("Conflicto: El paciente con cédula %s ya existe", dto.cedula);
            throw new IllegalStateException("El paciente con la cédula ingresada ya se encuentra registrado.");
        }

        Paciente paciente = new Paciente();
        paciente.cedula = dto.cedula;
        paciente.nombre = dto.nombre;
        paciente.correo = dto.correo;
        paciente.password = BcryptUtil.bcryptHash(dto.password); // Hashing seguro

        paciente.persist();
        LOG.infof("Paciente %s registrado exitosamente en MongoDB", dto.cedula);

        return PacienteResponseDTO.fromEntity(paciente);
    }

    public PacienteResponseDTO autenticarPaciente(LoginRequestDTO dto) {
        LOG.infof("Intento de inicio de sesión para la cédula: %s", dto.cedula);

        if (dto.cedula == null || dto.password == null) {
            throw new IllegalArgumentException("La cédula y la contraseña son requeridas.");
        }

        Paciente paciente = Paciente.findByCedula(dto.cedula);
        if (paciente == null) {
            LOG.warnf("Fallo de autenticación: Cédula %s no encontrada", dto.cedula);
            throw new SecurityException("Credenciales inválidas: La cédula o la contraseña son incorrectas.");
        }

        if (!BcryptUtil.matches(dto.password, paciente.password)) {
            LOG.warnf("Fallo de autenticación: Contraseña incorrecta para la cédula %s", dto.cedula);
            throw new SecurityException("Credenciales inválidas: La cédula o la contraseña son incorrectas.");
        }

        LOG.infof("Inicio de sesión exitoso para el paciente: %s", paciente.nombre);
        return PacienteResponseDTO.fromEntity(paciente);
    }
}