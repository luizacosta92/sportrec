package com.meli.sportrec.estadio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadioRepository extends JpaRepository<EstadioModel, Long> {

    boolean existsByEstadioNomeIgnoreCase(String estadioNome);

    boolean existsByEstadioNomeIgnoreCaseAndIdNot(String estadioNome, Long id);

    @Query("SELECT e FROM EstadioModel e ORDER BY e.estadioNome")
    Page<EstadioModel> findAllOrderByNome(Pageable pageable);
}