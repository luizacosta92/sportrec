package com.meli.sportrec.partida;

import com.meli.sportrec.clube.ClubeModel;
import com.meli.sportrec.clube.ClubeRepository;
import com.meli.sportrec.clube.ClubeService;
import com.meli.sportrec.estadio.EstadioModel;
import com.meli.sportrec.estadio.EstadioService;
import com.meli.sportrec.exceptionhandler.EntityConflictException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartidaService {
    @Autowired
    PartidaRepository partidaRepository;
    @Autowired
    ClubeService clubeService;
    @Autowired
    ClubeRepository clubeRepository;
    @Autowired
    EstadioService estadioService;
    @Autowired
    PartidaValidator partidaValidator;


    @Transactional
    public PartidaModel criarPartida(PartidaRecordDto partidaRecordDto) {

        Optional<ClubeModel> clubeMandante = clubeService.buscarClubePorId(partidaRecordDto.clubeMandanteId()); //nao é boa prática chamar um service para outro service
        Optional<ClubeModel> clubeVisitante = clubeService.buscarClubePorId(partidaRecordDto.clubeVisitanteId()); //melhor chamar para o repository
        Optional<EstadioModel> estadio = estadioService.buscarEstadioPorId(partidaRecordDto.estadioId());
        PartidaModel partidaTemporaria = new PartidaModel();
        partidaTemporaria.setDataHoraPartida(partidaRecordDto.dataHoraPartida());

        partidaValidator.validarClubes(
                partidaRecordDto.clubeMandanteId(),
                partidaRecordDto.clubeVisitanteId(),
                clubeMandante.get(),
                clubeVisitante.get(),
                partidaTemporaria);


        var partidaModel = new PartidaModel();
        BeanUtils.copyProperties(partidaRecordDto, partidaModel);
        partidaModel.setClubeMandante(clubeMandante.get());
        partidaModel.setClubeVisitante(clubeVisitante.get());
        partidaModel.setEstadio(estadio.get());

        return partidaRepository.save(partidaModel);
    }

    public Optional<PartidaModel> buscarPartidaPorId(Long id) {
        if (partidaRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Clube não encontrado");

        }
        return partidaRepository.findByIdComDetalhes(id);
    }

    @Transactional
    public PartidaModel atualizarPartida(Long id, PartidaRecordDto partidaRecordDto) {
        Optional<PartidaModel> partida = partidaRepository.findById(id);
        Boolean clubeMandanteAtivo = clubeRepository.findClubeAtivoById(partidaRecordDto.clubeMandanteId());
        Boolean clubeVisitanteAtivo = clubeRepository.findClubeAtivoById(partidaRecordDto.clubeVisitanteId());
        if (clubeMandanteAtivo == null || !clubeMandanteAtivo) {
            throw new EntityConflictException("Clube mandante está inativo");
        }
        if (clubeVisitanteAtivo == null || !clubeVisitanteAtivo) {
            throw new EntityConflictException("Clube visitante está inativo");
        }

        if (partida.isEmpty()) {
            throw new EntityNotFoundException("Partida não encontrada");
        }
        if (partidaRecordDto.clubeMandanteId().equals(partidaRecordDto.clubeVisitanteId())) {
            throw new EntityConflictException("Os clubes devem ser diferentes");
        }

        partidaValidator.validarConflitosHorarios(
                partidaRecordDto.clubeMandanteId(),
                partidaRecordDto.clubeVisitanteId(),
                partidaRecordDto.dataHoraPartida(),
                partidaRecordDto.estadioId(),
                null
        );


        var partidaModel = partida.get();
        return partidaRepository.save(partidaModel);

    }
}

