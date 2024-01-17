package com.unika;

import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.io.IOException;
import java.util.Date;

public class FormularioMonitorador extends WebPage {
    private static final long serialVersionUID = 8415273463108359061L;

    MonitoradorApi monitoradorApi = new MonitoradorApi();

    ListarMonitoradores listarMonitoradores;

    FormularioMonitorador(ListarMonitoradores listarMonitoradores, ModalWindow modalWindow){
        add(new Label("monitoradorForm", Model.of("Cadastrar um novo monitorador")));
        this.listarMonitoradores = listarMonitoradores;

        Form<Monitorador> monitoradorForm = getForm();
        add(monitoradorForm);
    }

    FormularioMonitorador(ListarMonitoradores listarMonitoradores, ModalWindow modalWindow, Long idMonitorador) throws IOException {
        add(new Label("monitoradorForm", Model.of("Editar info do monitorador")));
        this.listarMonitoradores = listarMonitoradores;

        Form<Monitorador> monitoradorForm = getForm();
        Monitorador monitorador = monitoradorApi.buscarMonitorador(idMonitorador);
        monitoradorForm.setModelObject(monitorador);
        add(monitoradorForm);
    }

    private Form<Monitorador> getForm(){ // TODO SPLIT IN MORE METHODS
        Form<Monitorador> monitoradorForm = new Form<Monitorador>("formMonitorador", new CompoundPropertyModel<>(new Monitorador()));

        RadioGroup<TipoPessoa> radioTipoPessoa = new RadioGroup<>("tipoPessoa");
        radioTipoPessoa.add(new Radio<>("pf", new Model<>(TipoPessoa.PESSOA_FISICA)));
        radioTipoPessoa.add(new Radio<>("pj", new Model<>(TipoPessoa.PESSOA_JURIDICA)));
        EmailTextField inputEmail = new EmailTextField("email");
        TextField<Date> inputDataNascimento = new DateTextField("dataNascimento", "yyyy-MM-dd"); // TODO não vem preenchido
        monitoradorForm.add(radioTipoPessoa, inputEmail, inputDataNascimento);

        TextField<String> inputNome = new TextField<>("nome");
        TextField<String> inputCpf = new TextField<>("cpf");
        TextField<String> inputRg = new TextField<>("rg");

        WebMarkupContainer pfWMC = new WebMarkupContainer("pfWMC");
        pfWMC.setVisible(false);
        pfWMC.setOutputMarkupId(true);
        pfWMC.setOutputMarkupPlaceholderTag(true);
        pfWMC.add(inputNome, inputCpf, inputRg);

        TextField<String> inputRazaoSocial = new TextField<>("razaoSocial");
        TextField<String> inputCnpj = new TextField<>("cnpj");
        TextField<String> inputInscricaoEstadual = new TextField<>("inscricaoEstadual");

        WebMarkupContainer pjWMC = new WebMarkupContainer("pjWMC");
        pjWMC.setVisible(false);
        pjWMC.setOutputMarkupId(true);
        pjWMC.setOutputMarkupPlaceholderTag(true);
        pjWMC.add(inputRazaoSocial, inputCnpj, inputInscricaoEstadual);


        // -- -- -- Validações em campos
        radioTipoPessoa.setLabel(Model.of("Tipo de pessoa")).setRequired(true);
        inputEmail.setLabel(Model.of("Email do contato")).setRequired(true).add(EmailAddressValidator.getInstance());
        inputDataNascimento.setLabel(Model.of("Data nascimento")).setRequired(true);
        inputCpf.setLabel(Model.of("CPF")).setRequired(true).add(StringValidator.lengthBetween(11, 14));
        inputNome.setLabel(Model.of("Nome")).setRequired(true).add(StringValidator.lengthBetween(3, 200));
        inputRg.setLabel(Model.of("RG")).setRequired(true).add(StringValidator.lengthBetween(9, 12));
        inputRazaoSocial.setLabel(Model.of("Razão Social")).setRequired(true).add(StringValidator.lengthBetween(3, 200));
        inputCnpj.setLabel(Model.of("CNPJ")).setRequired(true).add(StringValidator.lengthBetween(14, 18));
        inputInscricaoEstadual.setLabel(Model.of("Inscrição Estadual")).setRequired(true).add(StringValidator.maximumLength(18));

        FeedbackPanel feedbackPanel = new FeedbackPanel("FeedbackMessage");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        monitoradorForm.add(new AjaxButton("ajaxSubmit") {
            private static final long serialVersionUID = -8806215908629462715L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
                try {
                    Monitorador monitorador = monitoradorForm.getModelObject();
                    System.out.println("Monitorador submetido: " + monitorador);
                    salvar(monitorador);
                    listarMonitoradores.mostrarMsg("Monitorador Salvo com sucesso!");
                    ModalWindow.closeCurrent(target);
                } catch (Exception e){
                    feedbackPanel.info(e.getMessage());
                    target.add(feedbackPanel);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });

        AjaxLink<Void> showPf = new AjaxLink<Void>("showPf") {
            private static final long serialVersionUID = -7872773929920444908L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                pfWMC.setVisible(true);
                pjWMC.setVisible(false);
                target.add(pfWMC, pjWMC);
                System.out.println("Mostrar inputs de Pessoa Física!");
            }
        };
        AjaxLink<Void> showPj = new AjaxLink<Void>("showPj") {
            private static final long serialVersionUID = -7468546216784505534L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                pfWMC.setVisible(false);
                pjWMC.setVisible(true);
                target.add(pfWMC, pjWMC);
                System.out.println("Mostrar inputs de Pessoa Jurídica!");
            }
        };

        radioTipoPessoa.add(showPf, showPj);

        monitoradorForm.add(pfWMC, pjWMC);
        return monitoradorForm;
    }

    private void salvar(Monitorador monitorador){
        try {
            if(monitorador.getId() == null){
                monitoradorApi.cadastrarMonitorador(monitorador);
            } else {
                monitoradorApi.atualizarMonitorador(monitorador, monitorador.getId());
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
