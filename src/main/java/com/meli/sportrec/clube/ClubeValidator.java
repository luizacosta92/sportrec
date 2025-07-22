package com.meli.sportrec.clube;


import com.meli.sportrec.partida.PartidaModel;
import org.springframework.stereotype.Component;

import java.time.chrono.ChronoLocalDate;

@Component
public class ClubeValidator {

    public void validate(ClubeModel clubeModel,  PartidaModel partidaModel) {
        validarDataCriacaoClubeMenorQuePrimeiraPartida(clubeModel, partidaModel);
    }

    private void validarDataCriacaoClubeMenorQuePrimeiraPartida(ClubeModel clubeModel, PartidaModel partidaModel) {

        if(clubeModel.getDataCriacao().isAfter(ChronoLocalDate.from(partidaModel.getDataHoraPartida()))) {
        }
        }



}

