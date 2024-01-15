package com.unika;

import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import java.io.IOException;
import java.util.Date;

public class FormularioMonitorador extends WebPage {
    private static final long serialVersionUID = 8415273463108359061L;

    MonitoradorApi monitoradorApi = new MonitoradorApi();

    FormularioMonitorador(ListarMonitoradores listarMonitoradores, ModalWindow modalWindow){
        add(new Label("monitoradorForm", Model.of("Cadastrar um novo monitorador")));

        Form<Monitorador> monitoradorForm = getForm();
        add(monitoradorForm);
    }

    FormularioMonitorador(ListarMonitoradores listarMonitoradores, ModalWindow modalWindow, Long idMonitorador) throws IOException {
        add(new Label("monitoradorForm", Model.of("Editar info do monitorador")));

        Form<Monitorador> monitoradorForm = getForm();
        System.out.println(monitoradorApi.buscarMonitorador(idMonitorador));
        monitoradorForm.setModelObject(monitoradorApi.buscarMonitorador(idMonitorador));
//        monitoradorForm.visitFormComponents() TODO ao abrir formulário trazer inputs corretos.
        add(monitoradorForm);
    }

    private Form<Monitorador> getForm(){ // TODO SPLIT IN MORE METHODS
        Form<Monitorador> monitoradorForm = new Form<Monitorador>("formMonitorador", new CompoundPropertyModel<>(new Monitorador())){
            private static final long serialVersionUID = -7175314782218355274L;

            @Override
            protected void onSubmit() {
                System.out.println(getModelObject());
                Monitorador monitorador = getModelObject();
                System.out.println("Monitorador submetido" + monitorador);
                monitorador = salvar(getModelObject());
                System.out.println("Monitorador salvo no BD" + monitorador);
            }
        };

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

    private Monitorador salvar(Monitorador monitorador){
        try {
            if(monitorador.getId() == null){
                return monitoradorApi.cadastrarMonitorador(monitorador);
            } else {
                return monitoradorApi.atualizarMonitorador(monitorador, monitorador.getId());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Não salvei o que vc pediu!");
        }
    }
}
