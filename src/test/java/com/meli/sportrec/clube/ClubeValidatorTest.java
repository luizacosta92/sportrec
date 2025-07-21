package com.meli.sportrec.clube;

import com.meli.sportrec.exceptionhandler.EntityConflictException;
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
            String clubeNome = "Clube 1";
            LocalDate novaDataCriacao = LocalDate.now().plusDays(5);

           when(clubeRepository.dataPrimeiraPartida(clubeNome)).thenReturn(LocalDateTime.now());


            EntityConflictException exception = assertThrows(
                    EntityConflictException.class,
                    () -> clubeValidator.validarDataCriacao(clubeNome, novaDataCriacao));

            assertThat(exception.getMessage())
                    .isEqualTo("A data de criação não pode ser posterior à primeira partida");

        }

        @Test
        public void testGivenDataAcessExceptionWhenValidateDataCriacaoThenThrowException(){
            String clubeNome = "Clube 1";
            LocalDate novaDataCriacao = LocalDate.now();

            when(clubeRepository.dataPrimeiraPartida(clubeNome))
                    .thenThrow(new DataAccessException("Erro de conexão com banco") {});

            EntityConflictException exception = assertThrows(
                    EntityConflictException.class,
                    () -> clubeValidator.validarDataCriacao(clubeNome, novaDataCriacao)
            );

            assertThat(exception.getMessage())
                    .isEqualTo("Erro ao validar data de criação");
        }

    @Test // clube não tem partidas registradas
    public void testGivenClubeWithoutPartidasWhenValidateDataCriacaoThenDoNotThrowException() {

        String clubeNome = "Clube 1";
        LocalDate novaDataCriacao = LocalDate.now();

        when(clubeRepository.dataPrimeiraPartida(clubeNome)).thenReturn(null);

        assertDoesNotThrow(() -> clubeValidator.validarDataCriacao(clubeNome, novaDataCriacao));
    }

    @Test // data de criação anterior à primeira partida
    public void testGivenDataCriacaoBeforePrimeiraPartidaWhenValidateDataCriacaoThenDoNotThrowException() {

        String clubeNome = "Clube 1";
        LocalDate novaDataCriacao = LocalDate.now().minusDays(5);

        when(clubeRepository.dataPrimeiraPartida(clubeNome))
                .thenReturn(LocalDateTime.now());

        assertDoesNotThrow(() -> clubeValidator.validarDataCriacao(clubeNome, novaDataCriacao));
    }
        }

