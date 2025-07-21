package com.meli.sportrec.clube;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClubeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClubeRepository clubeRepository;

    @Test

    void deveVerificarSeExisteClubePorNomeEEstado() {
        ClubeModel clube = new ClubeModel();
        clube.setClubeNome("Santos");
        clube.setEstado("SP");
        clube.setAtivo(true);
        clube.setDataCriacao(LocalDate.now());
        entityManager.persistAndFlush(clube);

        assertTrue(clubeRepository.existsByClubeNomeIgnoreCaseAndEstado("santos", "SP"));
        assertTrue(clubeRepository.existsByClubeNomeIgnoreCaseAndEstado("SANTOS", "SP"));
        assertFalse(clubeRepository.existsByClubeNomeIgnoreCaseAndEstado("Santos", "RJ"));
    }
}