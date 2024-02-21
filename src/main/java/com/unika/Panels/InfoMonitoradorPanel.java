package com.unika.Panels;

import com.unika.model.Monitorador;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.io.Serial;

public class InfoMonitoradorPanel extends Panel {
    @Serial
    private static final long serialVersionUID = 2097601353745752544L;
    public InfoMonitoradorPanel(String id, Monitorador monitorador) {
        super(id);
        add(new Label("infoTitle", Model.of(monitorador.getEmail())));
    }
}
