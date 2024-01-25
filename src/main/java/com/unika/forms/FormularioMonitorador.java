package com.unika.forms;

import com.unika.Panels.EnderecoListPanel;
import com.unika.model.Endereco;
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
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormularioMonitorador extends WebPage {
    private static final long serialVersionUID = 8415273463108359061L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    WebMarkupContainer listaEnderecoWMC = new WebMarkupContainer("listaEnderecoWMC");
    List<Endereco> enderecoList = new ArrayList<>();

    // Criar novo monitorador
    public FormularioMonitorador(){
        add(new Label("monitoradorForm", Model.of("Cadastrar um novo monitorador")));
        add(getForm());

        add(addCriarEndButton(-1L));

        listaEnderecoWMC.setOutputMarkupId(true);
        listaEnderecoWMC.setOutputMarkupPlaceholderTag(true);

        addListaEndereco(-1L);
        listaEnderecoWMC.setVisible(false);
        add(listaEnderecoWMC);
    }


    // Editar um monitorador existente
    public FormularioMonitorador(Long idMonitorador) throws IOException {
        add(new Label("monitoradorForm", Model.of("Editar info do monitorador")));

        Form<Monitorador> monitoradorForm = getForm();

        // Preencher os dados do monitorador
        Monitorador monitorador = monitoradorApi.buscarMonitorador(idMonitorador);
        monitoradorForm.setModelObject(monitorador);
        this.enderecoList = monitorador.getEnderecoList();

        // Mostrar os inputs certos
        WebMarkupContainer wmc;
        if (monitorador.getTipoPessoa().equals(TipoPessoa.PESSOA_FISICA)){
            wmc = (WebMarkupContainer) monitoradorForm.get("pfWMC");
        } else{
            wmc = (WebMarkupContainer) monitoradorForm.get("pjWMC");
        }
        wmc.setVisible(true);

        add(addCriarEndButton(idMonitorador));
        listaEnderecoWMC.setOutputMarkupId(true);
        listaEnderecoWMC.setOutputMarkupPlaceholderTag(true);

        addListaEndereco(idMonitorador);
        add(monitoradorForm, listaEnderecoWMC);
    }


    private void addListaEndereco(Long idMonitorador){
        listaEnderecoWMC.removeAll();
        listaEnderecoWMC.setVisible(!enderecoList.isEmpty());
        listaEnderecoWMC.add(new EnderecoListPanel("enderecoPanel", enderecoList, idMonitorador));
    }

    private Form<Monitorador> getForm(){ // TODO SPLIT IN MORE METHODS
        Form<Monitorador> monitoradorForm = new Form<Monitorador>("formMonitorador", new CompoundPropertyModel<>(new Monitorador()));

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
                try {
                    Monitorador monitorador = monitoradorForm.getModelObject();
                    System.out.println("Monitorador submetido: " + monitorador);
                    salvar(monitorador);
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

        // Botões que mostram os inputs de acordo com o Tipo de Pessoa selecionado.
        AjaxLink<Void> showPf = new AjaxLink<Void>("showPf") {
            private static final long serialVersionUID = -7872773929920444908L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                pfWMC.setVisible(true);
                pjWMC.setVisible(false);
                target.add(pfWMC, pjWMC);
            }
        };
        AjaxLink<Void> showPj = new AjaxLink<Void>("showPj") {
            private static final long serialVersionUID = -7468546216784505534L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                pfWMC.setVisible(false);
                pjWMC.setVisible(true);
                target.add(pfWMC, pjWMC);
            }
        };

        radioTipoPessoa.add(showPf, showPj);

        monitoradorForm.add(pfWMC, pjWMC);
        return monitoradorForm;
    }


    // Botão criar novo monitorador TODO arrumar esse método, olhar referência do outro
    private AjaxLink<Void> addCriarEndButton(Long idMonitorador){

        // Modal Window que abre o formulário de endereço
        ModalWindow modalWindow = new ModalWindow("criarEndModal");
        modalWindow.setCookieName("criarendereco-modal");
        add(modalWindow);
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 1989574373953651865L;
            @Override
            public void onClose(AjaxRequestTarget target){
                System.out.println(enderecoList);

                addListaEndereco(idMonitorador);
                target.add(listaEnderecoWMC);
            }
        });

        AjaxLink<Void> criarEnderecoAjax =  new AjaxLink<Void>("criarEndereco") {
            private static final long serialVersionUID = 7923685135874148873L;
            @Override
            public void onClick(AjaxRequestTarget target) {

                modalWindow.setContent(new EmptyPanel(modalWindow.getContentId()));
                modalWindow.setPageCreator(() -> new FormularioEndereco(FormularioMonitorador.this, idMonitorador));
                modalWindow.show(target);
            }
        };

        return criarEnderecoAjax;
    }

    // Salvo o monitorador em POST (sem id) ou PUT (quando tem id).
    private void salvar(Monitorador monitorador){
        try {
            if(monitorador.getId() == null){
                monitorador.setEnderecoList(enderecoList);
                monitoradorApi.cadastrarMonitorador(monitorador);
            } else {
                monitoradorApi.atualizarMonitorador(monitorador, monitorador.getId());
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}