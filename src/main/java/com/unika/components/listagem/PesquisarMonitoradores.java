package com.unika.components.listagem;

import com.unika.BasePage;
import com.unika.apiService.MonitoradorApi;
import com.unika.model.Monitorador;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.Model;

import java.io.IOException;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class PesquisarMonitoradores extends BasePage { // TODO Limpar o WebMarkupContainer. Para pesquisar quantas vezes quiser.
    private static final long serialVersionUID = 5995098392046397023L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();

    public PesquisarMonitoradores(){
        add(new Label("pesquisarTitulo", Model.of("Pesquisar Monitoradores")));

        Form<String> pesquisarForm = new Form<>("pesquisarForm");
        add(pesquisarForm);

        TextField<String> pesquisaInput = new TextField<>("inputPesquisa", new Model<>());
        List<String> tipos = Arrays.asList("email", "cpf", "cnpj");
        DropDownChoice<String> tipoInput = new DropDownChoice<>("inputTipo", new Model<>(), tipos);

        pesquisarForm.add(pesquisaInput, tipoInput);

        final WebMarkupContainer containerResult = new WebMarkupContainer("divResultados");
        containerResult.setVisible(false);
        containerResult.setOutputMarkupPlaceholderTag(true);
        add(containerResult);

        AjaxButton submitAjaxLink = new AjaxButton("botaoPesquisar", pesquisarForm) {
            private static final long serialVersionUID = 4702831086642764166L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                try {
                    List<Monitorador> monitoradores = getListaMonitoradores(tipoInput.getModelObject(), pesquisaInput.getModelObject());
                    System.out.println(monitoradores);

                    PropertyListView<Monitorador> listaResultados = new PropertyListView<Monitorador>("monitoradores", monitoradores) {
                        private static final long serialVersionUID = 5732126294352603059L;

                        @Override
                        protected void populateItem(ListItem<Monitorador> listItem) {
                            listItem.add(new Label("index", Model.of(listItem.getIndex() + 1)));
                            listItem.add(new Label("tipoPessoa"));
                            listItem.add(new Label("email"));
                            listItem.add(new Label("dataNascimento"));
                        }
                    };

                    containerResult.add(listaResultados);
                    containerResult.setVisible(true);
                    target.add(containerResult);

                } catch (IOException e) {
                    System.out.println("NÃO CONSEGUIU BUSCAR NO BACKEND!");
                }
            }
        };
        pesquisarForm.add(submitAjaxLink);
    }

    private List<Monitorador> getListaMonitoradores(String tipo, String busca) throws IOException { // TODO lidar com exceçoẽs
        switch (tipo){
            case "email":
                return monitoradorApi.buscarMonitoradoresPorEmail(busca);
            case "cpf":
                return monitoradorApi.buscarMonitoradorPorCpf(busca);
            case "cnpj":
                return monitoradorApi.buscarMonitoradorPorCnpj(busca);
            default: throw new InvalidPropertiesFormatException("Tipo de pesquisa inválida!");
        }
    }
}
