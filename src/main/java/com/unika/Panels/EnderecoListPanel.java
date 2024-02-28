package com.unika.Panels;

import com.unika.dialogs.ConfirmationLink;
import com.unika.forms.EnderecoFormPanel;
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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class EnderecoListPanel extends Panel {
    @Serial
    private static final long serialVersionUID = -1429184536413087837L;
    final EnderecoApi enderecoApi = new EnderecoApi();
    final FeedbackPanel feedbackPanel;
    final ModalWindow modalWindow = new ModalWindow("modalEnd");
    final Long idMonitorador;
    private List<Endereco> enderecoList;
    WebMarkupContainer enderecoListWMC = new WebMarkupContainer("enderecoListWMC"){
        @Serial
        private static final long serialVersionUID = -5217040513869614038L;
        @Override
        protected void onConfigure() {
            super.onConfigure();
            this.setOutputMarkupId(true);
            this.setOutputMarkupPlaceholderTag(true);
            this.setVisible(!enderecoList.isEmpty());
        }
    };
    Endereco endereco;
    formType FLAG;
    boolean temEndPrincipal = false;

    // Editar Monitorador
    public EnderecoListPanel(String id, Long idMonitorador, FeedbackPanel feedbackPanel) {
        super(id);
        this.idMonitorador = idMonitorador;
        this.feedbackPanel = feedbackPanel;

        add(new AjaxLink<Void>("criarEndereco") { // Criar novo endereço
            @Serial
            private static final long serialVersionUID = -7867338417724841250L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                setModalFormSize();
                endereco = new Endereco();
                FLAG = formType.CREATE;

                if (enderecoList.size() >= 3){
                    error("Este monitorador já possuí o número maximo de endereços: 3.");
                    target.add(feedbackPanel);
                }
                else {
                    modalWindow.setContent(new EnderecoFormPanel(modalWindow.getContentId(), endereco, idMonitorador, feedbackPanel));
                    modalWindow.show(target);
                }
            }
        });

        try {
            enderecoList = enderecoApi.listarEnderecos(idMonitorador);
        } catch (Exception e){
            enderecoList = new ArrayList<>();
        }

        enderecoListWMC.add(construirLista());
        add(enderecoListWMC);

        modalWindow.setResizable(false);
        add(modalWindow);

        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Serial
            private static final long serialVersionUID = 3681913398232804854L;
            @Override
            public void onClose(AjaxRequestTarget target) {
                System.out.println(endereco);
                temEndPrincipal = false;
                if(FLAG.equals(formType.CREATE) && endereco.getCep() != null){
                    enderecoList.forEach(e -> {if(e.getPrincipal()) temEndPrincipal = true;});
                    endereco.setPrincipal(!temEndPrincipal);

                    if (enderecoList.size() >=3) feedbackPanel.error("Este monitorador já possuí o número maximo de endereços: 3.");
                    else enderecoList.add(endereco);
                }
                target.add(enderecoListWMC, feedbackPanel);
            }
        });
    }

    // Construir a lista de endereços
    private ListView<Endereco> construirLista(){
        try{
            return new ListView<>("enderecos", enderecoList) {
                @Serial
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
                        @Serial
                        private static final long serialVersionUID = 2580172425403012021L;
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            FLAG = formType.EDIT;
                            setModalFormSize();
                            modalWindow.setContent(new EnderecoFormPanel(
                                modalWindow.getContentId(),
                                    listItem.getModelObject(),
                                    idMonitorador,
                                    feedbackPanel)); // Endereco
                            modalWindow.show(target);
                        }
                    });
                    listItem.add(new ConfirmationLink<>("ajaxExcluir", "Tem certeza que deseja excluir este endereço?") {
                        @Serial
                        private static final long serialVersionUID = -660882723667008281L;
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            deletarEndereco(listItem.getModelObject());
                            target.add(enderecoListWMC, feedbackPanel);
                        }
                    });
                    Label principalButton = new Label("principalButton", Model.of("Principal"));
                    listItem.add(principalButton);
                    listItem.add(new ConfirmationLink<>("TornarPrincipal", "Tem ceterteza que deseja tornar este seu endereço principal") {
                        @Serial
                        private static final long serialVersionUID = -7811189455403194862L;
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            tornarEndPrincipal(listItem.getModelObject());
                            target.add(enderecoListWMC, feedbackPanel);
                        }
                        @Override
                        protected void onConfigure() {
                            super.onConfigure();
                            this.setVisible(!listItem.getModelObject().getPrincipal());
                            if (this.isVisible()){
                                principalButton.setVisible(false);
                            }
                        }
                    });
                }
            };
        } catch (Exception e){
            throw new RuntimeException("Não foi possível buscar seus endereços");
        }
    }

    private void setModalFormSize(){
        modalWindow.setInitialWidth(40);
        modalWindow.setWidthUnit("%");
        modalWindow.setInitialHeight(360);
    }

    // Em caso de edenreço.getId=null (Quando for um novo monitorador) não manda para o banco de dados
    private void tornarEndPrincipal(Endereco endereco){
            try {
                if (endereco.getId() != null) enderecoApi.tornarPrincipal(endereco.getId());
                success("O endereço " + endereco.getEndereco() + " agora é o seu endereço principal.");

                enderecoList.forEach(end -> end.setPrincipal(false));
                endereco.setPrincipal(true);
            } catch (Exception e) {
                error(e.getMessage());
            }

    }

    private void deletarEndereco(Endereco endereco){
        if (enderecoList.size() <=1) error("Monitorador não pode ficar sem endereços.");
        else try {
            if (endereco.getId() != null) success(enderecoApi.deletarEndereco(endereco.getId()));
            else success("O endereço " + endereco.getEndereco() + " foi removido.");

            enderecoList.remove(endereco);
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    public List<Endereco> getEnderecoList(){
        return this.enderecoList;
    }

    private enum formType{
        CREATE(0),
        EDIT(1);

        final int value;
        formType(int value) {
            this.value = value;
        }
    }
}