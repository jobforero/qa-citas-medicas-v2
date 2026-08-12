package org.acme.repository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CitaRepositoryTest {

    @Inject
    CitaRepository citaRepository;

    @Test
    public void testBuscarPorPacienteCedula() {
        assertNotNull(citaRepository.buscarPorPacienteCedula("8-888-8888"));
    }
}