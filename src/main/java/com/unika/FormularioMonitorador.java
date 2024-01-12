package com.unika;

import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class FormularioMonitorador extends WebPage {
    private static final long serialVersionUID = 8415273463108359061L;

    MonitoradorApi monitoradorApi = new MonitoradorApi();

    FormularioMonitorador(ListarMonitoradores listarMonitoradores, ModalWindow modalWindow){
        add(new Label("monitoradorForm", Model.of("Cadastrar um novo monitorador")));
    }

    FormularioMonitorador(ListarMonitoradores listarMonitoradores, ModalWindow modalWindow, Long idMonitorador){
        add(new Label("monitoradorForm", Model.of("Editar info do monitorador")));
    }

}
