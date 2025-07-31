package com.meli.sportrec.retrospecto;

public class RetrospectoGeralDto {
    private String clubeNome;
    private Integer totalJogos;
    private Integer vitorias;
    private Integer empates;
    private Integer derrotas;
    private Integer golsFeitos;
    private Integer golsSofridos;
    private Integer pontos;
    private Integer saldoGols;

    public RetrospectoGeralDto() {}
    public RetrospectoGeralDto(String clubeNome) {
        this.clubeNome = clubeNome;
        this.totalJogos = 0;
        this.vitorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.golsFeitos = 0;
        this.golsSofridos = 0;
        this.pontos = 0;
        this.saldoGols = 0;
    }

    public String getClubeNome() {
        return clubeNome;
    }

    public void setClubeNome(String clubeNome) {
        this.clubeNome = clubeNome;
    }

    public Integer getTotalJogos() {
        return totalJogos;
    }

    public void setTotalJogos(Integer totalJogos) {
        this.totalJogos = totalJogos;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public void setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
        calcularPontos();
    }

    public Integer getEmpates() {
        return empates;
    }

    public void setEmpates(Integer empates) {
        this.empates = empates;
        calcularPontos();
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
    }

    public Integer getGolsFeitos() {
        return golsFeitos;

    }

    public void setGolsFeitos(Integer golsFeitos) {
        this.golsFeitos = golsFeitos;
        calcularSaldoGols();
    }

    public Integer getGolsSofridos() {
        return golsSofridos;
    }

    public void setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
        calcularSaldoGols();
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public Integer getSaldoGols() {
        return saldoGols;
    }

    public void setSaldoGols(Integer saldoGols) {
        this.saldoGols = saldoGols;
    }

    private void calcularPontos(){
        this.pontos = (this.vitorias * 3) +(this.empates *1);
    }

    private void calcularSaldoGols() {
        this.saldoGols = this.saldoGols - this.golsSofridos;
    }
}
