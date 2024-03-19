package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serial;

public class ListarMonitoradores extends HomePage{
    @Serial
    private static final long serialVersionUID = 2677937176959960357L;

    public ListarMonitoradores(){
        add(new MonitoradorListPanel("listMonitorador", getFeedbackPanel()));
    }

    public ListarMonitoradores(String feedback){
        if (!feedback.isBlank()){
            add( new AjaxEventBehavior("load") {
                @Serial
                private static final long serialVersionUID = 5137470672715434781L;
                @Override
                protected void onEvent(final AjaxRequestTarget target) {
                    success(feedback);
                    target.add(getFeedbackPanel());
                }
            });
        }
        add(new MonitoradorListPanel("listMonitorador", getFeedbackPanel()));
    }
}
