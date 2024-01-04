package com.unika.components.cadastro.endereco;

import com.unika.BasePage;
import com.unika.apiService.MonitoradorApi;
import com.unika.components.Index;
import com.unika.model.Endereco;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

public class CadastroEndereco extends BasePage{
    private static final long serialVersionUID = 1978102769724181321L;
    private final MonitoradorApi monitoradorApi = new MonitoradorApi();

    public CadastroEndereco(){
        Label label = new Label("TitleEnderecoForm", Model.of("Cadastrar novo endereço"));
        add(label);

        Endereco endereco = new Endereco();
        CompoundPropertyModel<Endereco> enderecoModel = new CompoundPropertyModel<>(endereco);

        Form<Endereco> enderecoForm = new Form<>("enderecoForm", enderecoModel){
            private static final long serialVersionUID = -5402096776653835056L;

            @Override
            public void onSubmit(){
                try {
                    Endereco enderecoSubmetido = getModelObject();
                    System.out.println("Endereco submetido: " + enderecoSubmetido);
                    salvar(enderecoSubmetido);
                    setResponsePage(Index.class);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

        };
        add(enderecoForm);

        TextArea<String> enderecoInput = new TextArea<>("endereco");
        NumberTextField<Integer> numeroInput = new NumberTextField<>("numero");
        TextField<String> cepInput = new TextField<>("cep");
        TextField<String> bairroInput = new TextField<>("bairro");
        TextField<String> telefoneInput = new TextField<>("telefone");
        TextField<String> cidadeInput = new TextField<>("cidade");
        Select<String> dropEstado = new Select<>("estado"); // TODO Não está recebendo uma String
        CheckBox principalCheckBox = new CheckBox("principal");

        enderecoForm.add(enderecoInput, numeroInput, cepInput, bairroInput, telefoneInput, cidadeInput, dropEstado, principalCheckBox);

    }

    void salvar(Endereco endereco){
        try {
            // TODO Salvar no monitorador solicitante
            String resultado = monitoradorApi.adcionarEndereco(1L, endereco);
            System.out.println(resultado);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
