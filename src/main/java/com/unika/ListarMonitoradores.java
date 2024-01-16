package com.unika;

import com.unika.model.Monitorador;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class ListarMonitoradores extends HomePage{
    private static final long serialVersionUID = 2178207601281324056L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();

    public ListarMonitoradores(PageParameters parameters) throws IOException {
        super(parameters);

        add(new Label("ListLabel", Model.of("Lista de monitoradores")));

        final ModalWindow modalWindow = new ModalWindow("modawFormMonitorador");
        modalWindow.setCookieName("modalWindow-1");
        add(modalWindow);
        AjaxLink<Void> ajaxNovoMonitorador = new AjaxLink<Void>("formNovoMonitorador") {
            private static final long serialVersionUID = -387605849215267697L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.setPageCreator(new ModalWindow.PageCreator() {
                    private static final long serialVersionUID = -4592416407253742093L;
                    @Override
                    public Page createPage() {
                        return new FormularioMonitorador(ListarMonitoradores.this, modalWindow);
                    }
                });
                modalWindow.show(target);
            }
        };
        add(ajaxNovoMonitorador);

        final WebMarkupContainer listWMC = new WebMarkupContainer("listWMC");
        listWMC.setOutputMarkupId(true);
        listWMC.add(construirLista(listWMC, monitoradorApi.listarMonitoradores(), modalWindow));
        add(listWMC);

        // Search form

        Form<String> pesquisarForm = new Form<>("pesquisarForm");
        add(pesquisarForm);

        TextField<String> inputPesquisa = new TextField<>("inputPesquisa", new Model<>());
        List<String> tipos = Arrays.asList("email", "cpf", "cnpj");
        DropDownChoice<String> inputTipo = new DropDownChoice<>("inputTipo", new Model<>(), tipos);
        AjaxButton ajaxButton = new AjaxButton("searchButton", pesquisarForm) {
            private static final long serialVersionUID = -3533755636036242218L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    listWMC.remove("monitoradores");

                    listWMC.add(construirLista(
                            listWMC,
                            getListaMonitoradores(inputTipo.getModelObject(), inputPesquisa.getModelObject()),
                            modalWindow));

                    target.add(listWMC);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        pesquisarForm.add(inputPesquisa, inputTipo, ajaxButton);

        inputTipo.setLabel(Model.of("O tipo da pesquisa")).setRequired(true);
    }

    // --- METÓDOS INTERNOS ---
    private ListView<Monitorador> construirLista(WebMarkupContainer wmc, List<Monitorador> enderecos, ModalWindow modalWindow) throws IOException {
        return new ListView<Monitorador>("monitoradores", enderecos) {
            private static final long serialVersionUID = -7313164500893623865L;

            @Override
            protected void populateItem(final ListItem<Monitorador> listItem) {
                listItem.add(new Label("index", Model.of(listItem.getIndex() + 1)));
                listItem.add(new Label("tipoPessoa", new PropertyModel<Monitorador>(listItem.getModel(),"tipoPessoa")));
                listItem.add(new Label("email", new PropertyModel<Monitorador>(listItem.getModel(),"email")));
                listItem.add(new Label("dataNascimento", new PropertyModel<Monitorador>(listItem.getModel(),"dataNascimento")));
                listItem.add(new Label("cpf", new PropertyModel<Monitorador>(listItem.getModel(),"cpf")));
                listItem.add(new Label("cnpj", new PropertyModel<Monitorador>(listItem.getModel(),"cnpj")));

                AjaxLink<Void> ajaxLink = new AjaxLink<Void>("ajaxEcluirMonitorador") {
                    private static final long serialVersionUID = -1679276620382639682L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        System.out.println("Excluindo o monitorador: " + listItem.getModelObject());
                        deletar(listItem.getModelObject().getId());
                        ((ListView<?>) listItem.getParent()).getList().remove(listItem.getModelObject());
                        target.add(wmc);
                    }
                };
                ajaxLink.add(new AttributeModifier("onclick", "return confirm('Delete ?');"));
                listItem.add(ajaxLink);
                listItem.add(new AjaxLink<Void>("formEditarMonitorador") {
                    private static final long serialVersionUID = -387605849215267697L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modalWindow.setPageCreator(new ModalWindow.PageCreator() {
                            private static final long serialVersionUID = -4592416407253742093L;

                            @Override
                            public Page createPage() {
                                try {
                                    return new FormularioMonitorador(
                                            ListarMonitoradores.this,
                                            modalWindow,
                                            listItem.getModelObject().getId());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                        modalWindow.show(target);
                    }
                });
            }
        };
    }

    private void deletar(Long idMonitorador){
        try {
            monitoradorApi.deletarMonitorador(idMonitorador);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Para o filtro
    private List<Monitorador> getListaMonitoradores(String tipo, String busca) throws IOException { // TODO lidar com exceções
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
