package com.unika.dialogs;

import com.unika.model.Endereco;
import com.unika.model.Monitorador;
import com.unika.model.apiService.EnderecoApi;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.io.Serial;

public class ConfirmationModal extends Panel {
    @Serial
    private static final long serialVersionUID = -2628184519158280658L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    EnderecoApi enderecoApi = new EnderecoApi();

    public ConfirmationModal(String id, ListItem<?> listItem, String msg, String action){ // TODO resolver o action
        super(id);
        add(new Label("deleteMsg", Model.of(msg)));

        add(new AjaxLink<Void>("confirmAjax") {
            @Serial
            private static final long serialVersionUID = -7950244595795172311L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                try {
                    switch (action){
                        case "excluir":
                            deletarMontorador(((Monitorador) listItem.getModelObject()).getId());
                            break;
                        case "desativar":
                            desativarMontorador(((Monitorador) listItem.getModelObject()).getId());
                            break;
                        case "ativar":
                            ativarMontorador(((Monitorador) listItem.getModelObject()).getId());
                            break;
                        case "excluirEndereco":
                            deletarEndereco(((Endereco) listItem.getModelObject()).getId());
                            break;
                        case "tornarEndPrincipal":
                            tornarEndPrincipal((ListItem<Endereco>) listItem);
                    }
                    ModalWindow.closeCurrent(target);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
        add(new AjaxLink<Void>("cancelarAjax") {
            @Serial
            private static final long serialVersionUID = -7950244595795172311L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                ModalWindow.closeCurrent(target);
            }
        });
    }

    private void deletarMontorador(Long id) {
        try {
            System.out.println("Excluindo o monitorador: " + id);
            monitoradorApi.deletarMonitorador(id);
            success("Monitorador excluído!");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void desativarMontorador(Long id) {
        try {
            System.out.println("Desativando o monitorador: " + id);
            monitoradorApi.desativarMonitorador(id);
            success("Monitorador Desativado!");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void ativarMontorador(Long id) {
        try {
            System.out.println("Ativando o monitorador: " + id);
            monitoradorApi.ativarMonitorador(id);
            success("Monitorador Ativado!");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void deletarEndereco(Long id){
        try {
            enderecoApi.deletarEndereco(id);
            success("Endereço Excluído!");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void tornarEndPrincipal(ListItem<Endereco> listItem){
        try {
            success(enderecoApi.tornarPrincipal(
                    listItem.getModelObject().getMonitoradorId(),
                    listItem.getModelObject().getId()
            ));
        } catch (Exception e) {
            error(e.getMessage());
        }
    }
}
