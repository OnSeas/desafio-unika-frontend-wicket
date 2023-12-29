package com.unika.apiService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unika.model.Endereco;
import com.unika.model.Monitorador;
import okhttp3.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class EnderecoApi {
    final String apiUrl = "http://localhost:8080/endereco";

    ApiService apiService = new ApiService();
    Gson gson = new Gson();

    public Endereco buscarEnderecoPrincipal(Long idMonitor) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/principal/" + idMonitor);

        if (response.isSuccessful()){
            assert response.body() != null;
            String json = response.body().string();
            return gson.fromJson(json, Endereco.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public List<Endereco> listarEnderecos(Long idMonitor) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/listar/" + idMonitor);

        if (response.isSuccessful()){
            assert response.body() != null;
            String json = response.body().string();
            Type listType = new TypeToken<List<Endereco>>() {}.getType();
            return gson.fromJson(json, listType);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public Endereco editarEndereco(Long idEndereco, Endereco endereco) throws IOException {
        Response response = apiService.conectarApiPUT(
                gson.toJson(endereco, Endereco.class),
                apiUrl + "/editar/" + idEndereco
        );

        if (response.isSuccessful()){
            assert response.body() != null;
            String json = response.body().string();
            return gson.fromJson(json, Endereco.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public String deletarEndereco(Long idEndereco) throws IOException {
        Response response = apiService.conectarApiDELETE(
                apiUrl + "/deletar/" + idEndereco
        );

        if (response.isSuccessful()){
            assert response.body() != null;
            return response.body().string();
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public String tornarPrincipal(Long idEndereco, Long idMonitor) throws IOException {

        System.out.println(apiUrl + "/" + idEndereco + "/tornar-principal/" + idMonitor);

        Response response = apiService.conectarApiPUT(
                gson.toJson(""),
                apiUrl + "/" + idEndereco + "/tornar-principal/" + idMonitor
        );

        System.out.println("Teste2");

        if (response.isSuccessful()){
            assert response.body() != null;
            return response.body().string();
        } else {
            throw new RuntimeException(response.body().string());
        }
    }
}
