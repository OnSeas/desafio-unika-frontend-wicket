package com.unika.Panels;

import com.unika.dialogs.ConfirmationModal;
import com.unika.forms.MonitoradorFormPanel;
import com.unika.forms.PesquisaFormPanel;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.File;
import java.io.Serial;
import java.util.List;

public class MonitoradorListPanel extends Panel {
    @Serial
    private static final long serialVersionUID = -3590955289064988486L;
    ModalWindow modalWindow = new ModalWindow("modawOpcoes");
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    WebMarkupContainer monitoradorListWMC = new WebMarkupContainer("monitoradorListWMC");
    List<Monitorador> monitoradores;

    public MonitoradorListPanel(String id, List<Monitorador> monitoradores, List<FeedbackPanel> feedbackPanels) {
        super(id);

        // Inicia a Lista
        this.monitoradores = monitoradores;

        monitoradorListWMC.setOutputMarkupId(true);
        monitoradorListWMC.setOutputMarkupPlaceholderTag(true);
        addPageableList(feedbackPanels);
        add(monitoradorListWMC);

        // config do modaw
        modalWindow.setCookieName("modalWindow-options");
        modalWindow.setOutputMarkupId(true);
        add(modalWindow);

        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Serial
            private static final long serialVersionUID = 7800205096545281286L;
            @Override
            public void onClose(AjaxRequestTarget target) {
                recarregarMonitoradores(); // Ao Atualizar, Excluir, Desativar, Ativar.
                addPageableList(feedbackPanels);
                feedbackPanels.forEach(target::add); // Lista com feedbackPanel success e error
                target.add(monitoradorListWMC);
            }
        });
    }

    // Recarrega a lista de monitoradores
    private void recarregarMonitoradores(){
        try {
            monitoradores = monitoradorApi.listarMonitoradores();
        } catch (Exception e){
            error("Erro ao atualizar lista de monitoradores: " + e.getMessage());
        }
    }

    // Coloca Páginação
    private void addPageableList(List<FeedbackPanel> feedbackPanels){
        monitoradorListWMC.removeAll(); // Remove se já existir uma lista

        PageableListView<Monitorador> monitoradorListView = construirLista();
        AjaxPagingNavigator pagingNavigation = new AjaxPagingNavigator("pageNavigator", monitoradorListView){
            @Serial
            private static final long serialVersionUID = 3745148448399664563L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setVisible(monitoradorListView.getList().size() > 8);
            }
        };
        pagingNavigation.setOutputMarkupId(true);
        pagingNavigation.setOutputMarkupPlaceholderTag(true);

        monitoradorListWMC.setVisible(!monitoradorListView.getList().isEmpty());
        monitoradorListWMC.add(monitoradorListView, pagingNavigation, addFormPesquisa(monitoradorListView, feedbackPanels));
    }

    private Panel addFormPesquisa(ListView<Monitorador> listView, List<FeedbackPanel> feedbackPanels){
        return new PesquisaFormPanel("searchForm", listView, feedbackPanels);
    }

    // CONSTRÓI A LISTA
    private PageableListView<Monitorador> construirLista() {
        return new PageableListView<Monitorador>("monitoradores", monitoradores, 8) {
            @Serial
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
                    @Serial
                    private static final long serialVersionUID = -387605849215267697L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setContent(new MonitoradorFormPanel(modalWindow.getContentId(), listItem.getModelObject()));
                        } catch (Exception e){
                            error(e.getMessage());
                        }
                        modalWindow.show(target);
                    }
                });
                listItem.add(new AjaxLink<Void>("ajaxEcluirMonitorador") {
                    @Serial
                    private static final long serialVersionUID = -1679276620382639682L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setContent(new ConfirmationModal(
                                    modalWindow.getContentId(),
                                    listItem,
                                    "Deseja excluir o monitorador " + listItem.getModelObject().getId() + "?",
                                    "excluir"
                            ));
                        } catch (Exception e){
                            error(e.getMessage());
                        }
                        modalWindow.show(target);
                    }
                });
                AjaxLink<Void> desativarAjax = new AjaxLink<Void>("Destivar") {
                    @Serial
                    private static final long serialVersionUID = -505817807318118040L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setContent(new ConfirmationModal(
                                    modalWindow.getContentId(),
                                            listItem,
                                            "Deseja desativar o monitorador " + listItem.getModelObject().getId() + "?",
                                            "desativar"
                                    ));
                        } catch (Exception e){
                            error(e.getMessage());
                        }
                        modalWindow.show(target);
                    }
                };
                AjaxLink<Void> ativarAjax = new AjaxLink<Void>("Ativar") {
                    @Serial
                    private static final long serialVersionUID = -505817807318118040L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            modalWindow.setContent(new ConfirmationModal(
                                            modalWindow.getContentId(),
                                            listItem,
                                            "Deseja ativar o monitorador " + listItem.getModelObject().getId() + "?",
                                            "ativar"
                                    ));
                        } catch (Exception e){
                            error(e.getMessage());
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

                listItem.add(new AjaxLink<Void>("reportView") { //TODO Mostrar o Pdf ou Html na ModalWindow
                    @Serial
                    private static final long serialVersionUID = 7878631882100442474L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        modalWindow.setContent(new PdfPanel(modalWindow.getContentId()));
                        modalWindow.show(target);
                    }
                });

                listItem.add(new DownloadLink("reportDownload", new AbstractReadOnlyModel<File>() {
                    @Serial
                    private static final long serialVersionUID = -8630718144901310523L;
                    @Override
                    public File getObject() {
                        File tempFile;
                        try {
                            tempFile = monitoradorApi.gerarRelatorio(listItem.getModelObject().getId());
                        }
                        catch (Exception e){
                            tempFile = null;
                            error(e.getMessage());
                        }
                        return tempFile;
                    }
                }));
            }
        };
    }
}
