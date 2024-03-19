package com.unika.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class Filtro implements Serializable {
    @Serial
    private static final long serialVersionUID = -330575548289940219L;

    private String busca;
    private TipoBusca tipoBusca;
    private Boolean pessoaFisica;
    private Boolean pessoaJuridica;
    private Boolean soAtivados;

    @Getter
    public enum TipoBusca{
        CPF("cpf", 0),
        CNPJ("cnpj", 1),
        EMAIL("email", 2);

        private final String label;
        private final Integer value;
        TipoBusca(String label, Integer value){
            this.label = label;
            this.value = value;
        }

    }

    @Override
    public String toString() {
        return "Filtro{" +
                "busca='" + busca + '\'' +
                ", tipoBusca=" + tipoBusca +
                ", pessoaFisica=" + pessoaFisica +
                ", pessoaJuridica=" + pessoaJuridica +
                ", soAtivados=" + soAtivados +
                '}';
    }
}
