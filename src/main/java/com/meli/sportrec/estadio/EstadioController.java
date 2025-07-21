package com.meli.sportrec.estadio;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/estadios")
public class EstadioController {

    @Autowired
    EstadioRepository estadioRepository;

    @Autowired
    EstadioService estadioService;

    @PostMapping
    public ResponseEntity<EstadioModel> saveEstadio(@RequestBody @Valid EstadioRecordDto estadioRecordDto) {
        EstadioModel savedEstadio = estadioService.salvarEstadio(estadioRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEstadio);
    }

    @GetMapping
    public ResponseEntity<Page<EstadioModel>> getAll(Pageable pageable) {
        Page<EstadioModel> estadios = estadioService.buscarTodosEstadios(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(estadios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id) {
        Optional<EstadioModel> estadioO = estadioService.buscarEstadioPorId(id);
        if (estadioO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(estadioO.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEstadio(@PathVariable(value = "id") Long id,
                                                @RequestBody @Valid EstadioRecordDto estadioRecordDto) {
        EstadioModel updateEstadio = estadioService.atualizarEstadio(id, estadioRecordDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateEstadio);
    }
}