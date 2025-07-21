package com.meli.sportrec.clube;


import com.meli.sportrec.exceptionhandler.EntityConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ClubeValidator {
    @Autowired
    ClubeRepository clubeRepository;

    private void validarDataCriacaoContraPartidas(String clubeNome, LocalDate novaDataCriacao) {
        try {
            LocalDateTime primeiraPartidaDateTime = clubeRepository.dataPrimeiraPartida(clubeNome);

            if (primeiraPartidaDateTime != null) {

                LocalDate primeiraPartidaDate = primeiraPartidaDateTime.toLocalDate();

                if (novaDataCriacao.isAfter(primeiraPartidaDate)) {
                    throw new EntityConflictException("A data de criação não pode ser posterior à primeira partida");
                }
            }
        } catch (EntityConflictException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new EntityConflictException("Erro ao validar data de criação");
        }
    }
    public void validarDataCriacao(String clubeNome, LocalDate novaDataCriacao) {
        validarDataCriacaoContraPartidas(clubeNome, novaDataCriacao);
    }
}

