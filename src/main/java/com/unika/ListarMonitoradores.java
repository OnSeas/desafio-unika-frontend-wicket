package com.unika;

import com.unika.dialogs.ConfirmationModal;
import com.unika.model.Monitorador;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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
    final FeedbackPanel feedbackPanel = new FeedbackPanel("FeedBackMessages");
    final ModalWindow modalWindow = new ModalWindow("modaw");
    final WebMarkupContainer listWMC = new WebMarkupContainer("listWMC");

    public ListarMonitoradores(PageParameters parameters) throws IOException {
        super(parameters);

        // TODO Ainda não retorna as mensagens
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        // Modal que é usado em todos os pop-ups
        modalWindow.setCookieName("modalWindow-1");
        modalWindow.setAutoSize(true);
        add(modalWindow);

        // WMC que envolve a lista já inciando a lista geral
        listWMC.setOutputMarkupId(true);
        putPageableList(monitoradorApi.listarMonitoradores());
        add(listWMC);

        add(new Label("ListLabel", Model.of("Lista de monitoradores")));

        // Botão de criar novo monitorador
        add( new AjaxLink<Void>("formNovoMonitorador") {
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
        });

        // Função quando alguma modal window é fechada.
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
            private static final long serialVersionUID = 1L;
            @Override
            public void onClose(AjaxRequestTarget target){
                try {
                    putPageableList(monitoradorApi.listarMonitoradores());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                target.add(listWMC);

                // TODO Adcionar a mensagem de sucesso!
            }
        });

        // Adciona o Form de pesquisar
        add(getFormPesquisa());
    }

    // --- METÓDOS INTERNOS ---
    // PEGA O FORM
    private Form<String> getFormPesquisa(){
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
                    putPageableList(getListaMonitoradores(inputTipo.getModelObject(), inputPesquisa.getModelObject()));
                    target.add(listWMC);
                    target.add(feedbackPanel);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        };
        pesquisarForm.add(inputPesquisa, inputTipo, ajaxButton);

        inputTipo.setLabel(Model.of("O tipo da pesquisa")).setRequired(true);

        return pesquisarForm;
    }

    // ADCIONA A LISTA COM PAGING
    private void putPageableList(List<Monitorador> monitoradores) throws IOException {
        // remove se já existir algum
        listWMC.removeAll();

        // Adciona o PageableListView de acordo com a lista gerada
        PageableListView<Monitorador> monitoradorListView = construirLista(listWMC, monitoradores, modalWindow);
        listWMC.add(monitoradorListView);
        // Adciona o pagingNavigator
        AjaxPagingNavigator pagingNavigation = new AjaxPagingNavigator("pageNavigator", monitoradorListView);
        listWMC.add(pagingNavigation);
    }


    // CONSTRÓI A LISTA
    private PageableListView<Monitorador> construirLista(WebMarkupContainer wmc, List<Monitorador> monitoradores, ModalWindow modalWindow) throws IOException {
        return new PageableListView<Monitorador>("monitoradores", monitoradores, 10) {
            private static final long serialVersionUID = -7313164500893623865L;
            @Override
            protected void populateItem(final ListItem<Monitorador> listItem) {
                listItem.add(new Label("index", Model.of(listItem.getIndex() + 1)));
                listItem.add(new Label("tipoPessoa", new PropertyModel<Monitorador>(listItem.getModel(),"tipoPessoa")));
                listItem.add(new Label("email", new PropertyModel<Monitorador>(listItem.getModel(),"email")));
                listItem.add(new Label("dataNascimento", new PropertyModel<Monitorador>(listItem.getModel(),"dataNascimento")));
                listItem.add(new Label("cpf", new PropertyModel<Monitorador>(listItem.getModel(),"cpf")));
                listItem.add(new Label("cnpj", new PropertyModel<Monitorador>(listItem.getModel(),"cnpj")));
                listItem.add(new AjaxLink<Void>("ajaxEcluirMonitorador") {
                    private static final long serialVersionUID = -1679276620382639682L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setPageCreator(new ModalWindow.PageCreator() {
                                private static final long serialVersionUID = 6452198188800512805L;
                                @Override
                                public Page createPage() {
                                    return new ConfirmationModal(
                                            listItem,
                                            "Deseja excluir o monitorador " + listItem.getModelObject().getId() + "?",
                                            "excluir"
                                    );
                                }
                            });
                            target.add(wmc);
                        } catch (Exception e){
                            System.out.println("Não excluíu!");
                        }
                        modalWindow.show(target);
                    }
                });
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
                AjaxLink<Void> desativarAjax = new AjaxLink<Void>("Destivar") {
                    private static final long serialVersionUID = -505817807318118040L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setPageCreator(new ModalWindow.PageCreator() {
                                private static final long serialVersionUID = -488870408406200986L;

                                @Override
                                public Page createPage() {
                                    return new ConfirmationModal(
                                            listItem,
                                            "Deseja desativar o monitorador " + listItem.getModelObject().getId() + "?",
                                            "desativar"
                                    );
                                }
                            });
                            target.add(wmc);
                        } catch (Exception e){
                            System.out.println("Não desativou!");
                        }
                        modalWindow.show(target);
                    }
                };
                AjaxLink<Void> ativarAjax = new AjaxLink<Void>("Ativar") {
                        private static final long serialVersionUID = -505817807318118040L;
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            try {
                                modalWindow.setPageCreator(new ModalWindow.PageCreator() { // TODO Talvez compensa criar uma função para isso
                                    private static final long serialVersionUID = -6353631616791952939L;

                                    @Override
                                    public Page createPage() {
                                        return new ConfirmationModal(
                                                listItem,
                                                "Deseja ativar o monitorador " + listItem.getModelObject().getId() + "?",
                                                "ativar"
                                        );
                                    }
                                });
                                target.add(wmc);
                            } catch (Exception e){
                                System.out.println("Não ativou!");
                            }
                            modalWindow.show(target);
                        }
                    };
                if (listItem.getModelObject().getAtivo()){
                    ativarAjax.setVisible(false);
                } else {
                    desativarAjax.setVisible(false);
                }

                listItem.add(desativarAjax, ativarAjax);
            }
        };
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
