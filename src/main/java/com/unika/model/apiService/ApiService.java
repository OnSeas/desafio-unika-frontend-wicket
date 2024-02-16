package com.unika.model.apiService;


import okhttp3.*;

import java.io.IOException;
import java.io.Serializable;

public class ApiService  implements Serializable {
    private static final long serialVersionUID = 103112267540728378L;

    public Response conectarApiPOST(String jsonBody, String url) {
        return conectarApiSendingBody(jsonBody, url, "POST");
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
