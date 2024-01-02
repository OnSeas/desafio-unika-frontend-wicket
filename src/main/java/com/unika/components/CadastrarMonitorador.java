package com.unika.components;

import com.unika.BasePage;
import com.unika.apiService.MonitoradorApi;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CadastrarMonitorador extends BasePage implements Serializable {

    private static final long serialVersionUID = 5796724304354432622L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();

    public CadastrarMonitorador() {
        Label labelAdd = new Label("CadastrarMonitorador", Model.of("Cadastrar Monitorador"));
        add(labelAdd);

        Monitorador monitorador = new Monitorador();
        CompoundPropertyModel<Monitorador> monitoradorCompoundPropertyModel = new CompoundPropertyModel<>(monitorador);

        Form<Monitorador> monitoradorForm = new Form<>("monitoradorForm", monitoradorCompoundPropertyModel){
            @Override
            public void onSubmit(){
                Monitorador monitoradorSubmetido = getModelObject();
                System.out.println("Monitorador submetido: " + monitoradorSubmetido);
                salvar(monitoradorSubmetido);
                setResponsePage(BasePage.class);
            }
        };
        add(monitoradorForm);

        DropDownChoice<TipoPessoa> tipoPessoaDropDown = new DropDownChoice<>("tipoPessoa",
                Arrays.asList(TipoPessoa.values()),
                new IChoiceRenderer<TipoPessoa>() {
                    private static final long serialVersionUID = -7052635911861230645L;

                    @Override
                    public Object getDisplayValue(TipoPessoa tipoPessoa) {
                        return tipoPessoa.getLabel();
                    }

                    @Override
                    public String getIdValue(TipoPessoa tipoPessoa, int i) {
                        return tipoPessoa.name();
                    }

                    @Override
                    public TipoPessoa getObject(String s, IModel<? extends List<? extends TipoPessoa>> iModel) {
                        return TipoPessoa.PESSOA_FISICA;
                    }
                }
        );
        TextField<String> emailInput = new TextField<>("email");
        TextField<String> dataNascimentoInput = new TextField<>("dataNascimento");
        TextField<String> inscricaoEstadualInput = new TextField<>("inscricaoEstadual");
        TextField<String> cpfInput = new TextField<>("cpf");
        TextField<String> nomeInput = new TextField<>("nome");
        TextField<String> rgInput = new TextField<>("rg");
        TextField<String> cnpjInput = new TextField<>("cnpj");
        TextField<String> razaoSocialInput = new TextField<>("razaoSocial");

        monitoradorForm.add(tipoPessoaDropDown, emailInput, dataNascimentoInput, inscricaoEstadualInput, cpfInput, nomeInput, rgInput, cnpjInput, razaoSocialInput);
    }

    private void salvar(Monitorador monitorador){
        try {
            Monitorador monitoradorDB = monitoradorApi.cadastrarMonitorador(monitorador);
            System.out.println(monitoradorDB);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
