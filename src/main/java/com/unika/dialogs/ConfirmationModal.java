package com.unika.dialogs;

import com.unika.model.Endereco;
import com.unika.model.Monitorador;
import com.unika.model.apiService.EnderecoApi;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;

import java.io.IOException;

public class ConfirmationModal extends WebPage {
    private static final long serialVersionUID = -2628184519158280658L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    EnderecoApi enderecoApi = new EnderecoApi();

    public ConfirmationModal(ListItem<?> listItem, String msg, String action){ // TODO resolver o action
        add(new Label("deleteMsg", Model.of(msg)));

        add(new AjaxLink<Void>("confirmAjax") {
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
                    }
                    ModalWindow.closeCurrent(target);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
        add(new AjaxLink<Void>("cancelarAjax") {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void desativarMontorador(Long id) {
        try {
            System.out.println("Desativando o monitorador: " + id);
            monitoradorApi.desativarMonitorador(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void ativarMontorador(Long id) {
        try {
            System.out.println("Ativando o monitorador: " + id);
            monitoradorApi.ativarMonitorador(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deletarEndereco(Long id){
        try {
            System.out.println("Excluindo o endereço: " + id);
            enderecoApi.deletarEndereco(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
