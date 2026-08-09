package org.acme;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.dto.LoginRequestDTO;
import org.acme.dto.PacienteRegistroDTO;
import org.acme.dto.PacienteResponseDTO;
import org.acme.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class PacienteResourceTest {

    @InjectMock
    PacienteService pacienteService;

    // --- CASOS DE USO DE LOGIN ---

    @Test
    public void testLoginExitoso() {
        PacienteResponseDTO responseDTO = new PacienteResponseDTO();
        responseDTO.cedula = "8-888-8888";
        responseDTO.nombre = "Juan Perez";
        responseDTO.correo = "juan@correo.com";

        Mockito.when(pacienteService.autenticarPaciente(any(LoginRequestDTO.class)))
                .thenReturn(responseDTO);

        given()
                .contentType(ContentType.JSON)
                .body(Map.of("cedula", "8-888-8888", "password", "123456"))
                .when().post("/api/pacientes/login")
                .then()
                .statusCode(200)
                .body("cedula", is("8-888-8888"))
                .body("nombre", is("Juan Perez"));
    }

    @Test
    public void testLoginCredencialesInvalidas() {
        Mockito.when(pacienteService.autenticarPaciente(any(LoginRequestDTO.class)))
                .thenThrow(new SecurityException("Credenciales inválidas: La cédula o la contraseña son incorrectas."));

        given()
                .contentType(ContentType.JSON)
                .body(Map.of("cedula", "00-000-0000", "password", "claveErronea"))
                .when().post("/api/pacientes/login")
                .then()
                .statusCode(401)
                .body("error", is("Credenciales inválidas: La cédula o la contraseña son incorrectas."));
    }

    @Test
    public void testLoginCamposIncompletos() {
        Mockito.when(pacienteService.autenticarPaciente(any(LoginRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("La cédula y la contraseña son requeridas."));

        given()
                .contentType(ContentType.JSON)
                .body(Map.of("cedula", "8-888-8888"))
                .when().post("/api/pacientes/login")
                .then()
                .statusCode(400)
                .body("error", is("La cédula y la contraseña son requeridas."));
    }

    // --- CASOS DE USO DE REGISTRO ---

    @Test
    public void testRegistroExitoso() {
        PacienteResponseDTO responseDTO = new PacienteResponseDTO();
        responseDTO.cedula = "8-999-9999";
        responseDTO.nombre = "Maria Lopez";
        responseDTO.correo = "maria@correo.com";

        Mockito.when(pacienteService.registrarPaciente(any(PacienteRegistroDTO.class)))
                .thenReturn(responseDTO);

        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "cedula", "8-999-9999",
                        "nombre", "Maria Lopez",
                        "correo", "maria@correo.com",
                        "password", "123456"
                ))
                .when().post("/api/pacientes/registro")
                .then()
                .statusCode(201)
                .body("cedula", is("8-999-9999"));
    }

    @Test
    public void testRegistroUsuarioExistente() {
        Mockito.when(pacienteService.registrarPaciente(any(PacienteRegistroDTO.class)))
                .thenThrow(new IllegalStateException("El paciente con la cédula ingresada ya se encuentra registrado."));

        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "cedula", "8-888-8888",
                        "nombre", "Juan Perez",
                        "correo", "juan@correo.com",
                        "password", "123456"
                ))
                .when().post("/api/pacientes/registro")
                .then()
                .statusCode(409)
                .body("error", is("El paciente con la cédula ingresada ya se encuentra registrado."));
    }
}