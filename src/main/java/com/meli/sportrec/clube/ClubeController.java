package com.meli.sportrec.clube;

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
@RequestMapping("/clubes")
public class ClubeController {

    @Autowired
    ClubeRepository clubeRepository;

    @Autowired
    ClubeService clubeService;

    @PostMapping
    public ResponseEntity<ClubeModel> saveClube(@RequestBody @Valid ClubeRecordDto clubeRecordDto) {
        ClubeModel savedClube = clubeService.salvarClube(clubeRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClube);
    }

    @GetMapping
    public ResponseEntity<Page<ClubeModel>> getAll(@RequestParam(required = false) String clubeNome,
                                                   @RequestParam(required = false) String estado,
                                                   @RequestParam(required = false) Boolean ativo,
                                                   Pageable pageable) {


        Page<ClubeModel> clubes = clubeService.buscarClubesComFiltros(clubeNome, estado, ativo, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(clubes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id) {
        Optional<ClubeModel> clubeO = clubeService.buscarClubePorId(id);
        if (clubeO.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clubeO.get());
    }


    @PutMapping("{id}")
    public ResponseEntity<Object> updateClube(@PathVariable (value="id") Long id,
                                              @RequestBody @Valid ClubeRecordDto clubeRecordDto){
       ClubeModel updateClube = clubeService.atualizarClube(id, clubeRecordDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateClube);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteClube(@PathVariable(value = "id") Long id) {
        clubeService.inativarClube(id);
        return ResponseEntity.status(HttpStatus.OK).body("Clube inativado com sucesso");
        }
    }


