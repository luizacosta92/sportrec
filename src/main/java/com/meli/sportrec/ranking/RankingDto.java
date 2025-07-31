package com.meli.sportrec.ranking;

public class RankingDto {
    private String clubeNome;
    private Integer posicao;
    private Integer pontos;
    private Integer totalGols;
    private Integer vitorias;
    private Integer empates;
    private Integer derrotas;
    private Integer golsSofridos;
    private Integer saldoGols;

    public RankingDto() {}
    public RankingDto(String clubeNome) {
        this.clubeNome = clubeNome;
        this.pontos = 0;
        this.saldoGols = 0;
        this.vitorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.golsSofridos = 0;
        this.totalGols = 0;
    }

    public String getClubeNome() {
        return clubeNome;
    }

    public void setClubeNome(String clubeNome) {
        this.clubeNome = clubeNome;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public Integer getTotalGols() {
        return totalGols;
    }

    public void setTotalGols(Integer totalGols) {
        this.totalGols = totalGols;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public void setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
    }

    public Integer getEmpates() {
        return empates;
    }

    public void setEmpates(Integer empates) {
        this.empates = empates;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
    }

    public Integer getGolsSofridos() {
        return golsSofridos;
    }

    public void setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
    }

    public Integer getSaldoGols() {
        return saldoGols;
    }

    public void setSaldoGols(Integer saldoGols) {
        this.saldoGols = saldoGols;
    }
}
