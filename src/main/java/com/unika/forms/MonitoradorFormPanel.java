package com.unika.forms;

import com.unika.Panels.EnderecoListPanel;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.EnderecoApi;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MonitoradorFormPanel extends Panel {
    private static final long serialVersionUID = -8284617783524484913L;
    public WebMarkupContainer listaEnderecoWMC = new WebMarkupContainer("listaEnderecoWMC");
    public EnderecoApi enderecoApi = new EnderecoApi();
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    public FeedbackPanel feedbackMessageError;
    public FeedbackPanel feedbackMessageSuccess; // Mostra os erros no form
    List<FeedbackPanel> feedbackPanels = new ArrayList<>();
    public MonitoradorFormPanel(String id, Monitorador monitorador) {
        super(id);

        //Configurando os FeedbackPanels
        feedbackMessageSuccess = new FeedbackPanel("FeedbackMessageSuccess"){
            private static final long serialVersionUID = -1547023497652558705L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                setVisible(anyMessage(250));
            }
        };
        feedbackMessageSuccess.add(new AjaxEventBehavior("click") {
            private static final long serialVersionUID = 7722774318433529522L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                feedbackMessageSuccess.setVisible(false);
                target.add(feedbackMessageSuccess);
            }
        });
        add(feedbackMessageSuccess);
        feedbackMessageError = new FeedbackPanel("FeedbackMessageError"){
            private static final long serialVersionUID = 5148472465033548020L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                setVisible(anyErrorMessage());
            }
        };
        feedbackMessageError.add(new AjaxEventBehavior("click") {
            private static final long serialVersionUID = -8000879227247712657L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                feedbackMessageError.setVisible(false);
                target.add(feedbackMessageError);
            }
        });
        add(feedbackMessageError);

        feedbackPanels.add(feedbackMessageError);
        feedbackPanels.add(feedbackMessageSuccess);

        Form<Monitorador> monitoradorForm = getForm(monitorador);
        add(monitoradorForm);

        // Criando um novo monitorador
        if(monitorador.getId() == null) {
            add(new Label("monitoradorFormTitle", Model.of("Cadastrar um novo monitorador")));
            listaEnderecoWMC.add(new EmptyPanel("enderecoPanel"));
        }
        // Editando um monitorador existente
        else {
            add(new Label("monitoradorFormTitle", Model.of("Editar info do monitorador")));

            WebMarkupContainer wmc;
            if (monitorador.getTipoPessoa().equals(TipoPessoa.PESSOA_FISICA)){
                wmc = (WebMarkupContainer) monitoradorForm.get("pfWMC");
            } else{
                wmc = (WebMarkupContainer) monitoradorForm.get("pjWMC");
            }
            wmc.setVisible(true);

            addListaEndereco(monitorador.getId());
        }

        // Configuração da lista de Endereços
        listaEnderecoWMC.setOutputMarkupId(true);
        listaEnderecoWMC.setOutputMarkupPlaceholderTag(true);
        add(listaEnderecoWMC);
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
        inputCpf.add(new AjaxEventBehavior("focusin") {
            private static final long serialVersionUID = -8024661883244296260L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                target.appendJavaScript(
                        "$(document).ready(function (){\n" +
                        "   $('#cpfInput').mask('000.000.000-00');\n" +
                        "});");
            }
        });

        inputRg.add(new AjaxEventBehavior("focusin") {
            private static final long serialVersionUID = -984263498898264817L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                target.appendJavaScript(
                        "$(document).ready(function (){\n" +
                        "   $('#inputRgForm').mask('00.000.000-0');\n" +
                        "});");
            }
        });

        inputCnpj.add(new AjaxEventBehavior("focusin") {
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
                    ModalWindow.closeCurrent(target);
                } catch (Exception e){
                    feedbackMessageError.error(e.getMessage());
                    target.add(feedbackMessageError);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackMessageError);
            }
        });


        // Mostrar os inputs corretos ao selecionar o tipo de pessoa
        radioTipoPessoa.get("pf").add(new AjaxEventBehavior("focusin") {
            private static final long serialVersionUID = 7722774318433529522L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                pfWMC.setVisible(true);
                pjWMC.setVisible(false);
                target.add(pfWMC, pjWMC);
            }
        });
        radioTipoPessoa.get("pj").add(new AjaxEventBehavior("focusin") {
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

    private void addListaEndereco(Long idMonitorador){
        listaEnderecoWMC.removeAll();
        try {
            listaEnderecoWMC.add(new EnderecoListPanel("enderecoPanel",
                    enderecoApi.listarEnderecos(idMonitorador),
                    idMonitorador,
                    feedbackPanels));
        } catch (Exception e){
            error("Não foi possivel carregar a lista de endereços. Erro: " + e.getMessage());
        }

    }

    private void salvar(Monitorador monitorador){
        try {
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
