package com.unika;

import com.unika.forms.MonitoradorFormPanel;
import com.unika.model.Monitorador;

import java.io.Serial;

public class MonitoradorForm extends HomePage{
    @Serial
    private static final long serialVersionUID = -1612787591188054427L;

    public MonitoradorForm(){
        add(new MonitoradorFormPanel("monitoradorForm", new Monitorador(), getFeedbackPanel()));
    }
    public MonitoradorForm(Monitorador monitorador){
        add(new MonitoradorFormPanel("monitoradorForm", monitorador, getFeedbackPanel()));
    }

}
