package com.unika.model;

import java.io.Serializable;
import java.util.List;

public class Monitorador implements Serializable {
    private static final long serialVersionUID = -6007073673347568740L;

    private Long id;
    private TipoPessoa tipoPessoa;
    private String email;
    private String dataNascimento;
    private String inscricaoEstadual;
    private List<Endereco> enderecoList;
    private String cpf;
    private String nome;
    private String rg;
    private String cnpj;
    private String razaoSocial;
}
