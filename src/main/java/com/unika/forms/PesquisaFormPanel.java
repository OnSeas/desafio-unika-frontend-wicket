package com.unika.forms;

import com.unika.model.Monitorador;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.io.IOException;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class PesquisaFormPanel extends Panel {
    private static final long serialVersionUID = -4704292867141672385L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();

    public PesquisaFormPanel(String id, ListView<Monitorador> monitoradorListView, List<FeedbackPanel> feedbackPanels) {
        super(id);

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
                    monitoradorListView.setList(getListaMonitoradores(inputTipo.getModelObject(), inputPesquisa.getModelObject()));
                    target.add(monitoradorListView.getParent());
                    success("Pesquisa realizada com sucesso!");
                    feedbackPanels.forEach(target::add);
                } catch (Exception e) {
                    error(e.getMessage());
                    feedbackPanels.forEach(target::add);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                feedbackPanels.forEach(target::add);
            }
        };
        pesquisarForm.add(inputPesquisa, inputTipo, ajaxButton);
    }

    // Para o filtro
    private List<Monitorador> getListaMonitoradores(String tipo, String busca) throws IOException {
        if (tipo == null){
            throw new InvalidPropertiesFormatException("Tipo de pesquisa inválida!");
        }

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
