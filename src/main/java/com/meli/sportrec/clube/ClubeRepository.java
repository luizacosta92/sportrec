package com.meli.sportrec.clube;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubeRepository extends JpaRepository<ClubeModel, Long> {

    //JPQL
    @Query("SELECT clube FROM ClubeModel clube WHERE " +
            "(:clubeNome IS NULL OR LOWER(clube.clubeNome) LIKE LOWER(CONCAT('%', :clubeNome, '%'))) AND " +
            "(:estado IS NULL OR clube.estado = :estado) AND " +
            "(:ativo IS NULL OR clube.ativo = :ativo)")
    Page<ClubeModel> findByFiltros(@Param("clubeNome") String clubeNome,
                                   @Param("estado") String estado,
                                   @Param("ativo") Boolean ativo,
                                   Pageable pageable);

    boolean existsByClubeNomeIgnoreCaseAndEstado(String clubeNome, String estado);

    boolean existsByClubeNomeIgnoreCaseAndEstadoAndIdNot(String clubeNome, String estado, Long id);

}
