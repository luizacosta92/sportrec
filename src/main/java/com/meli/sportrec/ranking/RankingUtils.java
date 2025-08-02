package com.meli.sportrec.ranking;

import com.meli.sportrec.partida.PartidaModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RankingUtils {

    public void calcularEstatisticasClube(RankingDto rankingDto, List<PartidaModel> partidas, Long clubId) {
        if (partidas.isEmpty()) {
            return;
        }
        int vitorias = 0;
        int empates = 0;
        int derrotas = 0;
        int golsFeitos = 0;
        int golsSofridos = 0;

        for(PartidaModel partida: partidas) {
            boolean mandante = partida.getClubeMandante().getId().equals(clubId);

            if(mandante) {
                golsFeitos += partida.getGolsMandante();
                golsSofridos += partida.getGolsVisitante();

                if (partida.getGolsMandante() > partida.getGolsVisitante()) {
                    vitorias++;
                } else if (partida.getGolsMandante().equals(partida.getGolsVisitante())) {
                    empates++;
                } else {
                    derrotas++;
                }
            } else{
                golsFeitos += partida.getGolsVisitante();
                golsSofridos += partida.getGolsMandante();

                if (partida.getGolsVisitante() > partida.getGolsMandante()){
                    vitorias++;
                } else if (partida.getGolsVisitante().equals(partida.getGolsMandante())) {
                    empates++;
                } else {
                    derrotas++;
                }
            }
        }
        rankingDto.setTotalJogos(partidas.size());
        rankingDto.setVitorias(vitorias);
        rankingDto.setEmpates(empates);
        rankingDto.setDerrotas(derrotas);
        rankingDto.setGolsFeitos(golsFeitos);
        rankingDto.setGolsSofridos(golsSofridos);
        rankingDto.calcularPontos();
    }
}
