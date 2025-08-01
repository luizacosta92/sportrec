package com.meli.sportrec.retrospecto;

import com.meli.sportrec.clube.ClubeModel;
import com.meli.sportrec.clube.ClubeRepository;
import com.meli.sportrec.partida.PartidaModel;
import com.meli.sportrec.partida.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RetrospectoService {
    @Autowired
    private PartidaRepository partidaRepository;
    @Autowired
    private ClubeRepository clubeRepository;
    @Autowired
    private Utils utils;

    public Optional<RetrospectoGeralDto> buscarRetrospectoGeral(Long clubeId){
        Optional<ClubeModel> clubeOptional = clubeRepository.findById(clubeId);
        if (clubeOptional.isEmpty()) {
            return Optional.empty();
        }
        ClubeModel clube = clubeOptional.get();
        Specification<PartidaModel> specification = RetrospectoSpecification.clubeParticipou(clubeId);
        List<PartidaModel> partidas = partidaRepository.findAll(specification);

        RetrospectoGeralDto retrospectoGeral = new RetrospectoGeralDto(clube.getClubeNome());

        if (!partidas.isEmpty()) {
            utils.calculoGeral(retrospectoGeral, partidas, clubeId);
        }
        return Optional.of(retrospectoGeral);
    }

    public Optional<List<RetrospectoAdversarioDto>> buscarRetrospectoAdversario(Long clubeId) {
        Optional<ClubeModel> clubeOptional = clubeRepository.findById(clubeId);
        if (clubeOptional.isEmpty()) {
            return Optional.empty();
        }

        Specification<PartidaModel> spec = RetrospectoSpecification.clubeParticipou(clubeId);
        List<PartidaModel> partidas = partidaRepository.findAll(spec);

        if (partidas.isEmpty()) {
            return Optional.of(new ArrayList<>());
        }

        Map<Long, List<PartidaModel>> partidasPorAdversario =
                utils.agruparPartidasPorAdversario(partidas, clubeId);

        List<RetrospectoAdversarioDto> resultado = new ArrayList<>();

        for (Map.Entry<Long, List<PartidaModel>> entry : partidasPorAdversario.entrySet()) {
            Long adversarioId = entry.getKey();
            List<PartidaModel> partidasContraAdversario = entry.getValue();


            String nomeAdversario = clubeRepository.findById(adversarioId)
                    .map(ClubeModel::getClubeNome)
                    .orElse("Adversário não encontrado");

            RetrospectoAdversarioDto retrospectoAdversario = new RetrospectoAdversarioDto(nomeAdversario);
            utils.calculoContraAdversario(retrospectoAdversario, partidasContraAdversario, clubeId);

            resultado.add(retrospectoAdversario);
        }
        RetrospectoAdversarioDto retrospectoAdversario = new RetrospectoAdversarioDto();
        utils.calculoContraAdversario(retrospectoAdversario, partidas, clubeId);

        return Optional.of(resultado);
    }

    public Optional<ConfrontoDiretoDto> buscarConfrontoDireto(Long clube1Id, Long clube2Id) {
        Optional<ClubeModel> clube1Optional = clubeRepository.findById(clube1Id);
        Optional<ClubeModel> clube2Optional = clubeRepository.findById(clube2Id);

        if (clube1Optional.isEmpty() || clube2Optional.isEmpty()) {
            return Optional.empty();
        }

        ClubeModel clube1 = clube1Optional.get();
        ClubeModel clube2 = clube2Optional.get();

        Specification<PartidaModel> specification = RetrospectoSpecification.confrontoDireto(clube1Id, clube2Id);
        List<PartidaModel> partidas = partidaRepository.findAll(specification);


        ConfrontoDiretoDto confronto = new ConfrontoDiretoDto(clube1.getClubeNome(), clube2.getClubeNome());
        confronto.setPartidas(partidas);

        if (!partidas.isEmpty()) {
            utils.calcularRetrospectosConfrontoDireto(confronto, partidas, clube1Id, clube2Id);
        }
        return Optional.of(confronto);
    }
}
