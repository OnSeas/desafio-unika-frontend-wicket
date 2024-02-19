package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import com.unika.forms.ImportFormPanel;
import com.unika.forms.MonitoradorFormPanel;
import com.unika.model.Monitorador;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ControleMonitoradores extends HomePage{
    @Serial
    private static final long serialVersionUID = 2178207601281324056L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();
    final ModalWindow modalWindow = new ModalWindow("modaw");
    final WebMarkupContainer listWMC = new WebMarkupContainer("listWMC");
    FeedbackPanel feedbackPanelSuccess;
    FeedbackPanel feedbackPanelError;
    List<FeedbackPanel> feedbackPanels = new ArrayList<>();

    public ControleMonitoradores(PageParameters parameters) {
        super(parameters);

        // FEEDBACK PANELS CONFIG
        feedbackPanelSuccess = new FeedbackPanel("feedbackPanelSuccess"){
            @Serial
            private static final long serialVersionUID = 8923533799419175857L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                this.setVisible(anyMessage(250) && !anyErrorMessage()); // Não consigo entender pq sem a segunda validação aparece as msg de erro tbm (ps: esse mostra qualquer msg que não seja de erro mesmo usando o 250(success) como parâmetro.
                if(this.isVisible()){
                    // Todo Descobrir como fazer para o feedbackPanel sumir sozinho depois de alguns segundos.
                }
            }
        };
        feedbackPanelSuccess.add(new AjaxEventBehavior("click") {
            @Serial
            private static final long serialVersionUID = 3239147858404127676L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                feedbackPanelSuccess.setVisible(false);
                target.add(feedbackPanelSuccess);
            }
        });
        add(feedbackPanelSuccess);

        feedbackPanelError = new FeedbackPanel("feedbackPanelError"){
            @Serial
            private static final long serialVersionUID = -706855642723453811L;
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.setOutputMarkupId(true);
                this.setOutputMarkupPlaceholderTag(true);
                this.setVisible(anyErrorMessage());
            }
        };
        feedbackPanelError.add(new AjaxEventBehavior("click") {
            @Serial
            private static final long serialVersionUID = 4186326921821554339L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                feedbackPanelError.setVisible(false);
                target.add(feedbackPanelError);
            }
        });
        add(feedbackPanelError);

        feedbackPanels.add(feedbackPanelSuccess);
        feedbackPanels.add(feedbackPanelError);


        // Modal que é usado em todos pop-ups
//        modalWindow.setCookieName("modal-window-1");
        modalWindow.setResizable(false);
        add(modalWindow);

        // WMC que envolve a lista já inciando a lista geral
        listWMC.setOutputMarkupId(true);
        try {
            listWMC.add(new MonitoradorListPanel("monitoradorListPanel", monitoradorApi.listarMonitoradores(), feedbackPanels));
        } catch (IOException e) {
            error(e.getMessage());
        }
        add(listWMC);

        add(new Label("ControlLabel", Model.of("Controle de monitoradores")));

        // Botão de criar novo monitorador
        add( new AjaxLink<Void>("formNovoMonitorador") {
            @Serial
            private static final long serialVersionUID = -387605849215267697L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.setInitialWidth(55);
                modalWindow.setWidthUnit("%");
                modalWindow.setInitialHeight(400);
                modalWindow.setContent(new MonitoradorFormPanel(modalWindow.getContentId(),
                        new Monitorador()));
                modalWindow.show(target);
            }
        });

        add(new AjaxLink<Void>("importarMonitoradores") {
            @Serial
            private static final long serialVersionUID = -7139346449600703225L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.setInitialWidth(35);
                modalWindow.setWidthUnit("%");
                modalWindow.setInitialHeight(210);
                modalWindow.setContent(new ImportFormPanel(modalWindow.getContentId()));
                modalWindow.show(target);
            }
        });

        // Função quando alguma modal window é fechada.
        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
            @Serial
            private static final long serialVersionUID = 1L;
            @Override
            public void onClose(AjaxRequestTarget target){
                try {
                    putPageableList(monitoradorApi.listarMonitoradores());
                    feedbackPanels.forEach(target::add);
                } catch (Exception e){
                    error(e.getMessage());
                }
                target.add(listWMC);
            }
        });
    }

    // ADCIONA A LISTA COM PAGING
    private void putPageableList(List<Monitorador> monitoradores) throws IOException {
        // remove se já existir alguma coisa
        listWMC.removeAll();

        // Adciona a lista paginada (Panel)
        listWMC.add(new MonitoradorListPanel("monitoradorListPanel", monitoradores, feedbackPanels));
    }
}
