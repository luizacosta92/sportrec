package com.meli.sportrec.ranking;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class RankingValidator {

    public boolean incluirNoRanking(RankingDto clube, TipoRanking tipo){
        return switch(tipo){
            case PONTOS -> clube.getPontos() > 0;
            case GOLS -> clube.getGolsFeitos() > 0;
            case VITORIAS ->  clube.getVitorias() > 0;
            case TOTAL_JOGOS ->  clube.getTotalJogos() > 0;
        };
    }

    public void ordenarRanking(List<RankingDto> ranking, TipoRanking tipo){
        Comparator<RankingDto> comparador = switch (tipo){
            case PONTOS -> (c1, c2) -> c2.getPontos().compareTo( c1.getPontos());
            case GOLS -> (c1, c2) -> c2.getGolsFeitos().compareTo( c1.getGolsFeitos());
            case VITORIAS ->  (c1, c2) -> c2.getVitorias().compareTo( c1.getVitorias());
            case TOTAL_JOGOS -> (c1, c2) -> c2.getTotalJogos().compareTo( c1.getTotalJogos());
        };
        ranking.sort(comparador);
    }

    public void definirPosicoes(List<RankingDto> ranking){
        for (int i = 0; i<ranking.size(); i++){
            ranking.get(i).setPosicao(i+1);
        }
    }

}
