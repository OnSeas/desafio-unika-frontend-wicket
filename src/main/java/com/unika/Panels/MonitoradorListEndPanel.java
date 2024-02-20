package com.unika.Panels;

import com.unika.model.Endereco;
import com.unika.model.Monitorador;
import com.unika.model.apiService.EnderecoApi;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class MonitoradorListEndPanel extends Panel {
    @Serial
    private static final long serialVersionUID = 4403031067842706116L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    EnderecoApi enderecoApi = new EnderecoApi();
    WebMarkupContainer webMarkupContainer = new WebMarkupContainer("enderecoPanel");

    public MonitoradorListEndPanel(String id) {
        super(id);

        webMarkupContainer.add(new EmptyPanel("enderecoListPanel"));
        webMarkupContainer.setOutputMarkupId(true);
        webMarkupContainer.setOutputMarkupPlaceholderTag(true);
        add(webMarkupContainer);

        List<Monitorador> monitoradorList;
        try {
            monitoradorList = monitoradorApi.listarMonitoradores();
        } catch (IOException e) {
            monitoradorList = new ArrayList<>();
            throw new RuntimeException(e);
        }

        Form<Monitorador> selectMonitoradorForm = new Form<>("selectForm", new CompoundPropertyModel<>(new Monitorador()));
        TextField<Long> idInput = new TextField<>("id");
        selectMonitoradorForm.add(idInput);
        add(selectMonitoradorForm);

        selectMonitoradorForm.add(new AjaxButton("ajaxSubmit") {
            @Serial
            private static final long serialVersionUID = 7984847538804302893L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                System.out.println(selectMonitoradorForm.getModelObject());
                List<Endereco> enderecoList;
                try {
                    enderecoList = enderecoApi.listarEnderecos(selectMonitoradorForm.getModelObject().getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                webMarkupContainer.removeAll();
                webMarkupContainer.add(new EnderecoListPanel("enderecoListPanel", enderecoList, selectMonitoradorForm.getModelObject().getId(), new ArrayList<>()));
                target.add(webMarkupContainer);
            }
        });
    }
}
