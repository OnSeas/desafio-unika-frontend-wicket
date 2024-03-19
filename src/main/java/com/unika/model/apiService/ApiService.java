package com.unika.model.apiService;


import com.unika.model.Filtro;
import okhttp3.*;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ApiService  implements Serializable {
    @Serial
    private static final long serialVersionUID = 103112267540728378L;

    public Response conectarApiPOST(String jsonBody, String url) {
        return conectarApiSendingBody(jsonBody, url, "POST");
    }

    public Response conectarApiPOSTRequest(RequestBody requestBody, String url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possivel estabelecer uma conexão com o backend");
        }
    }

    public Response conectarApiPUT(String jsonBody, String url) {
        return conectarApiSendingBody(jsonBody, url, "PUT");
    }


    public Response conectarApiGET(String url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url(url).get().build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possivel estabelecer uma conexão com o backend");
        }
    }

    public Response conectarApiGETParams(String url, Filtro filtro) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

        Map<String, Object> params = new HashMap<>();
        if (filtro.getBusca() != null) params.put("busca", filtro.getBusca());
        if (filtro.getTipoBusca() != null) params.put("tipoBusca", filtro.getTipoBusca().getValue());
        if (filtro.getSoAtivados() != null) params.put("soAtivados", filtro.getSoAtivados());
        if (filtro.getPessoaFisica() != null) params.put("pessoaFisica", filtro.getPessoaFisica());
        if (filtro.getPessoaJuridica() != null) params.put("pessoaJuridica", filtro.getPessoaJuridica());

        if (!params.isEmpty())
            for(Map.Entry<String, Object> param : params.entrySet()) {
                httpBuilder.addQueryParameter(param.getKey(),param.getValue().toString());
            }

        Request request = new Request.Builder().url(httpBuilder.build()).get().build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possivel estabelecer uma conexão com o backend");
        }
    }

    public Response conectarApiDELETE(String url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url(url).delete().build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possivel estabelecer uma conexão com o backend");
        }
    }

    private Response conectarApiSendingBody(String jsonBody, String url, String metodo) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .method(metodo, body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possivel estabelecer uma conexão com o backend");
        }
    }
}
