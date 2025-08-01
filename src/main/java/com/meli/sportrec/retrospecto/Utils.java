package com.meli.sportrec.retrospecto;

import com.meli.sportrec.partida.PartidaModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Utils {
    public void calculoGeral (RetrospectoGeralDto retrospecto,
                              List<PartidaModel> partidas, Long clubeId) {
        int vitorias = 0;
        int empates = 0;
        int derrotas = 0;
        int golsFeitos = 0;
        int golsSofridos = 0;

        for (PartidaModel partida: partidas) {
            boolean mandante = partida.getClubeMandante().getId().equals(clubeId);

            if(mandante) {
                golsFeitos += partida.getGolsMandante();
                golsSofridos += partida.getGolsVisitante();

                if (partida.getGolsMandante() > partida.getGolsVisitante()){
                    vitorias++;
                } else if (partida.getGolsMandante().equals(partida.getGolsVisitante())) {
                    empates++;
                } else  {
                    derrotas++;
                }
            } else {
                golsFeitos += partida.getGolsVisitante();
                golsSofridos += partida.getGolsMandante();
                if (partida.getGolsVisitante() > partida.getGolsMandante()) {
                    vitorias++;
                } else if (partida.getGolsVisitante().equals(partida.getGolsMandante())) {
                    empates++;
                } else   {
                    derrotas++;
                }
            }
        }

        retrospecto.setTotalJogos(partidas.size());
        retrospecto.setVitorias(vitorias);
        retrospecto.setEmpates(empates);
        retrospecto.setDerrotas(derrotas);
        retrospecto.setGolsFeitos(golsFeitos);
        retrospecto.setGolsSofridos(golsSofridos);

    }

    public void calculoContraAdversario(RetrospectoAdversarioDto retrospectoAdversario,
                                        List<PartidaModel> partidas, Long clubeId) {
        int vitorias = 0;
        int empates = 0;
        int derrotas = 0;
        int golsFeitos = 0;
        int golsSofridos = 0;

        for (PartidaModel partida: partidas) {
            boolean mandante = partida.getClubeMandante().getId().equals(clubeId);
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
            } else  {
                golsFeitos += partida.getGolsVisitante();
                golsSofridos += partida.getGolsMandante();
                if (partida.getGolsVisitante() > partida.getGolsMandante()) {
                    vitorias++;
                } else if (partida.getGolsVisitante().equals(partida.getGolsMandante())) {
                    empates++;
                } else {
                    derrotas++;
                }
            }
        }
        retrospectoAdversario.setTotalJogos(partidas.size());
        retrospectoAdversario.setVitorias(vitorias);
        retrospectoAdversario.setEmpates(empates);
        retrospectoAdversario.setDerrotas(derrotas);
        retrospectoAdversario.setGolsFeitos(golsFeitos);
        retrospectoAdversario.setGolsSofridos(golsSofridos);
    }

    public Map<Long, List<PartidaModel>> agruparPartidasPorAdversario(List<PartidaModel> partidas,
                                                                      Long clubeId) {
        return  partidas.stream()
                .collect(Collectors.groupingBy(partida -> {
                    if (partida.getClubeMandante().getId().equals(clubeId)) {
                        return partida.getClubeVisitante().getId();
                    } else {
                        return partida.getClubeMandante().getId();
                    }
                }));
    }

    public void atualizarRetrospecto(RetrospectoConfrontoDto retrospectoConfronto, Integer golsFeitos, Integer golsSofridos) {
        retrospectoConfronto.setGolsFeitos(retrospectoConfronto.getGolsFeitos() + golsFeitos);
        retrospectoConfronto.setGolsSofridos(retrospectoConfronto.getGolsSofridos() + golsSofridos);

        if (golsFeitos > golsSofridos) {
            retrospectoConfronto.setVitorias(retrospectoConfronto.getVitorias() + 1);
        } else if (golsFeitos.equals(golsSofridos)) {
            retrospectoConfronto.setEmpates(retrospectoConfronto.getEmpates() + 1);
        } else {
            retrospectoConfronto.setDerrotas(retrospectoConfronto.getDerrotas() + 1);
        }
    }

    public void calcularRetrospectosConfrontoDireto(ConfrontoDiretoDto confrontoDireto,
                                                    List<PartidaModel> partidas,
                                                    Long clube1Id, Long clube2Id) {
        RetrospectoConfrontoDto retrospectoClube1 = confrontoDireto.getRetrospectoClube1();
        RetrospectoConfrontoDto retrospectoClube2 = confrontoDireto.getRetrospectoClube2();


        for (PartidaModel partida: partidas) {
            boolean clube1Mandante = partida.getClubeMandante().getId().equals(clube1Id);
            if(clube1Mandante) {
                atualizarRetrospecto(retrospectoClube1, partida.getGolsMandante(), partida.getGolsVisitante());
                atualizarRetrospecto(retrospectoClube2, partida.getGolsVisitante(), partida.getGolsMandante());
            } else {
                atualizarRetrospecto(retrospectoClube2, partida.getGolsVisitante(), partida.getGolsMandante());
                atualizarRetrospecto(retrospectoClube1, partida.getGolsMandante(), partida.getGolsVisitante());

            }
        }
    }
}
