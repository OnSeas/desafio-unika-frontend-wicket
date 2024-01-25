package com.unika.forms;

import com.unika.model.Endereco;
import com.unika.model.UF;
import com.unika.model.apiService.EnderecoApi;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import java.io.IOException;
import java.util.Arrays;

public class FormularioEndereco extends WebPage {
    private static final long serialVersionUID = -1829624338524145673L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    EnderecoApi enderecoApi = new EnderecoApi();
    final Long idMonitorador; // Monitorador a qual o endereço pertence. Por padrão quando é -1 quer dizer que é um monitorador que está sendo criado.
    FormularioMonitorador formularioMonitorador;

    // Criar novo Form
    public FormularioEndereco(FormularioMonitorador formularioMonitorador, Long idMontorador){
        this.idMonitorador = idMontorador;

        this.formularioMonitorador = formularioMonitorador;

        add(new Label("enderecoFormTitle", Model.of("Criar novo endereço"))); // TODO Unicode
        add(getForm());

        addToList(new Endereco());
    }

    // Editar um form
    public FormularioEndereco(Long idMontorador, Long idEndereco) throws IOException {
        this.idMonitorador = idMontorador;

        add(new Label("enderecoFormTitle", Model.of("Editar endereço"))); // TODO Unicode
        Form<Endereco> enderecoForm = getForm();
        enderecoForm.setModelObject(enderecoApi.buscarEndereco(idEndereco));
        add(enderecoForm);
    }

    // Construir o Form
    private Form<Endereco> getForm(){
        // Mensagens de erro
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMessage");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        // Construção do formulário
        Form<Endereco> enderecoForm = new Form<>("enderecoForm", new CompoundPropertyModel<>(new Endereco()));

        TextArea<String> inputEndereco = new TextArea<>("endereco");
        TextField<String> inputNumero = new TextField<>("numero");
        TextField<String> inputCep = new TextField<>("cep");
        TextField<String> inputBairro = new TextField<>("bairro");
        TextField<String> inputTelefone = new TextField<>("telefone");
        TextField<String> inputCidade = new TextField<>("cidade");

        DropDownChoice<UF> dropEstado = new DropDownChoice<>("estado",
                Arrays.asList(UF.values()),
                new ChoiceRenderer<UF>(){
                    private static final long serialVersionUID = 2254888871382690067L;
                    @Override
                    public Object getDisplayValue(UF estado){
                        return estado.getSigla();
                    }
                });

        CheckBox checkBoxPrincipal = new CheckBox("principal");

        AjaxButton submitAjax = new AjaxButton("submitAjax", enderecoForm) {
            private static final long serialVersionUID = 3333211378039191514L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    salvar(enderecoForm.getModelObject());
                    ModalWindow.closeCurrent(target);
                } catch (Exception e){
                    feedbackPanel.error(e.getMessage());
                    target.add(feedbackPanel);
                }
            }

            // Função para que o feedbackPanel retorne os erros.
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        };
        enderecoForm.add(inputEndereco, inputNumero, inputCep, inputBairro, inputTelefone, inputCidade, dropEstado, checkBoxPrincipal, submitAjax);


        // Validações do formulário
        inputEndereco.setLabel(Model.of("Endereço")).setRequired(true).add(StringValidator.lengthBetween(3, 50)); // TODO unicode ç
        inputNumero.setLabel(Model.of("Número")).setRequired(true).add(StringValidator.maximumLength(5)); // TODO unicode ú
        inputCep.setLabel(Model.of("CEP")).setRequired(true).add(StringValidator.lengthBetween(8, 9));
        inputBairro.setLabel(Model.of("Bairro")).setRequired(true).add(StringValidator.lengthBetween(3, 20));
        inputTelefone.setLabel(Model.of("Telefone")).setRequired(true).add(StringValidator.lengthBetween(10, 14));
        inputCidade.setLabel(Model.of("Cidade")).setRequired(true).add(StringValidator.lengthBetween(3, 20));
        dropEstado.setLabel(Model.of("Estado")).setRequired(true);

        return enderecoForm;
    }

    // Metódo salvar usado para adcionar ou editar um endereço ao banco de dados
    private void salvar(Endereco endereco){
        try {
            if(endereco.getId() == null){ // Criando novo endereço
                if (idMonitorador == -1L){ // Monitorador está sendo criado
                    addToList(endereco);
                } else { // Monitorador já existe
                    monitoradorApi.adcionarEndereco(idMonitorador, endereco);
                    System.out.println("Salvou novo!");
                }
            } else { // Editando um endereçoi existente
                enderecoApi.editarEndereco(endereco.getId(), endereco);
                System.out.println("Editou um existente!");
            }
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    // TODO deve adcionar um endereço na lista Formulario monitorador
    private void addToList(Endereco endereco){
        System.out.println(endereco);
        this.formularioMonitorador.enderecoList.add(endereco);
    }
}
