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

public class CadastroPessoaFisica extends SelecaoCadastro {
    private static final long serialVersionUID = -7185314781376710112L;
    
    public CadastroPessoaFisica(){
        Label labelAdd = new Label("cadastrarPF", Model.of("Cadastrar Pessoa Física"));
        add(labelAdd);

        Form<Monitorador> pessoaFisicaForm  = getForm("pessoaFisicaForm", TipoPessoa.PESSOA_FISICA);
        add(pessoaFisicaForm);
        
        TextField<String> cpfInput = new TextField<>("cpf");
        TextField<String> nomeInput = new TextField<>("nome");
        TextField<String> rgInput = new TextField<>("rg");

        pessoaFisicaForm.add(cpfInput, nomeInput, rgInput);
        
        cpfInput.setLabel(Model.of("CPF")).setRequired(true).add(StringValidator.lengthBetween(11, 14));
        nomeInput.setLabel(Model.of("Nome")).setRequired(true).add(StringValidator.lengthBetween(3, 200));
        rgInput.setLabel(Model.of("RG")).setRequired(true).add(StringValidator.exactLength(9));

        add(new FeedbackPanel("FeedbackMessage", new ErrorLevelFeedbackMessageFilter(FeedbackMessage.ERROR)));
    }
}
