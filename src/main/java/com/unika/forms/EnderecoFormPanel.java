package com.unika.forms;

import com.unika.model.Endereco;
import com.unika.model.UF;
import com.unika.model.apiService.EnderecoApi;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.List;

public class EnderecoFormPanel extends Panel {
    @Serial
    private static final long serialVersionUID = -7243543132733295966L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();
    final EnderecoApi enderecoApi = new EnderecoApi();
    final FeedbackPanel feedbackPanel;

    public EnderecoFormPanel(String id, Endereco endereco, Long idMonitorador, FeedbackPanel feedbackPanel) {
        super(id);
        this.feedbackPanel = feedbackPanel;

        // Criando um endereço
        if(endereco.getId() == null){
            add(new Label("enderecoFormTitle", Model.of("Criar novo endereço")));
        }
        // Editando um endereço
        else {
            add(new Label("enderecoFormTitle", Model.of("Editar endereço")));
        }

        Form<Endereco> enderecoForm = new Form<>("enderecoForm", new CompoundPropertyModel<>(endereco));

        TextField<String> inputEndereco = new TextField<>("endereco");
        TextField<String> inputNumero = new TextField<>("numero");
        TextField<String> inputCep = new TextField<>("cep");
        TextField<String> inputBairro = new TextField<>("bairro");
        TextField<String> inputTelefone = new TextField<>("telefone");
        TextField<String> inputCidade = new TextField<>("cidade");

        DropDownChoice<UF> dropEstado = new DropDownChoice<>("estado",
                Arrays.asList(UF.values()),
                new ChoiceRenderer<>(){
                    @Serial
                    private static final long serialVersionUID = 2254888871382690067L;
                    @Override
                    public Object getDisplayValue(UF estado){
                        return estado.getSigla();
                    }
                });

        AjaxButton submitAjax = new AjaxButton("submitAjax", enderecoForm) {
            @Serial
            private static final long serialVersionUID = 3333211378039191514L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    System.out.println(enderecoForm.getModelObject());

                    Endereco enderecobd = salvar(enderecoForm.getModelObject(), idMonitorador);
                    enderecoForm.getModelObject().setId(enderecobd.getId());

                    if (enderecoForm.getModelObject().getId() == null){
                        success("Endereço cadastrado com sucesso!");
                    } else {
                        success("Endereço atualizado com sucesso!");
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


        List<Component> inputsList = Arrays.asList(inputEndereco, inputNumero, inputCep, inputBairro, inputTelefone, inputCidade, dropEstado, submitAjax);
        inputsList.forEach(component -> {
            component.setOutputMarkupId(true);
            enderecoForm.add(component);
        });


        // Validações do formulário
        inputEndereco.setLabel(Model.of("Endereço")).setRequired(true).add(StringValidator.lengthBetween(3, 50));
        inputNumero.setLabel(Model.of("Número")).setRequired(true).add(StringValidator.maximumLength(5));
        inputCep.setLabel(Model.of("CEP")).setRequired(true).add(StringValidator.exactLength(9));
        inputBairro.setLabel(Model.of("Bairro")).setRequired(true).add(StringValidator.lengthBetween(3, 20));
        inputTelefone.setLabel(Model.of("Telefone")).setRequired(true).add(StringValidator.lengthBetween(13, 14));
        inputCidade.setLabel(Model.of("Cidade")).setRequired(true).add(StringValidator.lengthBetween(3, 20));
        dropEstado.setLabel(Model.of("Estado")).setRequired(true);


        // Ao digitar o CEP
        inputCep.add(new AjaxEventBehavior("blur") {
            @Serial
            private static final long serialVersionUID = 8995325121134414023L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                try {
                    Endereco enderecoCep = enderecoApi.buscarEnderecopeloCep(inputCep.getInput());
                    inputEndereco.setModelObject(enderecoCep.getEndereco());
                    inputBairro.setModelObject(enderecoCep.getBairro());
                    inputCidade.setModelObject(enderecoCep.getCidade());
                    dropEstado.setModelObject(enderecoCep.getEstado());
                    target.add(inputEndereco, inputBairro, inputCidade, dropEstado);
                } catch (IOException e) {
                    feedbackPanel.error("Erro ao buscar pelo CEP no sistema! obs. erro de conexão com o backend");
                } catch (Exception e){
                    feedbackPanel.error(e.getMessage());
                    target.add(feedbackPanel);
                }
            }
        });

        add(enderecoForm);
    }

    // Metódo salvar usado para adcionar ou editar um endereço ao banco de dados
    private Endereco salvar(Endereco endereco, Long idMonitorador){
        try {
            // Não chega no backend quando vai criar um novo monitorador, então validando aqui.
            if (!endereco.getCidade().matches("[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]*")) throw  new RuntimeException("Cidade não pode conter números ou caracteres especiais!");
            if (!endereco.getBairro().matches("[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]*")) throw  new RuntimeException("Bairro não pode conter números ou caracteres especiais!");

            if (idMonitorador!=null){
                if(endereco.getId() == null){ // Criando novo endereço
                    return monitoradorApi.adcionarEndereco(idMonitorador, endereco);
                } else { // Editando um endereço existente
                    return enderecoApi.editarEndereco(endereco.getId(), endereco);
                }
            } return endereco;
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
