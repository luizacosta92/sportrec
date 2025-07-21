package com.meli.sportrec.clube;

import com.meli.sportrec.exceptionhandler.EntityConflictException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ClubeService {

    @Autowired
    ClubeRepository clubeRepository;

    @Autowired
    ClubeValidator clubeValidator;

    @Transactional
    public ClubeModel salvarClube(ClubeRecordDto clubeRecordDto) {
        if (clubeRepository.existsByClubeNomeIgnoreCaseAndEstado(clubeRecordDto.clubeNome(), clubeRecordDto.estado())) {
            throw new EntityConflictException("Clube " + clubeRecordDto.clubeNome() + " já existe no estado " + clubeRecordDto.estado());
        }
        var clubeModel = new ClubeModel();
        BeanUtils.copyProperties(clubeRecordDto, clubeModel); //TODO criar mapper apenas para receber o DTO e criar o Model na mão
        return clubeRepository.save(clubeModel);
    }

    public Page<ClubeModel> buscarClubesComFiltros(String clubeNome, String estado, Boolean ativo, Pageable pageable) {
        if (pageable.getPageNumber() < 0) {
            throw new IllegalArgumentException("Número da página não pode ser negativo");
        }
        return clubeRepository.findByFiltros(clubeNome, estado, ativo, pageable);

    }

    public Optional<ClubeModel> buscarClubePorId(Long id) {
        if (clubeRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Clube não encontrado");
        }
        return clubeRepository.findById(id);
    }

    @Transactional
    public ClubeModel atualizarClube(Long id, ClubeRecordDto clubeRecordDto) {
        Optional<ClubeModel> clubeO = clubeRepository.findById(id);
        if (clubeO.isEmpty()) {
            throw new EntityNotFoundException("Clube não encontrado");
        }
        if (clubeRepository.existsByClubeNomeIgnoreCaseAndEstadoAndIdNot(
                clubeRecordDto.clubeNome(), clubeRecordDto.estado(), id)) {
            throw new EntityConflictException("Clube " + clubeRecordDto.clubeNome() + " já existe no estado " + clubeRecordDto.estado());
        }
        if (clubeRecordDto.dataCriacao().isAfter(LocalDate.now())) {
            throw new EntityConflictException("Data de criação não pode ser no futuro");
        }
       clubeValidator.validarDataCriacao(clubeRecordDto.clubeNome(), clubeRecordDto.dataCriacao());


        var clubeModel = clubeO.get();
        BeanUtils.copyProperties(clubeRecordDto, clubeModel);
        return clubeRepository.save(clubeModel);

    }



    @Transactional
    public void inativarClube(Long id) {
        Optional<ClubeModel> clubeO = clubeRepository.findById(id);
        if (clubeO.isEmpty()) {
            throw new EntityNotFoundException("Clube não encontrado"); //Excecao Usada para camadas de service
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clube não encontrado"); Usado para camada de controller
        }
        var clubeModel = clubeO.get();
        clubeModel.setAtivo(false);
        clubeRepository.save(clubeModel);
    }
}