package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.io.IOException;
import java.io.Serial;

public class ControleMonitoradores extends HomePage{
    @Serial
    private static final long serialVersionUID = 2178207601281324056L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();
    final WebMarkupContainer listWMC = new WebMarkupContainer("listWMC");

    public ControleMonitoradores() {
        add(new Label("ControlLabel", Model.of("Controle de monitoradores")));

        // WMC que envolve a lista já inciando a lista geral
        listWMC.setOutputMarkupId(true);
        try {
            listWMC.add(new MonitoradorListPanel("monitoradorListPanel", monitoradorApi.listarMonitoradores(), getFeedbackPanel()));
        } catch (IOException e) {
            error(e.getMessage());
        }
        add(listWMC);
    }
}
