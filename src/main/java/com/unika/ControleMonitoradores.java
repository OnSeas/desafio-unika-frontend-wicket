package com.unika;

import com.unika.Panels.MonitoradorFormPanel;
import com.unika.Panels.MonitoradorListPanel;
import com.unika.forms.ImportFormPanel;
import com.unika.forms.MonitoradorForm;
import com.unika.model.Monitorador;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxEventBehavior;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class ControleMonitoradores extends HomePage{
    private static final long serialVersionUID = 2178207601281324056L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();
    final ModalWindow modalWindow = new ModalWindow("modaw");
    final WebMarkupContainer listWMC = new WebMarkupContainer("listWMC");
    FeedbackPanel feedbackPanelSuccess;
    FeedbackPanel feedbackPanelError;
    List<FeedbackPanel> feedbackPanels = new ArrayList<>();

    public ControleMonitoradores(PageParameters parameters) throws IOException {
        super(parameters);


        // FEEDBACK PANELS CONFIG
        feedbackPanelSuccess = new FeedbackPanel("feedbackPanelSuccess"){
            private static final long serialVersionUID = 8923533799419175857L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                setVisible(anyMessage(250)); // 250 é o level de success FeedbackMessage
            }
        };
        feedbackPanelSuccess.add(new AjaxEventBehavior("click") {
            private static final long serialVersionUID = 3239147858404127676L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                feedbackPanelSuccess.setVisible(false);
                target.add(feedbackPanelSuccess);
            }
        });
        add(feedbackPanelSuccess);

        feedbackPanelError = new FeedbackPanel("feedbackPanelError"){
            private static final long serialVersionUID = -706855642723453811L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                setVisible(anyErrorMessage());
            }
        };
        feedbackPanelError.add(new AjaxEventBehavior("click") {
            private static final long serialVersionUID = 4186326921821554339L;

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                feedbackPanelError.setVisible(false);
                target.add(feedbackPanelError);
            }
        });
        add(feedbackPanelError);

        feedbackPanels.add(feedbackPanelSuccess);
        feedbackPanels.add(feedbackPanelError);


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
                        new Monitorador())));
                modalWindow.show(target);
            }
        });

        add(new AjaxLink<Void>("importarMonitoradores") {
            private static final long serialVersionUID = -7139346449600703225L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.setContent(new ImportFormPanel(modalWindow.getContentId()));
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
                    feedbackPanels.forEach(target::add); // Lista com feedbackPanel success e error
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                target.add(listWMC);
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
                    target.add(feedbackPanelError);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanelError);
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
        listWMC.add(new MonitoradorListPanel("monitoradorListPanel", monitoradores, feedbackPanels));
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
