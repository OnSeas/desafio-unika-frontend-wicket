package com.unika.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Monitorador implements Serializable {
    private static final long serialVersionUID = -6007073673347568740L;

    private Long id;
    private TipoPessoa tipoPessoa;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataNascimento;
    private String inscricaoEstadual;
    private List<Endereco> enderecoList;
    private String cpf;
    private String nome;
    private String rg;
    private String cnpj;
    private String razaoSocial;
    private Boolean ativo;

    @Override
    public String toString() {
        return "Monitorador{" +
                "id=" + id +
                ", tipoPessoa=" + tipoPessoa +
                ", email='" + email + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", inscricaoEstadual='" + inscricaoEstadual + '\'' +
                ", enderecoList=" + enderecoList +
                ", cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", rg='" + rg + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                '}';
    }
}
