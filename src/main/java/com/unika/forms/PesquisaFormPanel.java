package com.unika.forms;

import com.unika.model.Filtro;
import com.unika.model.Monitorador;
import com.unika.model.UF;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
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

        Form<Filtro> pesquisarForm = new Form<>("pesquisarForm", new CompoundPropertyModel<>(new Filtro()));
        add(pesquisarForm);

        DropDownChoice<Filtro.TipoBusca> inputTipo = new DropDownChoice<>("tipoBusca",
                Arrays.asList(Filtro.TipoBusca.values()),
                new ChoiceRenderer<>(){
                    @Serial
                    private static final long serialVersionUID = 2254888871382690067L;
                    @Override
                    public Object getDisplayValue(Filtro.TipoBusca tipo){
                        return tipo.getLabel();
                    }
                });
        inputTipo.setOutputMarkupId(true);
        inputTipo.setOutputMarkupPlaceholderTag(true);
        inputTipo.setVisible(false);

        WebMarkupContainer checkboxes = new WebMarkupContainer("checkboxes");
        checkboxes.setOutputMarkupId(true);
        checkboxes.setOutputMarkupPlaceholderTag(true);
        checkboxes.setVisible(false);

        CheckBox checkBoxPF = new CheckBox("pessoaFisica");
        CheckBox checkBoxPJ = new CheckBox("pessoaJuridica");
        CheckBox checkBoxAtivados = new CheckBox("soAtivados");

        checkboxes.add(checkBoxPF, checkBoxPJ, checkBoxAtivados);

        TextField<String> inputPesquisa = new TextField<>("busca");

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
                    checkboxes.setVisible(false);
                    pesquisarForm.setModelObject(new Filtro());
                    target.add(monitoradorListView.getParent(), this, inputTipo, inputPesquisa, checkboxes);
                } catch (Exception e) {
                    error(e.getMessage());
                    feedbackPanel.forEach(target::add);
                }
            }
        };
        clearButton.setOutputMarkupId(true);
        clearButton.setOutputMarkupPlaceholderTag(true);
        clearButton.setVisible(false);

        inputPesquisa.add(new AjaxEventBehavior("focus") {
            @Serial
            private static final long serialVersionUID = 5061484065988631638L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                if(!inputTipo.isVisible()){
                    inputTipo.setVisible(true);
                    checkboxes.setVisible(true);
                    clearButton.setVisible(true);
                    target.add(inputTipo, checkboxes, clearButton);
                }
            }
        });

        AjaxButton searchButton = new AjaxButton("searchButton", pesquisarForm) {
            @Serial
            private static final long serialVersionUID = -3533755636036242218L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    Filtro filtro = pesquisarForm.getModelObject();
                    System.out.println(filtro);
                    if (!pesquisaIsValid(filtro)){
                        error("Pesquisa vazia!");
                    }
                    else {
                        List<Monitorador> monitoradorList = getListaMonitoradores(filtro);
                        if(!monitoradorList.isEmpty()){
                            monitoradorListView.setList(monitoradorList);
                            success("Pesquisa realizada com sucesso!");
                            target.add(monitoradorListView.getParent());
                        } else {
                            error("Não foram encontrados monitoradores na pesquisa!");
                        }
                    }
                    target.add(feedbackPanel);
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

        pesquisarForm.add(inputPesquisa, inputTipo, searchButton, checkboxes, clearButton);
    }

    // Para o filtro
    private List<Monitorador> getListaMonitoradores(Filtro filtro) throws IOException {
        return monitoradorApi.filtrarMonitorares(filtro);
    }

    private boolean pesquisaIsValid(Filtro filtro){
        if ((filtro.getBusca() == null || filtro.getBusca().isBlank() || filtro.getTipoBusca() == null)
                && (filtro.getSoAtivados() == null || Boolean.FALSE.equals(filtro.getSoAtivados()))
                && (filtro.getPessoaFisica() == null || Boolean.FALSE.equals(filtro.getPessoaFisica()))
                && (filtro.getPessoaJuridica() == null || Boolean.FALSE.equals(filtro.getPessoaJuridica()))) return false;
        return true;
    }
}
