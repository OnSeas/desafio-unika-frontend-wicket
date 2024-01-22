package com.unika.Panels;

import com.unika.dialogs.ConfirmationModal;
import com.unika.forms.FormularioEndereco;
import com.unika.model.Endereco;
import com.unika.model.UF;
import com.unika.model.apiService.EnderecoApi;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class EnderecoListPanel extends Panel {
    private static final long serialVersionUID = -1429184536413087837L;
    EnderecoApi enderecoApi = new EnderecoApi();
    WebMarkupContainer enderecoListWMC = new WebMarkupContainer("enderecoListWMC");
    ModalWindow modalWindow = new ModalWindow("modalEndereco");

    // Editar Monitorador
    public EnderecoListPanel(String id, Long idMonitorador) {
        super(id);

        // Para poder aparecer se não tiver nenhum
        enderecoListWMC.setOutputMarkupId(true);
        enderecoListWMC.setOutputMarkupPlaceholderTag(true);

        ListView<Endereco> enderecoListView = construirLista(idMonitorador);
        enderecoListWMC.add(enderecoListView);
        enderecoListWMC.setVisible(!enderecoListView.getList().isEmpty());
        add(enderecoListWMC);

        modalWindow.setOutputMarkupId(true);
        add(modalWindow);

        // On Close de modal window
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 7800205096545281286L;
            @Override
            public void onClose(AjaxRequestTarget target) {
                enderecoListWMC.removeAll();
                ListView<Endereco> enderecoListView = construirLista(idMonitorador);
                enderecoListWMC.add(enderecoListView);
                enderecoListWMC.setVisible(!enderecoListView.getList().isEmpty());
                target.add(enderecoListWMC);
            }
        });
    }

    // Construir a lista de endereços
    private ListView<Endereco> construirLista(Long idMonitorador){
        try{
            System.out.println(enderecoApi.listarEnderecos(idMonitorador));
            return new ListView<Endereco>("enderecos", enderecoApi.listarEnderecos(idMonitorador)) {
                private static final long serialVersionUID = 7611437418587202266L;
                @Override
                protected void populateItem(ListItem<Endereco> listItem) {
                    listItem.add(new Label("index", Model.of(listItem.getIndex() + 1)));
                    listItem.add(new Label("endereco", new PropertyModel<Endereco>(listItem.getModel(),"endereco")));
                    listItem.add(new Label("cep", new PropertyModel<Endereco>(listItem.getModel(),"cep")));
                    listItem.add(new Label("bairro", new PropertyModel<Endereco>(listItem.getModel(),"bairro")));
                    listItem.add(new Label("cidade", new PropertyModel<Endereco>(listItem.getModel(),"cidade")));
                    listItem.add(new Label("estado", new PropertyModel<UF>(listItem.getModel(),"estado").getObject().getSigla()));

                    listItem.add(new AjaxLink<Void>("ajaxEditar") {
                        private static final long serialVersionUID = -660882723667008281L;
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            modalWindow.setPageCreator(new ModalWindow.PageCreator() {
                                private static final long serialVersionUID = 2885579755021559024L;
                                @Override
                                public Page createPage() {
                                    try {
                                        return new FormularioEndereco(modalWindow, idMonitorador, listItem.getModelObject().getId());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                            modalWindow.show(target);
                        }
                    });

                    listItem.add(new AjaxLink<Void>("ajaxExcluir") {
                        private static final long serialVersionUID = -660882723667008281L;
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            modalWindow.setPageCreator(new ModalWindow.PageCreator() {
                                private static final long serialVersionUID = 2885579755021559024L;
                                @Override
                                public Page createPage() {
                                    return new ConfirmationModal(
                                            listItem,
                                            "Tem certeza que deseja excluir o endereço: " + listItem.getModelObject().getId(),
                                            "excluirEndereco"
                                    );
                                }
                            });
                            modalWindow.show(target);
                        }
                    });
                }
            };
        } catch (Exception e){
            throw new RuntimeException("Não foi possível buscar seus endereços");
        }
    }
}