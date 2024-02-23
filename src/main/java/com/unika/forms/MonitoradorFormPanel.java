package com.unika.forms;

import com.unika.Panels.EnderecoListPanel;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.io.Serial;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MonitoradorFormPanel extends Panel {
    @Serial
    private static final long serialVersionUID = -8284617783524484913L;
    final FeedbackPanel feedbackPanel;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();
    private final EnderecoListPanel enderecoListPanel;

    public MonitoradorFormPanel(String id, Monitorador monitorador, FeedbackPanel feedbackPanel) {
        super(id);
        this.feedbackPanel = feedbackPanel;

        Form<Monitorador> monitoradorForm = getForm(monitorador);
        add(monitoradorForm);

        // Criando um novo monitorador
        if(monitorador.getId() == null) {
            add(new Label("monitoradorFormTitle", Model.of("Cadastrar um novo monitorador")));
        }
        // Editando um monitorador existente
        else {
            add(new Label("monitoradorFormTitle", Model.of("Editar info do monitorador")));

            WebMarkupContainer wmc; // Já mostrar os inputs corretos.
            if (monitorador.getTipoPessoa().equals(TipoPessoa.PESSOA_FISICA)){
                wmc = (WebMarkupContainer) monitoradorForm.get("pfWMC");
            } else{
                wmc = (WebMarkupContainer) monitoradorForm.get("pjWMC");
            }
            wmc.setVisible(true);
        }
        enderecoListPanel = new EnderecoListPanel("endListPanel", monitorador.getId(), feedbackPanel);
        add(enderecoListPanel);
    }

    Form<Monitorador> getForm(Monitorador monitorador){
        Form<Monitorador> monitoradorForm = new Form<>("formMonitorador", new CompoundPropertyModel<>(monitorador));
        RadioGroup<TipoPessoa> radioTipoPessoa = new RadioGroup<>("tipoPessoa");
        radioTipoPessoa.add(new Radio<>("pf", new Model<>(TipoPessoa.PESSOA_FISICA)));
        radioTipoPessoa.add(new Radio<>("pj", new Model<>(TipoPessoa.PESSOA_JURIDICA)));
        EmailTextField inputEmail = new EmailTextField("email");
        TextField<Date> inputDataNascimento = new DateTextField("dataNascimento", "yyyy-MM-dd");
        monitoradorForm.add(radioTipoPessoa, inputEmail, inputDataNascimento);


        // INPUTS DE PESSOA FÍSICA
        TextField<String> inputNome = new TextField<>("nome");
        TextField<String> inputCpf = new TextField<>("cpf");
        TextField<String> inputRg = new TextField<>("rg");

        WebMarkupContainer pfWMC = new WebMarkupContainer("pfWMC"); // Container a ser mostrado ao cadastrar uma pessoa física.
        pfWMC.setVisible(false);
        pfWMC.setOutputMarkupId(true);
        pfWMC.setOutputMarkupPlaceholderTag(true);
        pfWMC.add(inputNome, inputCpf, inputRg);


        // INPUTS DE PESSOA JURÍDICA
        TextField<String> inputRazaoSocial = new TextField<>("razaoSocial");
        TextField<String> inputCnpj = new TextField<>("cnpj");
        TextField<String> inputInscricaoEstadual = new TextField<>("inscricaoEstadual");

        WebMarkupContainer pjWMC = new WebMarkupContainer("pjWMC"); // Container a ser mostrado ao cadastrar uma pessoa Júridica
        pjWMC.setVisible(false);
        pjWMC.setOutputMarkupId(true);
        pjWMC.setOutputMarkupPlaceholderTag(true);
        pjWMC.add(inputRazaoSocial, inputCnpj, inputInscricaoEstadual);

        // Setando todos os input com OutputMarkupId true
        List<Component> inputList = Arrays.asList(inputEmail, inputDataNascimento, inputCpf, inputNome, inputRg, inputRazaoSocial, inputCnpj, inputInscricaoEstadual);
        inputList.forEach(component -> component.setOutputMarkupId(true));

        // -- -- -- Validações em campos
        radioTipoPessoa.setLabel(Model.of("Tipo de pessoa")).setRequired(true);
        inputEmail.setLabel(Model.of("Email do contato")).setRequired(true).add(EmailAddressValidator.getInstance());
        inputDataNascimento.setLabel(Model.of("Data nascimento")).setRequired(true);
        inputCpf.setLabel(Model.of("CPF")).setRequired(true).add(StringValidator.exactLength(14));
        inputNome.setLabel(Model.of("Nome")).setRequired(true).add(StringValidator.lengthBetween(3, 50));
        inputRg.setLabel(Model.of("RG")).setRequired(true).add(StringValidator.exactLength(12));
        inputRazaoSocial.setLabel(Model.of("Razão Social")).setRequired(true).add(StringValidator.lengthBetween(3, 50));
        inputCnpj.setLabel(Model.of("CNPJ")).setRequired(true).add(StringValidator.exactLength(18));
        inputInscricaoEstadual.setLabel(Model.of("Inscrição Estadual")).setRequired(true).add(StringValidator.maximumLength(18));

        // Mascáras de inputs
        inputCpf.add(new AjaxEventBehavior("focus") {
            @Serial
            private static final long serialVersionUID = -8024661883244296260L;
            @Override
            protected void onEvent(AjaxRequestTarget target) { // TODO mudar todos para não precisar usar o ajax
                target.appendJavaScript(
                        "$(document).ready(function (){\n" +
                        "   $('#cpfInput').mask('000.000.000-00');\n" +
                        "});");
            }
        });

        inputRg.add(new AjaxEventBehavior("focus") {
            @Serial
            private static final long serialVersionUID = -984263498898264817L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                target.appendJavaScript(
                        "$(document).ready(function (){\n" +
                        "   $('#inputRgForm').mask('00.000.000-0');\n" +
                        "});");
            }
        });

        inputCnpj.add(new AjaxEventBehavior("focus") {
            @Serial
            private static final long serialVersionUID = 3145077170075078630L;

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                target.appendJavaScript(
                        "$(document).ready(function (){\n" +
                        "   $('#inputCnpjForm').mask('00.000.000/0000-00')\n" +
                        "});");
            }
        });


        // Botão de submit
        monitoradorForm.add(new AjaxButton("ajaxSubmit", monitoradorForm) {
            @Serial
            private static final long serialVersionUID = -8806215908629462715L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    salvar(monitoradorForm.getModelObject());
                    if (monitoradorForm.getModelObject().getId() == null){
                        success("Monitorador Cadastrado com Sucesso!");
                    } else {
                        success("Monitorador Atualizado com Sucesso!");
                    }
                    target.add(feedbackPanel);
                } catch (Exception e){
                    error(e.getMessage());
                    target.add(feedbackPanel);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });


        // Mostrar os inputs corretos ao selecionar o tipo de pessoa
        radioTipoPessoa.get("pf").add(new AjaxEventBehavior("focus") {
            @Serial
            private static final long serialVersionUID = 7722774318433529522L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                pfWMC.setVisible(true);
                pjWMC.setVisible(false);
                target.add(pfWMC, pjWMC);
            }
        });
        radioTipoPessoa.get("pj").add(new AjaxEventBehavior("focus") {
            @Serial
            private static final long serialVersionUID = 7722774318433529522L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                pfWMC.setVisible(false);
                pjWMC.setVisible(true);
                target.add(pfWMC, pjWMC);
            }
        });
        monitoradorForm.add(pfWMC, pjWMC);
        return monitoradorForm;
    }

    private void salvar(Monitorador monitorador){
        try {
            monitorador.setEnderecoList(enderecoListPanel.getEnderecoList());

            System.out.println(monitorador);

            if(monitorador.getId() == null){
                monitoradorApi.cadastrarMonitorador(monitorador); // Novo monitorador
            } else {
                monitoradorApi.atualizarMonitorador(monitorador, monitorador.getId()); // Editar monitorador
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
