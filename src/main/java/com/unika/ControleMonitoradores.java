package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.io.Serial;

public class ControleMonitoradores extends HomePage{
    @Serial
    private static final long serialVersionUID = 2178207601281324056L;
    final WebMarkupContainer listWMC = new WebMarkupContainer("listWMC");

    public ControleMonitoradores() {
        add(new Label("ControlLabel", Model.of("Controle de monitoradores")));

        // WMC que envolve a lista
        listWMC.setOutputMarkupId(true);
        listWMC.add(new MonitoradorListPanel("monitoradorListPanel", getFeedbackPanel()));
        add(listWMC);
    }
}
