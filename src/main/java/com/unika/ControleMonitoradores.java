package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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

    public ControleMonitoradores(FeedbackPanel feedbackPanel) {
        add(new AjaxEventBehavior("load") {
            @Serial
            private static final long serialVersionUID = -593491193031230528L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                feedbackPanel.getFeedbackMessages().toList().forEach(feedbackMessage -> success(feedbackMessage.getMessage())); // Não sei dizer o pq precisa fazer, mas é o jeito que consegui fazer funcionar
                target.add(getFeedbackPanel());
            }
        });

        add(new Label("ControlLabel", Model.of("Controle de monitoradores")));

        // WMC que envolve a lista
        listWMC.setOutputMarkupId(true);
        listWMC.add(new MonitoradorListPanel("monitoradorListPanel", getFeedbackPanel()));
        add(listWMC);
    }
}
