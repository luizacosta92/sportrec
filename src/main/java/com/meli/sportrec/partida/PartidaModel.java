package com.meli.sportrec.partida;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meli.sportrec.clube.ClubeModel;
import com.meli.sportrec.estadio.EstadioModel;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_Partidas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PartidaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //MUITAS partidas podem acontecer com UM clube
    @JoinColumn(name = "clube_mandante_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PARTIDA_CLUBE_MANDANTE"))
    private ClubeModel clubeMandante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clube_visitante_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PARTIDA_CLUBE_VISITANTE"))
    private ClubeModel clubeVisitante;

    @ManyToOne(fetch = FetchType.LAZY) //MUITAS partidas podem acontecer em UM estadio
    @JoinColumn(name = "estadio_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PARTIDA_ESTADIO"))
    private EstadioModel estadio;

    @Column(name = "data_hora_partida", nullable = false)
    private LocalDateTime dataHoraPartida;

    @Column(name = "gols_mandante", nullable = false)
    private Integer golsMandante;

    @Column(name = "gols_visitante", nullable = false)
    private Integer golsVisitante;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClubeModel getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(ClubeModel clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public ClubeModel getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(ClubeModel clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public EstadioModel getEstadio() {
        return estadio;
    }

    public void setEstadio(EstadioModel estadio) {
        this.estadio = estadio;
    }

    public LocalDateTime getDataHoraPartida() {
        return dataHoraPartida;
    }

    public void setDataHoraPartida(LocalDateTime dataHoraPartida) {
        this.dataHoraPartida = dataHoraPartida;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }
}