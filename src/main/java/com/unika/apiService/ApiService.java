package com.unika.apiService;

import okhttp3.*;

import java.io.IOException;
import java.io.Serializable;

public class ApiService implements Serializable {

    private static final long serialVersionUID = 6231254149020498841L;

    public Response conectarApiPOST(String jsonBody, String url) throws IOException {
        return conectarApiSendingBody(jsonBody, url, "POST");
    }

    public Response conectarApiPUT(String jsonBody, String url) throws IOException {
        return conectarApiSendingBody(jsonBody, url, "PUT");
    }


    public Response conectarApiGET(String url) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url(url).get().build();
        return client.newCall(request).execute();
    }

    public Response conectarApiDELETE(String url) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder().url(url).delete().build();
        return client.newCall(request).execute();
    }

    private Response conectarApiSendingBody(String jsonBody, String url, String metodo) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .method(metodo, body)
                .addHeader("Content-Type", "application/json")
                .build();
        return client.newCall(request).execute();
    }
}
