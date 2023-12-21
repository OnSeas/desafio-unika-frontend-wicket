package com.unika.apiService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unika.model.Monitorador;
import com.unika.model.PessoaFisica;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MonitoradorApi {
    final String apiUrl = "http://localhost:8080/monitorador";

    public Monitorador cadastrarMonitorador(Monitorador monitorador) throws IOException {

        Response response = conectarApi(
                getJson(monitorador),
                "/cadastrar",
                "POST");

        if(response.isSuccessful()){
            assert response.body() != null;
            return getObject(response.body().string());
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

//    public List<Monitorador> listarMonitoradores() throws IOException {
//
//        Response response = conectarApiGet(
//                "/listar");
//
//        if(response.isSuccessful()){
//            Type listType = new TypeToken<List<Monitorador>>() {}.getType();
//
//            Gson gson = new Gson();
//            List<Monitorador> monitoradorList = gson.fromJson(response.body().string(), listType);
//            return monitoradorList;
//        } else {
//            throw new RuntimeException(response.body().string());
//        }
//    }

    // Conexões com a API
    private Response conectarApi(String jsonBody, String endpoint, String metodo) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(apiUrl + endpoint) // Muda em cada chamada
                .method(metodo, body) // Muda em cada chamada
                .addHeader("Content-Type", "application/json")
                .build();
        return client.newCall(request).execute();
    }

    private Response conectarApiGet(String endpoint) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url(apiUrl + endpoint).get().build();
        return client.newCall(request).execute();
    }

    private Response conectarApiDelete(String endpoint) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url(apiUrl + endpoint).delete().build();
        return client.newCall(request).execute();
    }

    // Conversores de Json pra Object

    private List<Monitorador> getObjects(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, List.class);
    }
    private Monitorador getObject(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, PessoaFisica.class);
    }

    private String getJson(Monitorador monitorador) {
        Gson gson = new Gson();
        return gson.toJson(monitorador, PessoaFisica.class);
    }

}
