package com.meli.sportrec.partida;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/partidas")
public class PartidaController {
    @Autowired
    PartidaService partidaService;
    @Autowired
    PartidaRepository partidaRepository;

    @PostMapping
    public ResponseEntity<PartidaModel> criarPartida(@Valid @RequestBody PartidaRecordDto partidaRecordDto) {
        PartidaModel partida = partidaService.criarPartida(partidaRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(partida);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPartida(@PathVariable(value = "id") Long id) {
        Optional<PartidaModel> partida = partidaService.buscarPartidaPorId(id);
        if (partida.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(partida.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePartida(@Valid @RequestBody
                                                             PartidaRecordDto partidaRecordDto,
                                                         @PathVariable(value = "id") Long id) {
        PartidaModel updatePartida = partidaService.atualizarPartida(id, partidaRecordDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatePartida);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePartida(@PathVariable(value = "id") Long id) {
        partidaService.removerPartida(id);
        return ResponseEntity.status(HttpStatus.OK).body("Partida removida com sucesso");
    }

}
