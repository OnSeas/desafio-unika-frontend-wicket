package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.IOException;

public class ControlePessoaFisica  extends HomePage{
    private static final long serialVersionUID = 4525142743173919311L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();

    public ControlePessoaFisica(PageParameters parameters) {
        super(parameters);

        add(new Label("pfTitle", Model.of("Controle de Pessoas Físicas")));

        try {
            add(new MonitoradorListPanel("pfList", monitoradorApi.buscarPF()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
