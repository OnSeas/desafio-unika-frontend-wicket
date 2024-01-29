package com.unika.Panels;

import com.unika.forms.EnderecoForm;
import com.unika.forms.MonitoradorForm;
import com.unika.model.Endereco;
import com.unika.model.TipoPessoa;
import com.unika.model.apiService.EnderecoApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.io.IOException;

public class MonitoradorFormPanel extends Panel {
    private static final long serialVersionUID = -8284617783524484913L;
    WebMarkupContainer listaEnderecoWMC = new WebMarkupContainer("listaEnderecoWMC");
    final MonitoradorForm monitoradorForm;
    EnderecoApi enderecoApi = new EnderecoApi();

    Long idMonitorador;

    public MonitoradorFormPanel(String id, MonitoradorForm monitoradorForm) {
        super(id);
        this.monitoradorForm = monitoradorForm;

        // Criando um novo monitorador
        if(monitoradorForm.getModelObject().getId() == null) {
            add(new Label("monitoradorFormTitle", Model.of("Cadastrar um novo monitorador")));
            this.idMonitorador = -1L;

            add(addCriarEndButton());
        }
        // Editando um monitorador existente
        else {
            add(new Label("monitoradorFormTitle", Model.of("Editar info do monitorador")));

            WebMarkupContainer wmc;
            if (monitoradorForm.getModelObject().getTipoPessoa().equals(TipoPessoa.PESSOA_FISICA)){
                wmc = (WebMarkupContainer) monitoradorForm.get("pfWMC");
            } else{
                wmc = (WebMarkupContainer) monitoradorForm.get("pjWMC");
            }
            wmc.setVisible(true);

            this.idMonitorador = monitoradorForm.getModelObject().getId();

            add(addCriarEndButton());
        }
        add(monitoradorForm);


        // Configuração da lista de Endereços
        listaEnderecoWMC.setOutputMarkupId(true);
        listaEnderecoWMC.setOutputMarkupPlaceholderTag(true);
        addListaEndereco();
        add(listaEnderecoWMC);
    }

    private void addListaEndereco(){
        listaEnderecoWMC.removeAll();

        listaEnderecoWMC.setVisible(!monitoradorForm.enderecoList.isEmpty());

        listaEnderecoWMC.add(new EnderecoListPanel("enderecoPanel",
                monitoradorForm.enderecoList,
                idMonitorador));
    }

    EnderecoForm enderecoForm;

    // Botão criar novo monitorador
    private AjaxLink<Void> addCriarEndButton(){

        // Modal Window que abre o formulário de endereço
        ModalWindow modalWindow = new ModalWindow("endFormModal");
        modalWindow.setCookieName("criarendereco-modal");
        add(modalWindow);
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 1989574373953651865L;
            @Override
            public void onClose(AjaxRequestTarget target){
                if (enderecoForm.submited == Boolean.TRUE && enderecoForm.idMonitorador == -1L){
                    monitoradorForm.enderecoList.add(enderecoForm.getModelObject());
                }else {
                    try {
                        monitoradorForm.enderecoList = enderecoApi.listarEnderecos(enderecoForm.idMonitorador);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                addListaEndereco();
                target.add(listaEnderecoWMC);
            }
        });

        return new AjaxLink<Void>("criarEndereco") {
            private static final long serialVersionUID1 = 7923685135874148873L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                enderecoForm = new EnderecoForm(
                        "enderecoForm",
                        new Endereco(),
                        idMonitorador);

                modalWindow.setContent(new EnderecoFormPanel(ModalWindow.CONTENT_ID, enderecoForm));
                modalWindow.show(target);
            }
        };
    }

}
