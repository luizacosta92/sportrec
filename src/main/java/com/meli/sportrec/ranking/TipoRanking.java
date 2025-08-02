package com.meli.sportrec.ranking;

public enum TipoRanking {
    PONTOS ("pontos"),
    GOLS("gols"),
    VITORIAS("vitorias"),
    TOTAL_JOGOS("totalJogos");

    private final String valor;

    TipoRanking(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return this.valor;
    }
}
