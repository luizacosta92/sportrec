package com.meli.sportrec.retrospecto;

import com.meli.sportrec.partida.PartidaModel;

import java.util.List;

public class ConfrontoDiretoDto {
    private String clube1Nome;
    private String clube2Nome;
    private List<PartidaModel> partidas;
    private RetrospectoConfrontoDto retrospectoClube1;
    private RetrospectoConfrontoDto retrospectoClube2;

    public ConfrontoDiretoDto() {}

    public ConfrontoDiretoDto(String clube1Nome, String clube2Nome) {
        this.clube1Nome = clube1Nome;
        this.clube2Nome = clube2Nome;
        this.retrospectoClube1 = new RetrospectoConfrontoDto(clube1Nome);
        this.retrospectoClube2 = new RetrospectoConfrontoDto(clube2Nome);
    }


    public String getClube1Nome() { return clube1Nome; }
    public void setClube1Nome(String clube1Nome) { this.clube1Nome = clube1Nome; }

    public String getClube2Nome() { return clube2Nome; }
    public void setClube2Nome(String clube2Nome) { this.clube2Nome = clube2Nome; }

    public List<PartidaModel> getPartidas() { return partidas; }
    public void setPartidas(List<PartidaModel> partidas) { this.partidas = partidas; }

    public RetrospectoConfrontoDto getRetrospectoClube1() {
        return retrospectoClube1;
    }

    public void setRetrospectoClube1(RetrospectoConfrontoDto retrospectoClube1) {
        this.retrospectoClube1 = retrospectoClube1;
    }

    public RetrospectoConfrontoDto getRetrospectoClube2() {
        return retrospectoClube2;
    }

    public void setRetrospectoClube2(RetrospectoConfrontoDto retrospectoClube2) {
        this.retrospectoClube2 = retrospectoClube2;
    }
}

