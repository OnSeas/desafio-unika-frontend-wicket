package com.unika.model.apiService;

import com.unika.model.Endereco;
import com.unika.model.Filtro;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.converters.ConverterDados;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.wicket.markup.html.form.Form;

import javax.swing.text.Utilities;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Response response = apiService.conectarApiGETParams(
                apiUrl + "/filtro",
                filtro
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
        ResponseBody res = response.body();

        if (response.isSuccessful()){
            Monitorador monitorador = buscarMonitorador(idMonitorador);

            byte[] bytes = res.bytes();
            String filePathName;
            if(monitorador.getTipoPessoa() == TipoPessoa.PESSOA_FISICA) filePathName = "C:\\Projetos\\zArquivos\\relatorios\\relatorio-" + monitorador.getNome() +".pdf";
            else filePathName = "C:\\Projetos\\zArquivos\\relatorios\\relatorio-" + monitorador.getRazaoSocial() +".pdf";

            File file = new File(filePathName);
            FileUtils.writeByteArrayToFile(file, bytes);
            return file;
        } else {
            String erro = res.string();
            response.close();
            throw new RuntimeException(erro);
        }
    }

    public File gerarRelatorio() throws IOException {
        Response response = apiService.conectarApiGET(apiUrl + "/report");

        assert response.body() != null;
        ResponseBody res = response.body();

        if (response.isSuccessful()){
            byte[] bytes = res.bytes();
            File file = new File("C:\\Projetos\\zArquivos\\relatorios\\relatorioGeral.pdf");
            FileUtils.writeByteArrayToFile(file, bytes);
            return file;
        } else {
            String erro = res.string();
            response.close();
            throw new RuntimeException(erro);
        }
    }

    public List<Monitorador> importarXLSX(File file) throws IOException {

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("application/vnd.ms-excel"), file))
                .addFormDataPart("some-field", "some-value")
                .build();

        Response response = apiService.conectarApiPOSTRequest(
                requestBody,
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
        ResponseBody res = response.body();

        if (response.isSuccessful()){
            byte[] bytes = res.bytes();
            File file = new File("C:\\Projetos\\zArquivos\\excel\\monitoradores-" + LocalDate.now() + ".xlsx");
            FileUtils.writeByteArrayToFile(file, bytes);
            return file;
        } else {
            String erro = res.string();
            response.close();
            throw new RuntimeException(erro);
        }
    }
}