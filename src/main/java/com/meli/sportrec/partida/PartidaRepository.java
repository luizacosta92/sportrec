package com.meli.sportrec.partida;

import com.meli.sportrec.clube.ClubeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartidaRepository extends JpaRepository<PartidaModel, Long> {

    @Query(""" 
        SELECT partida FROM PartidaModel partida
        LEFT JOIN FETCH partida.clubeMandante
        LEFT JOIN  FETCH partida.clubeVisitante
        LEFT JOIN FETCH partida.estadio
        WHERE partida.clubeMandante.id = :clubeId or partida.clubeVisitante.id = :clubeId
        ORDER BY partida.dataHoraPartida DESC 
        """)
    List<PartidaModel> findByClube(@Param("clubeId") Long clubeId); //buscar partida por clube

    @Query("""
        SELECT partida FROM PartidaModel partida 
        LEFT JOIN FETCH partida.clubeMandante 
        LEFT JOIN FETCH partida.clubeVisitante 
        LEFT JOIN FETCH partida.estadio
        WHERE partida.estadio.id = :estadioId
        ORDER BY partida.dataHoraPartida DESC
        """)
    List<PartidaModel> findByEstadio(@Param("estadioId") Long estadioId); //busca partida por estadio

    @Query("""
        SELECT partida FROM PartidaModel partida 
        WHERE partida.estadio.id = :estadioId 
        AND DATE(partida.dataHoraPartida) = DATE(:dataHoraPartida)
        AND (:excluirPartidaId IS NULL OR partida.id != :excluirPartidaId)
        """)
    List<PartidaModel> findConflitosEstadio(@Param("estadioId") Long estadioId,
                                            @Param("dataHoraPartida") LocalDateTime dataHoraPartida,
                                            @Param("excluirPartidaId") Long excluirPartidaId);
    @Query("""
        SELECT partida FROM PartidaModel partida 
        WHERE (partida.clubeMandante.id = :clubeId OR partida.clubeVisitante.id = :clubeId) 
        AND partida.dataHoraPartida BETWEEN :inicio AND :fim
        AND (:excluirPartidaId IS NULL OR partida.id != :excluirPartidaId)
        """)
    List<PartidaModel> findConflitoHorarioClube(@Param("clubeId") Long clubeId,
                                                 @Param("inicio") LocalDateTime inicio,
                                                 @Param("fim") LocalDateTime fim,
                                                 @Param("excluirPartidaId") Long excluirPartidaId);

    @Query("""
        SELECT partida FROM PartidaModel partida 
        LEFT JOIN FETCH partida.clubeMandante 
        LEFT JOIN FETCH partida.clubeVisitante 
        LEFT JOIN FETCH partida.estadio
        WHERE partida.id = :id
        """)
    Optional<PartidaModel> findByIdComDetalhes(@Param("id") Long id);


    @Query("""
        SELECT partida FROM PartidaModel partida 
        LEFT JOIN FETCH partida.clubeMandante
        LEFT JOIN FETCH partida.clubeVisitante 
        LEFT JOIN FETCH partida.estadio
        WHERE (:clubeId IS NULL OR partida.clubeMandante.id = :clubeId OR partida.clubeVisitante.id = :clubeId)
        AND (:estadioId IS NULL OR partida.estadio.id = :estadioId)
        """)
    Page<PartidaModel> findComFiltros(@Param("clubeId") Long clubeId,
                                       @Param("estadioId") Long estadioId,
                                       Pageable pageable);

    Optional<PartidaModel> findTopByClubeMandanteOrClubeVisitanteOrderByDataHoraPartidaAsc(ClubeModel clubeModelMandante, ClubeModel clubeModelVisitante);

}
