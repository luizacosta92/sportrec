package com.meli.sportrec.retrospecto;

import com.meli.sportrec.clube.ClubeRepository;
import com.meli.sportrec.partida.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/retrospecto")
public class RetrospectoController {
    @Autowired
    private RetrospectoService retrospectoService;
    @Autowired
    private ClubeRepository clubeRepository;
    @Autowired
    private PartidaRepository partidaRepository;

    @GetMapping("/{clubeId}/geral")
    public ResponseEntity<Object> buscarRetrospectoGeral(@PathVariable(value = "clubeId") Long clubeId) {
        Optional<RetrospectoGeralDto> retrospecto = retrospectoService.buscarRetrospectoGeral(clubeId);

        if (retrospecto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(retrospecto.get());
    }

    @GetMapping("/{clubeId}/adversarios")
    public ResponseEntity<Object> buscarRetrospectosConfrontoAdversarios(@PathVariable(value = "clubeId") Long clubeId) {
        Optional<List<RetrospectoAdversarioDto>> retrospectosAdversarios = retrospectoService.buscarRetrospectoAdversario(clubeId);
        if (retrospectosAdversarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Clube não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(retrospectosAdversarios.get());
    }

    @GetMapping("/confronto/{clube1Id}/{clube2Id}")
    public ResponseEntity<Object> buscarConfrontoDireto(@PathVariable(value = "clube1Id") Long clube1Id,
                                                        @PathVariable(value = "clube2Id") Long clube2Id){
        Optional<ConfrontoDiretoDto> confrontoDireto = retrospectoService.buscarConfrontoDireto(clube1Id, clube2Id);
        if (confrontoDireto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Clube não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(confrontoDireto.get());
    }
    }

