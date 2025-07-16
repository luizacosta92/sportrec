package com.meli.sportrec.clube;

public enum UfBrasil {
    AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, MT, MS, MG, PA, PB, PR, PE, PI, RJ, RN, RS, RO, RR, SC, SP, SE, TO;

    public static boolean ufValida(String uf){
        if(uf == null || uf.isEmpty()){
            return false;
        }
        for (UfBrasil estado: UfBrasil.values()){
            if (estado.toString().equalsIgnoreCase(uf)){
                return true;
            }
        }
        return false;
    }
}
