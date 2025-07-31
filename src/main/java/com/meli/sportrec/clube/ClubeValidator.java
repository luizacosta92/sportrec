package com.meli.sportrec.clube;


import com.meli.sportrec.exceptionhandler.EntityConflictException;
import com.meli.sportrec.partida.PartidaModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClubeValidator {

    public void validate(ClubeModel clubeModel,  PartidaModel partidaModel) {
        if (partidaModel != null) {
            validarDataCriacaoClubeMenorQuePrimeiraPartida(clubeModel, partidaModel);
        }
    }

    private void validarDataCriacaoClubeMenorQuePrimeiraPartida(ClubeModel clubeModel, PartidaModel partidaModel) {
        LocalDate dataCriacaoClube = clubeModel.getDataCriacao();
        LocalDate dataPrimeiraPartida = partidaModel.getDataHoraPartida().toLocalDate();

        if(dataCriacaoClube.isAfter(dataPrimeiraPartida)) {
            throw new EntityConflictException("A data de criação não pode ser posterior à primeira partida");

        }
        }



}

