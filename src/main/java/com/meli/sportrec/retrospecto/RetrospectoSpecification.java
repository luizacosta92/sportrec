package com.meli.sportrec.retrospecto;

import com.meli.sportrec.partida.PartidaModel;
import org.springframework.data.jpa.domain.Specification;

public class RetrospectoSpecification {
    public static Specification<PartidaModel> clubeParticipou(Long clubeId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("clubeMandante").get("id"), clubeId),
                    criteriaBuilder.equal(root.get("clubeVisitante").get("id"), clubeId)
            );
        };
    }

    public static Specification<PartidaModel> clubeMandante(Long clubeId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("clubeMandante").get("id"), clubeId);
        };
    }

    public static Specification<PartidaModel> clubeVisitante(Long clubeId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("clubeVisitante").get("id"), clubeId);
        };
    }

    public static Specification<PartidaModel> confrontoDireto(Long clube1Id, Long clube2Id) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("clubeMandante").get("id"), clube1Id),
                            criteriaBuilder.equal(root.get("clubeVisitante").get("id"), clube2Id)
                    ),
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("clubeMandante").get("id"), clube2Id),
                            criteriaBuilder.equal(root.get("clubeVisitante").get("id"), clube1Id)
                    )
            );
        };
    }


    public static Specification<PartidaModel> contraAdversario(Long clubeId, Long adversarioId){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("clubeMandante").get("id"), clubeId),
                            criteriaBuilder.equal(root.get("clubeVisitante").get("id"), adversarioId)),
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("clubeVisitante").get("id"), clubeId),
                            criteriaBuilder.equal(root.get("clubeMandante").get("id"), adversarioId)));
        };
    }

    public static Specification<PartidaModel> clubesAtivos(){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("clubeMandante").get("ativo"), true),
                    criteriaBuilder.equal(root.get("clubeVisitante").get("ativo"), true));
        };
    }

}
