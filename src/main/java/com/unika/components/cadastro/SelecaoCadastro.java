package com.unika.components.cadastro;

import com.unika.BasePage;
import com.unika.apiService.MonitoradorApi;
import com.unika.components.Index;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTextField;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;

public class SelecaoCadastro extends BasePage {
    private static final long serialVersionUID = 5334566601860196755L;
    private final MonitoradorApi monitoradorApi = new MonitoradorApi();

    Form<Monitorador> getForm(String id, TipoPessoa tipoPessoa) {
        Monitorador monitorador = new Monitorador();
        CompoundPropertyModel<Monitorador> monitoradorCompoundPropertyModel = new CompoundPropertyModel<>(monitorador);

        // Criação do Form
        Form<Monitorador> monitoradorForm = new Form<>(id, monitoradorCompoundPropertyModel){ // Cria o form conforme o id passado.

            private static final long serialVersionUID = 5445788925241821951L;

            @Override
            public void onSubmit(){
                try {
                    System.out.println(this);
                    Monitorador monitoradorSubmetido = getModelObject();
                    monitoradorSubmetido.setTipoPessoa(tipoPessoa); // Define o tipo de pessoa.
                    System.out.println("Monitorador submetido: " + monitoradorSubmetido);
                    salvar(monitoradorSubmetido);
                    setResponsePage(Index.class);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        };


        TextField<String> emailInput = new EmailTextField("email");
        // TODO Resolver problemas de data, não está funfando
        LocalDateTextField dataNascimentoInput = new LocalDateTextField("dataNascimento", "yyyy-MM-dd");
        monitoradorForm.add(emailInput, dataNascimentoInput); // Add inputs genéricos


        // Validaçoẽs Genéricas
        emailInput.setLabel(Model.of("Email")).setRequired(true).add(EmailAddressValidator.getInstance());
        // TODO Validação de Data de Nascimento
        
        return monitoradorForm;
    }

    void salvar(Monitorador monitorador){
        try {
            Monitorador monitoradorDB = monitoradorApi.cadastrarMonitorador(monitorador);
            System.out.println(monitoradorDB);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
