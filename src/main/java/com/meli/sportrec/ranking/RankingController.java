package com.meli.sportrec.ranking.controller;

import com.meli.sportrec.ranking.RankingDto;
import com.meli.sportrec.ranking.TipoRanking;
import com.meli.sportrec.ranking.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public ResponseEntity<Object> buscarRanking(@RequestParam(required = false, defaultValue = "PONTOS") String ordenarPor) {

        try {
            TipoRanking tipoRanking = TipoRanking.valueOf(ordenarPor.toUpperCase());

            Optional<List<RankingDto>> ranking = rankingService.buscarRanking(tipoRanking);
            return ResponseEntity.status(HttpStatus.OK).body(ranking.get());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tipo de ranking inválido. Use: Pontos, Gols, Vitorias ou Total_jogos.");
        }
    }
}