package com.unika;

import com.unika.Panels.MonitoradorListPanel;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.IOException;

public class ControlePessoaJuridica extends HomePage{
    private static final long serialVersionUID = 2056880168526927305L;

    MonitoradorApi monitoradorApi = new MonitoradorApi();

    public ControlePessoaJuridica(PageParameters parameters) {
        super(parameters);

        add(new Label("pjTtile", Model.of("Controle de Pessoas Jurídicas")));

        try {
            add(new MonitoradorListPanel("pjList", monitoradorApi.buscarPJ()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
