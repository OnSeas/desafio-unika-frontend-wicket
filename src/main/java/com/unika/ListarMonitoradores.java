package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serial;

public class ListarMonitoradores extends HomePage {
    @Serial
    private static final long serialVersionUID = 2677937176959960357L;

    public ListarMonitoradores() {
        add(new MonitoradorListPanel("listMonitorador", getFeedbackPanel()));
    }

    public ListarMonitoradores(String feedback) {
        AjaxEventBehavior ajaxEventBehavior = new AjaxEventBehavior("load") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                success(feedback);
                target.add(getFeedbackPanel());
                remove(this);
            }
        };
        if (!feedback.isBlank()) add(ajaxEventBehavior);

        add(new MonitoradorListPanel("listMonitorador", getFeedbackPanel()));
    }
}
