package com.unika.Panels;

import com.unika.forms.EnderecoForm;
import com.unika.forms.MonitoradorForm;
import com.unika.model.Endereco;
import com.unika.model.TipoPessoa;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class MonitoradorFormPanel extends Panel {
    private static final long serialVersionUID = -8284617783524484913L;
    WebMarkupContainer listaEnderecoWMC = new WebMarkupContainer("listaEnderecoWMC");
    final MonitoradorForm monitoradorForm;

    public MonitoradorFormPanel(String id, MonitoradorForm monitoradorForm) {
        super(id);
        this.monitoradorForm = monitoradorForm;

        // Criando um novo monitorador
        if(monitoradorForm.getModelObject().getId() == null) {
            add(new Label("monitoradorFormTitle", Model.of("Cadastrar um novo monitorador")));

            add(addCriarEndButton(-1L));
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

            add(addCriarEndButton(monitoradorForm.getModelObject().getId()));
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
                monitoradorForm.getModelObject().getId()));
    }

    EnderecoForm enderecoForm;


    // Botão criar novo monitorador
    private AjaxLink<Void> addCriarEndButton(Long idMonitorador){

        // Modal Window que abre o formulário de endereço
        ModalWindow modalWindow = new ModalWindow("endFormModal");
        modalWindow.setCookieName("criarendereco-modal");
        add(modalWindow);
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 1989574373953651865L;
            @Override
            public void onClose(AjaxRequestTarget target){ // TODO Conferir se monitorador foi submetido.
                monitoradorForm.enderecoList.add(enderecoForm.getModelObject());
                System.out.println(monitoradorForm.enderecoList);

                addListaEndereco();
                target.add(listaEnderecoWMC);
            }
        });

        AjaxLink<Void> criarEnderecoAjax =  new AjaxLink<Void>("criarEndereco") {
            private static final long serialVersionUID = 7923685135874148873L;
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

        return criarEnderecoAjax;
    }

}
