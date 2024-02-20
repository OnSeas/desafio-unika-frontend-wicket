package com.unika;

import com.unika.Panels.MonitoradorListEndPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serial;

public class ControleEnderecos extends HomePage{
    @Serial
    private static final long serialVersionUID = -3966165458128096317L;

    public ControleEnderecos(PageParameters parameters) {
        super(parameters);

        add(new MonitoradorListEndPanel("monitoradoresList"));
    }
}
