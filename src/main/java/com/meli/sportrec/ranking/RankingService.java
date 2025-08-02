package com.meli.sportrec.ranking;

import com.meli.sportrec.clube.ClubeModel;
import com.meli.sportrec.clube.ClubeRepository;
import com.meli.sportrec.partida.PartidaModel;
import com.meli.sportrec.partida.PartidaRepository;
import com.meli.sportrec.retrospecto.RetrospectoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private RankingUtils rankingUtils;

    @Autowired
    private RankingValidator rankingValidator;

    public Optional<List<RankingDto>> buscarRanking(TipoRanking tipoRanking) {


        try {
            List<ClubeModel> clubesAtivos = obterClubesAtivos();

            List<RankingDto> ranking = new ArrayList<>();

            for (ClubeModel clube : clubesAtivos) {
                RankingDto clubeRanking = criarRankingClube(clube);

                if (rankingValidator.incluirNoRanking(clubeRanking, tipoRanking)) {
                    ranking.add(clubeRanking);
                } else {
                }
            }

            rankingValidator.ordenarRanking(ranking, tipoRanking);
            rankingValidator.definirPosicoes(ranking);

            return Optional.of(ranking);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private List<ClubeModel> obterClubesAtivos() {
        List<ClubeModel> todosClubes = clubeRepository.findAll();
        List<ClubeModel> clubesAtivos = todosClubes.stream()
                .filter(ClubeModel::getAtivo)
                .collect(Collectors.toList());

        return clubesAtivos;
    }

    private RankingDto criarRankingClube(ClubeModel clube) {
        RankingDto rankingDto = new RankingDto(clube.getClubeNome());

        Specification<PartidaModel> specification = RetrospectoSpecification.clubeParticipou(clube.getId());
        List<PartidaModel> partidas = partidaRepository.findAll(specification);

        rankingUtils.calcularEstatisticasClube(rankingDto, partidas, clube.getId());

        return rankingDto;
    }
}