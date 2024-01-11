package com.unika;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ListarMonitoradores extends HomePage{
    private static final long serialVersionUID = 2178207601281324056L;

    public ListarMonitoradores(PageParameters parameters) {
        super(parameters);

        add(new Label("ListLabel", Model.of("Lista de monitoradores")));
    }
}
