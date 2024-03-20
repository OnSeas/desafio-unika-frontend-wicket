package com.unika.Panels;

import com.unika.MonitoradorForm;
import com.unika.dialogs.ConfirmationLink;
import com.unika.forms.ImportFormPanel;
import com.unika.forms.PesquisaFormPanel;
import com.unika.model.Monitorador;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
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
import java.io.IOException;
import java.io.Serial;
import java.util.List;

public class MonitoradorListPanel extends Panel {
    @Serial
    private static final long serialVersionUID = -3590955289064988486L;
    final FeedbackPanel feedbackPanel;
    ModalWindow modalWindow = new ModalWindow("modaw");
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    WebMarkupContainer monitoradorListWMC = new WebMarkupContainer("monitoradorListWMC"){
        @Serial
        private static final long serialVersionUID = 7497134965226070790L;
        @Override
        protected void onConfigure() {
            super.onConfigure();
            this.setVisible(!monitoradores.isEmpty());
        }
    };

    WebMarkupContainer emptyListWMC = new WebMarkupContainer("emptyListWMC"){
        @Serial
        private static final long serialVersionUID = -1140679043565414998L;
        @Override
        protected void onConfigure() {
            super.onConfigure();
            this.setVisible(!monitoradorListWMC.isVisible());
        }
    };
    List<Monitorador> monitoradores;

    public MonitoradorListPanel(String id, FeedbackPanel feedbackPanel) {
        super(id);
        this.feedbackPanel = feedbackPanel;

        // Inicia a Lista
        try {
            this.monitoradores = monitoradorApi.listarMonitoradores();
        } catch (IOException e) {
            feedbackPanel.error(e.getMessage());
        }

        add(new Label("titleListPanel", Model.of("Controle de monitoradores")));

        monitoradorListWMC.setOutputMarkupId(true);
        monitoradorListWMC.setOutputMarkupPlaceholderTag(true);
        addPageableList(feedbackPanel);
        add(monitoradorListWMC);

        emptyListWMC.setOutputMarkupId(true);
        emptyListWMC.setOutputMarkupPlaceholderTag(true);
        addEmptyListComponents();
        add(emptyListWMC);

        // config do modaw
        modalWindow.setOutputMarkupId(true);
        modalWindow.setResizable(false);
        add(modalWindow);

        addButtons();
    }

    private void addButtons(){
        monitoradorListWMC.add(new DownloadLink("downloadAllReport", new AbstractReadOnlyModel<>() {
            @Serial
            private static final long serialVersionUID = -8630718144901310523L;
            @Override
            public File getObject() {
                File reportFile;
                try {
                    reportFile = monitoradorApi.gerarRelatorio();
                }
                catch (Exception e){
                    reportFile = null;
                    System.out.println(e.getMessage());
                }
                return reportFile;
            }
        }));

        monitoradorListWMC.add(new DownloadLink("downloadMonitoradoresXlsx", new AbstractReadOnlyModel<File>() {
            @Serial
            private static final long serialVersionUID = 5404244274162871915L;

            @Override
            public File getObject() {
                File file;
                try {
                    file = monitoradorApi.exportarXLSX();
                    System.out.println(file);
                } catch (Exception e){
                    file = null;
                    System.out.println(e.getMessage());
                }
                return file;
            }
        }));
    }

    // Coloca Páginação
    private void addPageableList(FeedbackPanel feedbackPanel){
        PageableListView<Monitorador> monitoradorListView = construirLista();
        AjaxPagingNavigator pagingNavigation = new AjaxPagingNavigator("pageNavigator", monitoradorListView){
            @Serial
            private static final long serialVersionUID = 3745148448399664563L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setVisible(monitoradorListView.getList().size() > monitoradorListView.getItemsPerPage());
            }
        };
        pagingNavigation.setOutputMarkupId(true);
        pagingNavigation.setOutputMarkupPlaceholderTag(true);
        monitoradorListWMC.add(addFormPesquisa(monitoradorListView, feedbackPanel));
        monitoradorListWMC.add(monitoradorListView, pagingNavigation);
    }

    private Panel addFormPesquisa(ListView<Monitorador> listView, FeedbackPanel feedbackPanel){
        return new PesquisaFormPanel("searchForm", listView, feedbackPanel);
    }

    private void addEmptyListComponents(){
        emptyListWMC.add(new AjaxLink<Void>("criarMonitorador") {
            @Serial
            private static final long serialVersionUID = -7101144915138464271L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(MonitoradorForm.class);
            }
        });
        emptyListWMC.add(new AjaxLink<Void>("importarMonitorador") {
            @Serial
            private static final long serialVersionUID = 7931682729645654379L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                setModalImportPanel();
                modalWindow.setContent(new ImportFormPanel(modalWindow.getContentId(), feedbackPanel));
                modalWindow.show(target);
            }
        });
    }

    // CONSTRÓI A LISTA
    private PageableListView<Monitorador> construirLista() {
        return new PageableListView<Monitorador>("monitoradores", monitoradores, 10) {
            @Serial
            private static final long serialVersionUID = -7313164500893623865L;
            @Override
            protected void populateItem(final ListItem<Monitorador> listItem) {

                String nome = listItem.getModelObject().getTipoPessoa().equals(TipoPessoa.PESSOA_FISICA) ? listItem.getModelObject().getNome() : listItem.getModelObject().getRazaoSocial();

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
                        setResponsePage(new MonitoradorForm(listItem.getModelObject()));
                    }
                });
                listItem.add(new ConfirmationLink<>("ajaxEcluirMonitorador", "Tem certeza que deseja excluir o monitorador " + nome + "?") {
                    @Serial
                    private static final long serialVersionUID = -1679276620382639682L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        deletarMontorador(listItem.getModelObject());
                        target.add(monitoradorListWMC, emptyListWMC, feedbackPanel);
                    }
                });
                listItem.add(new AjaxLink<Void>("infoMonitorador") {
                    @Serial
                    private static final long serialVersionUID = -4065299638781858229L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setModalInfoPanel(listItem.getModelObject().getEnderecoList().size());
                        modalWindow.setContent(new InfoMonitoradorPanel(modalWindow.getContentId(), listItem.getModelObject(), feedbackPanel));
                        modalWindow.show(target);
                    }
                });
            }
        };
    }

    private void deletarMontorador(Monitorador monitorador) {
        try {
            monitoradorApi.deletarMonitorador(monitorador.getId());
            feedbackPanel.success("Monitorador deletado com sucesso!");
            monitoradores.remove(monitorador);
        } catch (Exception e) {
            feedbackPanel.error(e.getMessage());
        }
    }

    private void setModalInfoPanel(int qntEnd){
        modalWindow.showUnloadConfirmation(false);
        modalWindow.setInitialWidth(45);
        modalWindow.setWidthUnit("%");

        if(qntEnd < 1){
            modalWindow.setInitialHeight(410);
        } else if(qntEnd == 1){
            modalWindow.setInitialHeight(550);
        } else if(qntEnd == 2){
            modalWindow.setInitialHeight(590);
        } else {
            modalWindow.setInitialHeight(630);
        }
    }

    private void setModalImportPanel() {
        modalWindow.showUnloadConfirmation(false);
        modalWindow.setInitialWidth(30);
        modalWindow.setWidthUnit("%");
        modalWindow.setInitialHeight(230);
    }
        @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // jQuery para fazer sideBar ficar com css de selecionado de acordo com a página
        response.render(OnDomReadyHeaderItem.forScript("$('#formMonitoradorID').removeClass(\"active\"); " +
                "$('#homePageID').addClass(\"active\"); " +
                "$('#formImportID').removeClass(\"active\");"));
    }
}
