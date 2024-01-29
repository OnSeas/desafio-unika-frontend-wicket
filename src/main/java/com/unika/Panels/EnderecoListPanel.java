package com.unika.Panels;

import com.unika.dialogs.ConfirmationModal;
import com.unika.forms.EnderecoForm;
import com.unika.model.Endereco;
import com.unika.model.UF;
import com.unika.model.apiService.EnderecoApi;
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

import java.util.List;

public class EnderecoListPanel extends Panel {
    private static final long serialVersionUID = -1429184536413087837L;
    EnderecoApi enderecoApi = new EnderecoApi();
    WebMarkupContainer enderecoListWMC = new WebMarkupContainer("enderecoListWMC");
    ModalWindow modalWindow = new ModalWindow("modalEndereco");
    final Long idMonitorador;
    List<Endereco> enderecoList;

    // Editar Monitorador
    public EnderecoListPanel(String id, List<Endereco> enderecoList, Long idMonitorador) {
        super(id);
        this.enderecoList = enderecoList;
        this.idMonitorador = idMonitorador;

        // Para poder aparecer se não tiver nenhum
        enderecoListWMC.setOutputMarkupId(true);
        enderecoListWMC.setOutputMarkupPlaceholderTag(true);

        addList();
        add(enderecoListWMC);

        modalWindow.setOutputMarkupId(true);
        add(modalWindow);

        // Quando fecha o modal
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 7800205096545281286L;
            @Override
            public void onClose(AjaxRequestTarget target) {
                System.out.println(idMonitorador);
                System.out.println(enderecoList);
                if (idMonitorador != -1L){
                    recarregarList();
                    addList();
                    target.add(enderecoListWMC);
                } else {
                    addList();
                    target.add(enderecoListWMC);
                }
            }
        });
    }

    private void recarregarList(){
        try {
            enderecoList = enderecoApi.listarEnderecos(idMonitorador);
        } catch (Exception e){
            System.out.println("Erro ao recarregar a lista de enderecos");
        }
    }

    private void addList(){
        enderecoListWMC.removeAll();

        ListView<Endereco> enderecoListView = construirLista();
        enderecoListWMC.add(enderecoListView);
        enderecoListWMC.setVisible(!enderecoListView.getList().isEmpty());
    }

    // Construir a lista de endereços
    private ListView<Endereco> construirLista(){
        try{
            return new ListView<Endereco>("enderecos", enderecoList) {
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
                            modalWindow.setContent(new EnderecoFormPanel(ModalWindow.CONTENT_ID,
                                    new EnderecoForm("enderecoForm", listItem.getModelObject(), idMonitorador)));
                            modalWindow.show(target);
                        }
                    });

                    listItem.add(new AjaxLink<Void>("ajaxExcluir") {
                        private static final long serialVersionUID = -660882723667008281L;
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            modalWindow.setContent(new ConfirmationModal(
                                            ModalWindow.CONTENT_ID,
                                            listItem,
                                            "Tem certeza que deseja excluir o endereço: " + listItem.getModelObject().getId(),
                                            "excluirEndereco"
                                    ));
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