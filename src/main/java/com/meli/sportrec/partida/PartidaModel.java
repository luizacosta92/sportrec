package com.meli.sportrec.partida;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_Partidas")
public class PartidaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String clubeMandante;

    @NotBlank
    private String clubeVisitante;

    @NotBlank
    private String estadio;

    @NotBlank
    private LocalDateTime dataDaPartida;

    @NotBlank
    private String resultadoPartida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(String clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public String getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(String clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    public LocalDateTime getDataDaPartida() {
        return dataDaPartida;
    }

    public void setDataDaPartida(LocalDateTime dataDaPartida) {
        this.dataDaPartida = dataDaPartida;
    }

    public String getResultadoPartida() {
        return resultadoPartida;
    }

    public void setResultadoPartida(String resultadoPartida) {
        this.resultadoPartida = resultadoPartida;
    }
}

