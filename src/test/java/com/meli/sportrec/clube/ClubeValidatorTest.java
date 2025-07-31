package com.meli.sportrec.clube;

import com.meli.sportrec.exceptionhandler.EntityConflictException;
import com.meli.sportrec.partida.PartidaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClubeValidatorTest {
    @Mock
    private ClubeRepository clubeRepository;

    @InjectMocks
    private ClubeValidator clubeValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

        @Test
        public void testGivenDataCriacaoAfterPrimeiraPartidaWhenValidateDataCriacaoThenThrowException(){
            Long idClube = 1L;

            ClubeModel clube = new ClubeModel();
            clube.setId(idClube);
            clube.setClubeNome("Clube 1");
            clube.setDataCriacao(LocalDate.now().plusDays(5));

            PartidaModel partida = new PartidaModel();
            partida.setDataHoraPartida(LocalDateTime.now());


            EntityConflictException exception = assertThrows(
                    EntityConflictException.class,
                    () -> clubeValidator.validate(clube, partida));

            assertThat(exception.getMessage())
                    .isEqualTo("A data de criação não pode ser posterior à primeira partida");

        }

        @Test
        public void testGivenDataAcessExceptionWhenValidateDataCriacaoThenThrowException(){
            Long idClube = 1L;
            ClubeModel clube = new ClubeModel();
            clube.setClubeNome("Clube 1");
            clube.setDataCriacao(LocalDate.now());

            PartidaModel partida = new PartidaModel();
            partida.setDataHoraPartida(LocalDateTime.now().minusDays(1));

            when(clubeRepository.findClubeDataCriacaById(idClube))
                    .thenThrow(new DataAccessException("Erro de conexão com banco") {});

            EntityConflictException exception = assertThrows(
                    EntityConflictException.class,
                    () -> clubeValidator.validate(clube, partida)
            );

            assertThat(exception.getMessage())
                    .isEqualTo("Erro ao validar data de criação");
        }

    @Test // clube não tem partidas registradas
    public void testGivenClubeWithoutPartidasWhenValidateDataCriacaoThenDoNotThrowException() {

        ClubeModel clube = new ClubeModel();
        clube.setClubeNome("Clube 1");
        clube.setDataCriacao(LocalDate.now());

        PartidaModel partidaNaoExiste = null;

        assertDoesNotThrow(() -> clubeValidator.validate(clube, partidaNaoExiste));
    }

    @Test // data de criação anterior à primeira partida
    public void testGivenDataCriacaoBeforePrimeiraPartidaWhenValidateDataCriacaoThenDoNotThrowException() {

        ClubeModel clube = new ClubeModel();
        clube.setClubeNome("Clube 1");
        clube.setDataCriacao(LocalDate.now().minusDays(5));

        PartidaModel partida = new PartidaModel();
        partida.setId(1L);
        partida.setDataHoraPartida(LocalDateTime.now());


        assertDoesNotThrow(() -> clubeValidator.validate(clube, partida));
    }
        }

