package com.unika.forms;

import com.unika.model.Monitorador;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class PesquisaFormPanel extends Panel {
    @Serial
    private static final long serialVersionUID = -4704292867141672385L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();

    public PesquisaFormPanel(String id, ListView<Monitorador> monitoradorListView, FeedbackPanel feedbackPanel) {
        super(id);

        Form<String> pesquisarForm = new Form<>("pesquisarForm");
        add(pesquisarForm);

        List<String> tipos = Arrays.asList("email", "cpf", "cnpj");
        DropDownChoice<String> inputTipo = new DropDownChoice<>("inputTipo", new Model<>(), tipos);
        inputTipo.setOutputMarkupId(true);
        inputTipo.setOutputMarkupPlaceholderTag(true);
        inputTipo.setVisible(false);

        TextField<String> inputPesquisa = new TextField<>("inputPesquisa", new Model<>());
        inputPesquisa.add(new AjaxEventBehavior("focus") {
            @Serial
            private static final long serialVersionUID = 5061484065988631638L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                if(!inputTipo.isVisible()){
                    inputTipo.setVisible(true);
                    target.add(inputTipo);
                }
            }
        });

        AjaxLink<Void> clearButton = new AjaxLink<>("clearButton") {
            @Serial
            private static final long serialVersionUID = -1080532818105319443L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                try {
                    monitoradorListView.setList(monitoradorApi.listarMonitoradores());
                    success("Mostrando todos os monitoradores!");
                    target.add(feedbackPanel);
                    this.setVisible(false);
                    inputTipo.setVisible(false);
                    inputPesquisa.setModelObject(null);
                    target.add(monitoradorListView.getParent(), this, inputTipo, inputPesquisa);
                } catch (Exception e) {
                    error(e.getMessage());
                    feedbackPanel.forEach(target::add);
                }
            }
        };
        clearButton.setOutputMarkupId(true);
        clearButton.setOutputMarkupPlaceholderTag(true);
        clearButton.setVisible(false);

        AjaxButton searchButton = new AjaxButton("searchButton", pesquisarForm) {
            @Serial
            private static final long serialVersionUID = -3533755636036242218L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    monitoradorListView.setList(getListaMonitoradores(inputTipo.getModelObject(), inputPesquisa.getModelObject()));
                    success("Pesquisa realizada com sucesso!");
                    target.add(feedbackPanel);
                    clearButton.setVisible(true);
                    target.add(monitoradorListView.getParent(), clearButton);
                } catch (Exception e) {
                    error(e.getMessage());
                    target.add(feedbackPanel);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }
        };

        pesquisarForm.add(inputPesquisa, inputTipo, searchButton, clearButton);
    }

    // Para o filtro
    private List<Monitorador> getListaMonitoradores(String tipo, String busca) throws IOException {
        if (tipo == null){
            throw new InvalidPropertiesFormatException("Tipo de pesquisa inválida! É necessário inserir uma pesquisa.");
        }

        return switch (tipo) {
            case "email" -> monitoradorApi.buscarMonitoradoresPorEmail(busca);
            case "cpf" -> monitoradorApi.buscarMonitoradorPorCpf(busca);
            case "cnpj" -> monitoradorApi.buscarMonitoradorPorCnpj(busca);
            default -> throw new InvalidPropertiesFormatException("Tipo de pesquisa inválida!");
        };
    }
}
