package com.unika.model.apiService;

import com.unika.model.Endereco;
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
        String res = response.body().string();
        response.close();

        if(response.isSuccessful()){
            return converterDados.obterDados(res, Monitorador.class);
        } else {
            throw new RuntimeException(res);
        }
    }

    public List<Monitorador> listarMonitoradores() throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/listar");

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterLista(res, Monitorador.class);
        } else {
            return new ArrayList<>();
        }
    }

    public Monitorador buscarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/" + id);

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterDados(res, Monitorador.class);
        } else {
            throw new RuntimeException(res);
        }
    }

    public Monitorador atualizarMonitorador(Monitorador monitorador, Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                converterDados.obterJason(monitorador),
                apiUrl + "/atualizar/" + id
        );

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterDados(res, Monitorador.class);
        } else {
            throw new RuntimeException(res);
        }
    }

    public String deletarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiDELETE(
                apiUrl + "/deletar/" + id
        );

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return res;
        } else {
            throw new RuntimeException(res);
        }
    }

    // Adcionar Endereço
    public String adcionarEndereco(Long idMonitorador, Endereco endereco) throws IOException {
        Response response = apiService.conectarApiPOST(
                converterDados.obterJason(endereco),
                apiUrl + "/" + idMonitorador + "/endereco"
        );

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return res;
        } else {
            throw new RuntimeException(res);
        }
    }

    // Funcionalidades
    public String ativarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                "",
                apiUrl + "/ativar/" + id
        );

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return res;
        } else {
            throw new RuntimeException(res);
        }
    }

    public String desativarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                "",
                apiUrl + "/desativar/" + id
        );

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return res;
        } else {
            throw new RuntimeException(res);
        }
    }


    // Buscas
    public List<Monitorador> buscarMonitoradoresPorEmail(String email) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/email/" + email);

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterLista(res, Monitorador.class);
        } else {
            return listarMonitoradores();
        }
    }

    public List<Monitorador> buscarMonitoradorPorCpf(String cpf) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/cpf/" + cpf);

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterLista(res, Monitorador.class);
        } else {
            return listarMonitoradores();
        }
    }

    public List<Monitorador> buscarMonitoradorPorCnpj(String cnpj) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/buscar/cnpj/" + cnpj);

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterLista(res, Monitorador.class);
        } else {
            return listarMonitoradores();
        }
    }
}