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

    public RetrospectoAdversarioDto(){
        inicializarValores();
    }
    public RetrospectoAdversarioDto(String adversarioNome) {
        inicializarValores();
        this.adversarioNome = adversarioNome;
    }
    private void inicializarValores() {
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
        this.vitorias = vitorias != null ? vitorias : 0;
        calcularPontos();
    }

    public Integer getEmpates() {
        return empates;
    }

    public void setEmpates(Integer empates) {
        this.empates = empates != null ? empates : 0;
        calcularPontos();
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas != null ? derrotas : 0;
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

    private void calcularPontos() {
        int vitoriasValue = this.vitorias != null ? this.vitorias : 0;
        int empatesValue = this.empates != null ? this.empates : 0;
        this.pontos = (vitoriasValue * 3) + (empatesValue * 1);
    }
}

