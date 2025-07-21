package com.meli.sportrec.estadio;

import com.meli.sportrec.exceptionhandler.EntityConflictException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstadioService {

    @Autowired
    EstadioRepository estadioRepository;

    @Transactional
    public EstadioModel salvarEstadio(EstadioRecordDto estadioRecordDto) {
        if (estadioRepository.existsByEstadioNomeIgnoreCase(estadioRecordDto.estadioNome())) {
            throw new EntityConflictException("Estádio " + estadioRecordDto.estadioNome() + " já existe");
        }
        var estadioModel = new EstadioModel();
        BeanUtils.copyProperties(estadioRecordDto, estadioModel);
        return estadioRepository.save(estadioModel);
    }

    public Page<EstadioModel> buscarTodosEstadios(Pageable pageable) {
        if (pageable.getPageNumber() < 0) {
            throw new IllegalArgumentException("Número da página não pode ser negativo");
        }
        return estadioRepository.findAllOrderByNome(pageable);
    }

    public Optional<EstadioModel> buscarEstadioPorId(Long id) {
        Optional<EstadioModel> estadio = estadioRepository.findById(id);
        if (estadio.isEmpty()) {
            throw new EntityNotFoundException("Estádio não encontrado");
        }
        return estadio;
    }

    @Transactional
    public EstadioModel atualizarEstadio(Long id, EstadioRecordDto estadioRecordDto) {
        Optional<EstadioModel> estadioO = estadioRepository.findById(id);
        if (estadioO.isEmpty()) {
            throw new EntityNotFoundException("Estádio não encontrado");
        }

        if (estadioRepository.existsByEstadioNomeIgnoreCaseAndIdNot(
                estadioRecordDto.estadioNome(), id)) {
            throw new EntityConflictException("Estádio " + estadioRecordDto.estadioNome() + " já existe");
        }

        var estadioModel = estadioO.get();
        BeanUtils.copyProperties(estadioRecordDto, estadioModel);
        return estadioRepository.save(estadioModel);
    }
}