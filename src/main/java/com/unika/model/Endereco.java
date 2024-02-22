package com.unika.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Endereco implements Serializable {
    @Serial
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
    private Long monitoradorId;

    @Override
    public String toString() {
        return "Endereco{" +
                "id=" + id +
                ", endereco='" + endereco + '\'' +
                ", numero='" + numero + '\'' +
                ", cep='" + cep + '\'' +
                ", bairro='" + bairro + '\'' +
                ", telefone='" + telefone + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado=" + estado +
                ", principal=" + principal +
                ", monitoradorId=" + monitoradorId +
                '}';
    }
}
