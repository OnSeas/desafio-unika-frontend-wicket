package com.unika.components.listagem;

import com.unika.BasePage;
import com.unika.apiService.MonitoradorApi;
import com.unika.model.Monitorador;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.markup.html.basic.Label;
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
        Label labelAdd = new Label("ListarMonitorador", Model.of("Lista de monitoradores"));
        add(labelAdd);
        BootstrapButton editarButton = new BootstrapButton("botaoEditar", Buttons.Type.Primary);

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
