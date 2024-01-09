package com.unika.components.cadastro;

import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

public class CadastroPessoaJuridica extends SelecaoCadastro{
    private static final long serialVersionUID = -7571073861371389706L;

    public CadastroPessoaJuridica(){
        Label labelAdd = new Label("cadastrarPJ", Model.of("Cadastrar Pessoa Jurídico"));
        add(labelAdd);

        Form<Monitorador> pessoaFisicaForm  = getForm("pessoaJuridicaForm", TipoPessoa.PESSOA_JURIDICA);
        add(pessoaFisicaForm);

        TextField<String> razaoSocialInput = new TextField<>("razaoSocial");
        TextField<String> cnpjInput = new TextField<>("cnpj");
        TextField<String> inscricaoEstadualInput = new TextField<>("inscricaoEstadual");

        pessoaFisicaForm.add(razaoSocialInput, cnpjInput, inscricaoEstadualInput);

        razaoSocialInput.setLabel(Model.of("Razão Social")).setRequired(true).add(StringValidator.lengthBetween(3, 200));
        cnpjInput.setLabel(Model.of("CNPJ")).setRequired(true).add(StringValidator.lengthBetween(14, 18));
        inscricaoEstadualInput.setLabel(Model.of("Inscrição Estadual")).setRequired(true).add(StringValidator.maximumLength(18));

        add(new FeedbackPanel("FeedbackMessage", new ErrorLevelFeedbackMessageFilter(FeedbackMessage.ERROR)));
    }
}
