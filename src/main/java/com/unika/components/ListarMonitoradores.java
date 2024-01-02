package com.unika.components;

import com.unika.BasePage;
import com.unika.apiService.MonitoradorApi;
import com.unika.model.Monitorador;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.Model;

import java.io.IOException;
import java.util.List;

public class ListarMonitoradores extends BasePage {
    private static final long serialVersionUID = -2204567803298049884L;
    MonitoradorApi monitoradorApi = new MonitoradorApi();
    List<Monitorador> monitoradores = monitoradorApi.listarMonitoradores();

    public ListarMonitoradores() throws IOException {
        Label labelAdd = new Label("ListarMonitorador", Model.of("Listar Monitoradores"));
        add(labelAdd);

        PropertyListView<Monitorador> listaResultados = new PropertyListView<Monitorador>("Monitoradores", monitoradores) {
            private static final long serialVersionUID = 4998428137099886307L;

            @Override
            protected void populateItem(ListItem<Monitorador> listItem) {
                listItem.add(new Label("id")); // TODO Mudar para Count.
                listItem.add(new Label("tipoPessoa"));
                listItem.add(new Label("email"));
                listItem.add(new Label("dataNascimento"));
            }
        };
        add(listaResultados);

    }
}
