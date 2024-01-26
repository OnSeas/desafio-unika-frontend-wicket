package com.unika.Panels;

import com.unika.dialogs.ConfirmationModal;
import com.unika.forms.MonitoradorForm;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class MonitoradorListPanel extends Panel {
    private static final long serialVersionUID = -3590955289064988486L;
    ModalWindow modalWindow = new ModalWindow("modawOpcoes");
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    WebMarkupContainer monitoradorListWMC = new WebMarkupContainer("monitoradorListWMC");
    List<Monitorador> monitoradores;

    public MonitoradorListPanel(String id, List<Monitorador> monitoradores) {
        super(id);

        // Inicia a Lista
        this.monitoradores = monitoradores;

        monitoradorListWMC.setOutputMarkupId(true);
        monitoradorListWMC.setOutputMarkupPlaceholderTag(true);
        addPageableList();
        add(monitoradorListWMC);

        // config do modaw
        modalWindow.setCookieName("modalWindow-options");
        modalWindow.setOutputMarkupId(true);
        add(modalWindow);

        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 7800205096545281286L;
            @Override
            public void onClose(AjaxRequestTarget target) { // TODO checkar se foi submetido antes de atualziar a lista
                recarregarMonitoradores(); // Ao Atualizar, Excluir, Desativar, Ativar.
                addPageableList();
                target.add(monitoradorListWMC);
            }
        });
    }

    // Recarrega a lista de monitoradores
    private void recarregarMonitoradores(){
        try {
            monitoradores = monitoradorApi.listarMonitoradores();
        } catch (Exception e){
            System.out.println("Erro ao atualizar lista de monitoradores: " + e.getMessage());
        }
    }

    // Coloca Páginação
    private void addPageableList(){
        monitoradorListWMC.removeAll(); // Remove se já existir uma lista

        PageableListView<Monitorador> monitoradorListView = construirLista();

        // Page navigator
        AjaxPagingNavigator pagingNavigation = new AjaxPagingNavigator("pageNavigator", monitoradorListView);
        pagingNavigation.setOutputMarkupId(true);
        pagingNavigation.setOutputMarkupPlaceholderTag(true);
        pagingNavigation.setVisible(monitoradorListView.getList().size() >= 10);
        monitoradorListWMC.setVisible(!monitoradorListView.getList().isEmpty());

        monitoradorListWMC.add(monitoradorListView, pagingNavigation);
    }

    // CONSTRÓI A LISTA
    private PageableListView<Monitorador> construirLista() {
        return new PageableListView<Monitorador>("monitoradores", monitoradores, 10) {
            private static final long serialVersionUID = -7313164500893623865L;
            @Override
            protected void populateItem(final ListItem<Monitorador> listItem) {
                // Valores de cada monitorador
                listItem.add(new Label("index", Model.of(listItem.getIndex() + 1)));
                listItem.add(new Label("tipoPessoa", new PropertyModel<TipoPessoa>(listItem.getModel(),"tipoPessoa").getObject().getLabel()));
                listItem.add(new Label("email", new PropertyModel<Monitorador>(listItem.getModel(),"email")));
                listItem.add(new Label("dataNascimento", new PropertyModel<Monitorador>(listItem.getModel(),"dataNascimento")));
                listItem.add(new Label("cpf", new PropertyModel<Monitorador>(listItem.getModel(),"cpf")));
                listItem.add(new Label("cnpj", new PropertyModel<Monitorador>(listItem.getModel(),"cnpj")));

                // Opções para cada monitorador
                listItem.add(new AjaxLink<Void>("formEditarMonitorador") {
                    private static final long serialVersionUID = -387605849215267697L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setContent(new MonitoradorFormPanel(
                                    ModalWindow.CONTENT_ID,
                                    new MonitoradorForm("formMonitorador", monitoradorApi.buscarMonitorador(listItem.getModelObject().getId()))));
                        } catch (Exception e){
                            System.out.println("Não Editou!");
                        }
                        modalWindow.show(target);
                    }
                });
                listItem.add(new AjaxLink<Void>("ajaxEcluirMonitorador") {
                    private static final long serialVersionUID = -1679276620382639682L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setContent(new ConfirmationModal(
                                    ModalWindow.CONTENT_ID,
                                    listItem,
                                    "Deseja excluir o monitorador " + listItem.getModelObject().getId() + "?",
                                    "excluir"
                            ));
                        } catch (Exception e){
                            System.out.println("Não excluíu!");
                        }
                        modalWindow.show(target);
                    }
                });
                AjaxLink<Void> desativarAjax = new AjaxLink<Void>("Destivar") {
                    private static final long serialVersionUID = -505817807318118040L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setContent(new ConfirmationModal(
                                    ModalWindow.CONTENT_ID,
                                            listItem,
                                            "Deseja desativar o monitorador " + listItem.getModelObject().getId() + "?",
                                            "desativar"
                                    ));
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
                            modalWindow.setContent(new ConfirmationModal(
                                            ModalWindow.CONTENT_ID,
                                            listItem,
                                            "Deseja ativar o monitorador " + listItem.getModelObject().getId() + "?",
                                            "ativar"
                                    ));
                        } catch (Exception e){
                            System.out.println("Não ativou!");
                        }
                        modalWindow.show(target);
                    }
                };

                // Mostrar o botão ativar/desativar de acordo com o status do monitorador
                if (listItem.getModelObject().getAtivo()){
                    ativarAjax.setVisible(false);
                } else {
                    desativarAjax.setVisible(false);
                }

                listItem.add(desativarAjax, ativarAjax);
            }
        };
    }
}
