package com.unika.model.apiService;

import com.unika.model.Endereco;
import com.unika.model.Filtro;
import com.unika.model.Monitorador;
import com.unika.model.apiService.converters.ConverterDados;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.wicket.request.resource.ByteArrayResource;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class MonitoradorApi implements Serializable {
    @Serial
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
            throw new RuntimeException(res);
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

    public Monitorador deletarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiDELETE(
                apiUrl + "/deletar/" + id
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

    // Adcionar Endereço
    public Endereco adcionarEndereco(Long idMonitorador, Endereco endereco) throws IOException {
        Response response = apiService.conectarApiPOST(
                converterDados.obterJason(endereco),
                apiUrl + "/" + idMonitorador + "/endereco"
        );

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterDados(res, Endereco.class);
        } else {
            throw new RuntimeException(res);
        }
    }

    // Funcionalidades
    public Monitorador ativarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                "",
                apiUrl + "/ativar/" + id
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

    public Monitorador desativarMonitorador(Long id) throws IOException {
        Response response = apiService.conectarApiPUT(
                "",
                apiUrl + "/desativar/" + id
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

    // Filtro
    public List<Monitorador> filtrarMonitorares(Filtro filtro) throws IOException {
        Response response = apiService.conectarApiPOST(
                converterDados.obterJason(filtro),
                apiUrl + "/filtro"
        );

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterLista(res, Monitorador.class);
        } else {
            throw new RuntimeException(res);
        }
    }

    // Exports e Imports
    public File gerarRelatorio(Long idMonitorador) throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/report/" + idMonitorador);

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterDados(res, File.class);
        } else {
            throw new RuntimeException(res);
        }
    }

    public File gerarRelatorio() throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/report");

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            byte[] byteArray = converterDados.obterDados(res, byte[].class);
            System.out.println(Arrays.toString(byteArray));
            File file = new File("C:\\Projetos\\zArquivo\\relatorios\\relaorioGeral.pdf");
            FileUtils.writeByteArrayToFile(file, byteArray);
            return file;
        } else {
            throw new RuntimeException(res);
        }
    }

    public List<Monitorador> importarXLSX(File file) throws IOException { // TODO arrumar aqui quando mudar no backend
        Response response = apiService.conectarApiPOST(
                converterDados.obterJason(file),
                apiUrl + "/import"
        );

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterLista(res, Monitorador.class);
        } else {
            throw new RuntimeException(res);
        }
    }

    public File exportarXLSX() throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/export/xlsx");

        assert response.body() != null;
        String res = response.body().string();
        response.close();

        if (response.isSuccessful()){
            return converterDados.obterDados(res, File.class);
        } else {
            throw new RuntimeException(res);
        }
    }
}