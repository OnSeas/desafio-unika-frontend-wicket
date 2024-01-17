package com.unika.model.apiService;

import com.unika.model.Monitorador;
import com.unika.model.apiService.converters.ConverterDados;
import okhttp3.Response;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MonitoradorApi implements Serializable {
    private static final long serialVersionUID = -4705610385379890273L;
    final String apiUrl = "http://localhost:8080/monitorador";
    ApiService apiService = new ApiService();
    ConverterDados converterDados = new ConverterDados();

    public Monitorador cadastrarMonitorador(Monitorador monitorador) throws IOException {
        Response response = apiService.conectarApiPOST(
                converterDados.obterJason(monitorador),
                apiUrl + "/cadastrar"
        );

        assert response.body() != null;
        if(response.isSuccessful()){
            String json = response.body().string();
            return converterDados.obterDados(json, Monitorador.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public List<Monitorador> listarMonitoradores() throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/listar");

        assert response.body() != null;
        if (response.isSuccessful()){
            String json = response.body().string();
            return converterDados.obterLista(json, Monitorador.class);
        } else {
            return null;
        }
    }

    public Monitorador buscarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/" + id);

        assert response.body() != null;
        if (response.isSuccessful()){
            String json = response.body().string();
            return converterDados.obterDados(json, Monitorador.class);
        } else {
            return null;
        }
    }

    public Monitorador atualizarMonitorador(Monitorador monitorador, Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                converterDados.obterJason(monitorador),
                apiUrl + "/atualizar/" + id
        );

        assert response.body() != null;
        if (response.isSuccessful()){
            String json = response.body().string();
            return converterDados.obterDados(json, Monitorador.class);
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public String deletarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiDELETE(
                apiUrl + "/deletar/" + id
        );

        assert response.body() != null;
        if (response.isSuccessful()){
            return response.body().string();
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    // Funcionalidades
    public String ativarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                "",
                apiUrl + "/ativar/" + id
        );

        assert response.body() != null;
        if (response.isSuccessful()){
            return response.body().string();
        } else {
            throw new RuntimeException(response.body().string());
        }
    }

    public String desativarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                "",
                apiUrl + "/desativar/" + id
        );

        assert response.body() != null;
        if (response.isSuccessful()){
            return response.body().string();
        } else {
            throw new RuntimeException(response.body().string());
        }
    }


    // Buscas
    public List<Monitorador> buscarMonitoradoresPorEmail(String email) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/email/" + email);

        assert response.body() != null;
        if (response.isSuccessful()){
            String json = response.body().string();

            return converterDados.obterLista(json, Monitorador.class);
        } else {
            return listarMonitoradores();
        }
    }

    public List<Monitorador> buscarMonitoradorPorCpf(String cpf) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/cpf/" + cpf);

        assert response.body() != null;
        if (response.isSuccessful()){
            String json = response.body().string();

            return converterDados.obterLista(json, Monitorador.class);
        } else {
            return listarMonitoradores();
        }
    }

    public List<Monitorador> buscarMonitoradorPorCnpj(String cnpj) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/cnpj/" + cnpj);

        assert response.body() != null;
        if (response.isSuccessful()){
            String json = response.body().string();
            return converterDados.obterLista(json, Monitorador.class);
        } else {
            return listarMonitoradores();
        }
    }
}