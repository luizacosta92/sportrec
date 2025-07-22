package com.meli.sportrec.partida;

import com.meli.sportrec.clube.ClubeModel;
import com.meli.sportrec.clube.ClubeRepository;
import com.meli.sportrec.exceptionhandler.EntityConflictException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



@Component
public class PartidaValidator {
    @Autowired
    PartidaRepository partidaRepository;
    @Autowired
    ClubeRepository clubeRepository;

    PartidaRecordDto partidaRecordDto;

    public void validarClubes(Long clubeMandanteId, Long clubeVisitanteId, ClubeModel clubeMandante, ClubeModel clubeVisitante, PartidaModel partidaModel) {
        validarClubesIguais(clubeMandanteId, clubeVisitanteId);
        validarDataPartidaDepoisDaCriacaoDosClubes(partidaModel, clubeMandante, clubeVisitante);
        validarClubesAtivos(clubeMandante, clubeVisitante);


    }
    private void validarClubesIguais(Long clubeMandanteId, Long clubeVisitanteId) {
        if (clubeMandanteId.equals(clubeVisitanteId)) {
            throw new ValidationException("Os clubes devem ser diferentes");
        }
    }

    private void validarDataPartidaDepoisDaCriacaoDosClubes(PartidaModel partidaModel, ClubeModel clubeMandante, ClubeModel clubeVisitante) {
        LocalDate dataPartida = partidaModel.getDataHoraPartida().toLocalDate();

        if (dataPartida.isBefore(clubeMandante.getDataCriacao())) {
            throw new EntityConflictException("Data da partida é anterior à criação do clube mandante");
        }
        if (dataPartida.isBefore(clubeVisitante.getDataCriacao())) {
            throw new EntityConflictException("Data da partida é anterior à criação do clube visitante");
        }
    }

    private void validarClubesAtivos(ClubeModel clubeMandante, ClubeModel clubeVisitante) {
        if (!clubeMandante.getAtivo()) {
            throw new EntityConflictException("Clube mandante " + clubeMandante.getClubeNome() + " está inativo");
        }

        if (!clubeVisitante.getAtivo()) {
            throw new EntityConflictException("Clube visitante " + clubeVisitante.getClubeNome() + " está inativo");
        }
    }

    public void validarConflitosHorarios(Long clubeMandanteId, Long clubeVisitanteId, LocalDateTime dataHoraPartida, Long excluirPartidaId, Long estadioId) {
        validarConflitosHorarioClubes(clubeMandanteId, clubeVisitanteId, dataHoraPartida, excluirPartidaId);
        validarEstadioDisponivel(estadioId, dataHoraPartida, excluirPartidaId);
        validarDataNoPassado(dataHoraPartida);
    }

    private void validarConflitosHorarioClubes(Long clubeMandanteId, Long clubeVisitanteId,
                                         LocalDateTime dataHoraPartida, Long partidaIdParaExcluir) {
        LocalDateTime inicio = dataHoraPartida.minusHours(48);
        LocalDateTime fim = dataHoraPartida.plusHours(48);

        List<PartidaModel> conflitosMandante = partidaRepository.findConflitoHorarioClube(
                clubeMandanteId, inicio, fim, partidaIdParaExcluir);

        if (!conflitosMandante.isEmpty()) {
            throw new EntityConflictException("Clube mandante possui outra partida em menos de 48 horas");
        }

        List<PartidaModel> conflitosVisitante = partidaRepository.findConflitoHorarioClube(
                clubeVisitanteId, inicio, fim, partidaIdParaExcluir);

        if (!conflitosVisitante.isEmpty()) {
            throw new EntityConflictException("Clube visitante possui outra partida em menos de 48 horas");
        }
    }

    private void validarEstadioDisponivel(Long estadioId, LocalDateTime dataHoraPartida, Long excluirPartidaId) {
        List<PartidaModel> conflitosEstadio = partidaRepository.findConflitosEstadio(estadioId, dataHoraPartida, excluirPartidaId);

        if (!conflitosEstadio.isEmpty()) {
            PartidaModel partidaModel = conflitosEstadio.get(0);
            throw new EntityConflictException("Estádio já tem jogo realizado no horário informado");
        }
    }

    private void validarDataNoPassado(LocalDateTime dataHoraPartida) {
        if (dataHoraPartida.isAfter(LocalDateTime.now())) {
            throw new ValidationException("O registro da partida não pode ser no futuro");
        }
    }

    private void validarPartidaComCriacaoDoClube(LocalDateTime dataHoraPartida) {
        LocalDate dataCriacaoMandante = clubeRepository.findClubeDataCriacaById(partidaRecordDto.clubeMandanteId());
        LocalDate dataCriacaoVisitante = clubeRepository.findClubeDataCriacaById(partidaRecordDto.clubeVisitanteId());


        if (dataHoraPartida.toLocalDate().isBefore(dataCriacaoMandante) || partidaRecordDto.dataHoraPartida().toLocalDate().isBefore(dataCriacaoVisitante)) {
            throw new EntityConflictException("Data da partida não pode ser antes da criação do clube");
        }
    }

}
