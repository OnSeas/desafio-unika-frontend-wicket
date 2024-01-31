package com.unika.forms;

import com.unika.model.Endereco;
import com.unika.model.UF;
import com.unika.model.apiService.EnderecoApi;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.Arrays;

public class EnderecoForm extends Form<Endereco> {
    private static final long serialVersionUID = -5152812808250771426L;
    public Boolean submited;
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    EnderecoApi enderecoApi = new EnderecoApi();
    public Long idMonitorador;

    FeedbackPanel previousFeedbackPanel;

    public EnderecoForm(String id, Endereco endereco, Long idMonitorador, FeedbackPanel previousFeedbackPanel){
        super(id, new CompoundPropertyModel<>(endereco));
        submited = Boolean.FALSE;
        this.idMonitorador = idMonitorador; // Retirar ao enviar lista de endereços para update tbm;
        this.previousFeedbackPanel = previousFeedbackPanel;

        // Mensagens de erro
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMessage");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

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

        AjaxButton submitAjax = new AjaxButton("submitAjax") {
            private static final long serialVersionUID = 3333211378039191514L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    salvar(EnderecoForm.this.getModelObject());
                    submited = Boolean.TRUE;
                    if (EnderecoForm.this.getModelObject().getId() == null){
                        previousFeedbackPanel.success("Endereço cadastrado com sucesso!");
                    } else {
                        previousFeedbackPanel.success("Endereço atualizado com sucesso!");
                    }
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
        add(inputEndereco, inputNumero, inputCep, inputBairro, inputTelefone, inputCidade, dropEstado, checkBoxPrincipal, submitAjax);


        // Validações do formulário
        inputEndereco.setLabel(Model.of("Endereço")).setRequired(true).add(StringValidator.lengthBetween(3, 50)); // TODO unicode ç
        inputNumero.setLabel(Model.of("Número")).setRequired(true).add(StringValidator.maximumLength(5)); // TODO unicode ú
        inputCep.setLabel(Model.of("CEP")).setRequired(true).add(StringValidator.lengthBetween(8, 9));
        inputBairro.setLabel(Model.of("Bairro")).setRequired(true).add(StringValidator.lengthBetween(3, 20));
        inputTelefone.setLabel(Model.of("Telefone")).setRequired(true).add(StringValidator.lengthBetween(10, 14));
        inputCidade.setLabel(Model.of("Cidade")).setRequired(true).add(StringValidator.lengthBetween(3, 20));
        dropEstado.setLabel(Model.of("Estado")).setRequired(true);
    }

    // Metódo salvar usado para adcionar ou editar um endereço ao banco de dados
    private void salvar(Endereco endereco){
        try {
            if (idMonitorador != -1L){
                if(endereco.getId() == null){ // Criando novo endereço
                    monitoradorApi.adcionarEndereco(idMonitorador, endereco);
                    System.out.println("Salvou novo!");
                } else { // Editando um endereçoi existente
                    enderecoApi.editarEndereco(endereco.getId(), endereco);
                    System.out.println("Editou um existente!");
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
