package com.unika;

import com.unika.Panels.MonitoradorFormPanel;
import com.unika.Panels.MonitoradorListPanel;
import com.unika.forms.MonitoradorForm;
import com.unika.model.Monitorador;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class ControleMonitoradores extends HomePage{
    private static final long serialVersionUID = 2178207601281324056L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();
    final FeedbackPanel feedbackPanel = new FeedbackPanel("FeedBackMessages");
    final ModalWindow modalWindow = new ModalWindow("modaw");
    final WebMarkupContainer listWMC = new WebMarkupContainer("listWMC");

    public ControleMonitoradores(PageParameters parameters) throws IOException {
        super(parameters);

        // TODO Ainda não retorna as mensagens de sucesso
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        // Modal que é usado em todos pop-ups
        modalWindow.setCookieName("modalWindow-1");
        add(modalWindow);

        // WMC que envolve a lista já inciando a lista geral
        listWMC.setOutputMarkupId(true);
        putPageableList(monitoradorApi.listarMonitoradores());
        add(listWMC);

        add(new Label("ControlLabel", Model.of("Controle de monitoradores")));

        // Botão de criar novo monitorador
        add( new AjaxLink<Void>("formNovoMonitorador") {
            private static final long serialVersionUID = -387605849215267697L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.setContent(new MonitoradorFormPanel(ModalWindow.CONTENT_ID, new MonitoradorForm("formMonitorador",
                        new Monitorador(),
                        feedbackPanel)));
                modalWindow.show(target);
            }
        });

        // Função quando alguma modal window é fechada.
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
            private static final long serialVersionUID = 1L;
            @Override
            public void onClose(AjaxRequestTarget target){
                try {
                    putPageableList(monitoradorApi.listarMonitoradores());
                    target.add(feedbackPanel);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                target.add(listWMC);

                // TODO Adcionar a mensagem de sucesso!
            }
        });
    }

    // --- METÓDOS INTERNOS ---
    // PEGA O FORM
    private Form<String> getFormPesquisa(){
        Form<String> pesquisarForm = new Form<>("pesquisarForm");
        add(pesquisarForm);

        TextField<String> inputPesquisa = new TextField<>("inputPesquisa", new Model<>());
        List<String> tipos = Arrays.asList("email", "cpf", "cnpj");
        DropDownChoice<String> inputTipo = new DropDownChoice<>("inputTipo", new Model<>(), tipos);
        AjaxButton ajaxButton = new AjaxButton("searchButton", pesquisarForm) {
            private static final long serialVersionUID = -3533755636036242218L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    putPageableList(getListaMonitoradores(inputTipo.getModelObject(), inputPesquisa.getModelObject()));
                    target.add(listWMC);
                    target.add(feedbackPanel);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        };
        pesquisarForm.add(inputPesquisa, inputTipo, ajaxButton);
        inputTipo.setLabel(Model.of("O tipo da pesquisa")).setRequired(true);
        return pesquisarForm;
    }

    // ADCIONA A LISTA COM PAGING
    private void putPageableList(List<Monitorador> monitoradores) throws IOException {
        // remove se já existir alguma coisa
        listWMC.removeAll();

        // Adiciona o form na lista (só aparecer se tiver algum valor
        Form<String> searchForm = getFormPesquisa();
        searchForm.setVisible(!monitoradores.isEmpty());
        listWMC.add(searchForm);

        // Adciona a lista paginada (Panel)
        listWMC.add(new MonitoradorListPanel("monitoradorListPanel", monitoradores));
    }

    // Para o filtro
    private List<Monitorador> getListaMonitoradores(String tipo, String busca) throws IOException { // TODO lidar com exceções
        switch (tipo){
            case "email":
                return monitoradorApi.buscarMonitoradoresPorEmail(busca);
            case "cpf":
                return monitoradorApi.buscarMonitoradorPorCpf(busca);
            case "cnpj":
                return monitoradorApi.buscarMonitoradorPorCnpj(busca);
            default: throw new InvalidPropertiesFormatException("Tipo de pesquisa inválida!");
        }
    }
}
