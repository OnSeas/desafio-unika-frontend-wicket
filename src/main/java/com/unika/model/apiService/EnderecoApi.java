package com.unika.model.apiService;

import com.unika.model.Endereco;
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
}
