package com.unika.model;

import java.io.Serializable;

public class Endereco implements Serializable {
    private static final long serialVersionUID = 192787022611302892L;

    private Long id;
    private String endereco;
    private String numero;
    private String cep;
    private String bairro;
    private String telefone;
    private String cidade;
    private UF estado;
    private Boolean principal;
}
