package com.unika.model;

public enum TipoPessoa {
    PESSOA_FISICA(0, "Pessoa Física"),
    PESSOA_JURIDICA(1, "Pessoa Juridica");

    final int codigo;
    final String label;

    TipoPessoa(int codigo, String label){
        this.codigo = codigo;
        this.label = label;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getLabel(){return label;}
}
