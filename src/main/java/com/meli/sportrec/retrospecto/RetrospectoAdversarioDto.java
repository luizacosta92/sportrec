package com.meli.sportrec.retrospecto;

public class RetrospectoAdversarioDto {
    private String adversarioNome;
    private Integer totalJogos;
    private Integer vitorias;
    private Integer empates;
    private Integer derrotas;
    private Integer golsFeitos;
    private Integer golsSofridos;
    private Integer pontos;

    public RetrospectoAdversarioDto() {}

    public RetrospectoAdversarioDto(String adversarioNome) {
        this.adversarioNome = adversarioNome;
        this.totalJogos = 0;
        this.vitorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.golsFeitos = 0;
        this.golsSofridos = 0;
        this.pontos = 0;
    }

    public String getAdversarioNome() {
        return adversarioNome;
    }

    public void setAdversarioNome(String adversarioNome) {
        this.adversarioNome = adversarioNome;
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

    public Integer getGolsFeitos() {
        return golsFeitos;
    }

    public void setGolsFeitos(Integer golsFeitos) {
        this.golsFeitos = golsFeitos;
    }

    public Integer getGolsSofridos() {
        return golsSofridos;
    }

    public void setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }
}

