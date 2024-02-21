package com.unika;

import com.unika.Panels.EnderecoListPanel;
import com.unika.forms.MonitoradorFormPanel;
import com.unika.model.Monitorador;
import org.apache.wicket.markup.html.panel.EmptyPanel;

import java.io.Serial;

public class MonitoradorPage extends HomePage{
    @Serial
    private static final long serialVersionUID = -171359577333289584L;

    public MonitoradorPage(){
        adcionarContent(new Monitorador());
    }

    public MonitoradorPage(Monitorador monitorador){
        adcionarContent(monitorador);
    }

    private void adcionarContent(Monitorador monitorador){
        add(new MonitoradorFormPanel("monitoradorFormPanel", monitorador, getFeedbackPanel()));

        if (monitorador.getId() == null) add(new EmptyPanel("endListPanel"));
        else add(new EnderecoListPanel("endListPanel", monitorador.getId(), getFeedbackPanel()));
    }
}
