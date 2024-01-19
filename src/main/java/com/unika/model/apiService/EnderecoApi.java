package com.unika.model.apiService;

import com.unika.model.Endereco;
import com.unika.model.Monitorador;
import com.unika.model.apiService.converters.ConverterDados;
import okhttp3.Response;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnderecoApi implements Serializable {
    private static final long serialVersionUID = 1716049832595534895L;
    final String apiUrl = "http://localhost:8080/endereco";
    ApiService apiService = new ApiService();
    ConverterDados converterDados = new ConverterDados();

    public Endereco buscarEndereco(Long id) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/" + id);

        assert response.body() != null;
        if (response.isSuccessful()){
            String json = response.body().string();
            return converterDados.obterDados(json, Endereco.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public List<Endereco> listarEnderecos(Long idMonitor) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/listar/" + idMonitor);

        if (response.isSuccessful()){
            assert response.body() != null;
            String json = response.body().string();
            return converterDados.obterLista(json, Endereco.class);
        } else {
            return new ArrayList<>();
        }
    }

    public Endereco editarEndereco(Long idEndereco, Endereco endereco) throws IOException {
        Response response = apiService.conectarApiPUT(
                converterDados.obterJason(endereco),
                apiUrl + "/editar/" + idEndereco
        );

        assert response.body() != null;
        if (response.isSuccessful()){
            String json = response.body().string();
            return converterDados.obterDados(json, Endereco.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public String deletarEndereco(Long idEndereco) throws IOException {
        Response response = apiService.conectarApiDELETE(
                apiUrl + "/deletar/" + idEndereco
        );

        assert response.body() != null;
        if (response.isSuccessful()){
            return response.body().string();
        } else {
            throw new RuntimeException(response.body().string());
        }
    }
}
