package com.unika.apiService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unika.model.Endereco;
import com.unika.model.Monitorador;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MonitoradorApi { //TODO lidar com exeções
    final String apiUrl = "http://localhost:8080/monitorador";

    ApiService apiService = new ApiService();
    Gson gson = new Gson();

    public Monitorador cadastrarMonitorador(Monitorador monitorador) throws IOException {
        Response response = apiService.conectarApiPOST(
                gson.toJson(monitorador, Monitorador.class),
                apiUrl + "/cadastrar"
        );

        if(response.isSuccessful()){
            assert response.body() != null;
            String json = response.body().string();
            return gson.fromJson(json, Monitorador.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public List<Monitorador> listarMonitoradores() throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/listar");

        if (response.isSuccessful()){
            assert response.body() != null;
            String json = response.body().string();
            Type listType = new TypeToken<List<Monitorador>>() {}.getType();
            return gson.fromJson(json, listType);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public Monitorador buscarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/" + id);

        if (response.isSuccessful()){
            assert response.body() != null;
            String json = response.body().string();
            return gson.fromJson(json, Monitorador.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public Monitorador atualizarMonitorador(Monitorador monitorador, Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                gson.toJson(monitorador, Monitorador.class),
                apiUrl + "/atualizar/" + id
        );

        if (response.isSuccessful()){
            assert response.body() != null;
            String json = response.body().string();
            return gson.fromJson(json, Monitorador.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public String deletarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiDELETE(
                apiUrl + "/deletar/" + id
        );

        if (response.isSuccessful()){
            assert response.body() != null;
            return response.body().string();
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public String adcionarEndereco(Long id, Endereco endereco) throws IOException {
        Response response = apiService.conectarApiPOST(
                gson.toJson(endereco, Endereco.class),
                apiUrl + "/" + id + "/endereco"
        );

        if (response.isSuccessful()){
            assert response.body() != null;
            return response.body().string();
        } else {
            throw new RuntimeException(response.body().string());
        }
    }


}
