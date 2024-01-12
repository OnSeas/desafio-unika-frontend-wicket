package com.unika;

import com.unika.model.Monitorador;
import com.unika.model.apiService.MonitoradorApi;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.IOException;
import java.util.List;

public class ListarMonitoradores extends HomePage{
    private static final long serialVersionUID = 2178207601281324056L;
    final MonitoradorApi monitoradorApi = new MonitoradorApi();

    public ListarMonitoradores(PageParameters parameters) throws IOException {
        super(parameters);

        add(new Label("ListLabel", Model.of("Lista de monitoradores")));

        final WebMarkupContainer listWMC = new WebMarkupContainer("listWMC");
        listWMC.setOutputMarkupId(true);
        listWMC.add(new ListView<Monitorador>("monitoradores", monitoradorApi.listarMonitoradores()) {
            private static final long serialVersionUID = -7313164500893623865L;

            @Override
            protected void populateItem(final ListItem<Monitorador> listItem) {
                listItem.add(new Label("index", Model.of(listItem.getIndex() + 1)));
                listItem.add(new Label("tipoPessoa", new PropertyModel<Monitorador>(listItem.getModel(),"tipoPessoa")));
                listItem.add(new Label("email", new PropertyModel<Monitorador>(listItem.getModel(),"email")));
                listItem.add(new Label("dataNascimento", new PropertyModel<Monitorador>(listItem.getModel(),"dataNascimento")));
                listItem.add(new AjaxLink<Void>("ajaxEcluirMonitorador") {
                    private static final long serialVersionUID = -1679276620382639682L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        System.out.println("Excluindo o monitorador: " + listItem.getModelObject());
                        deletar(listItem.getModelObject().getId());
                        ((ListView<?>) listItem.getParent()).getList().remove(listItem.getModelObject());
                        target.add(listWMC);
                    }
                });
            }
        });
        add(listWMC);
    }

    private void deletar(Long idMonitorador){
        try {
            monitoradorApi.deletarMonitorador(idMonitorador);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
