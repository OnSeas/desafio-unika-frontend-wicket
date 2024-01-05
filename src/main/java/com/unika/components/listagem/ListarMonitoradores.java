package com.unika.components.listagem;

import com.unika.BasePage;
import com.unika.apiService.MonitoradorApi;
import com.unika.model.Monitorador;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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

        PropertyListView<Monitorador> listaResultados = new PropertyListView<Monitorador>("monitoradores", monitoradores) {
            private static final long serialVersionUID = 4998428137099886307L;

            @Override
            protected void populateItem(ListItem<Monitorador> listItem) {
                listItem.add(new Label("id")); //TODO Mudar para Count.
                listItem.add(new Label("tipoPessoa"));
                listItem.add(new Label("email"));
                listItem.add(new Label("dataNascimento"));

                // TODO Botão que de fato excluí, mas não recarrega a página enquanto não reiniciar o programa
                AjaxLink ajaxLinkExcluir = getBotaoExcluir(listItem.getModelObject().getId());
                listItem.add(ajaxLinkExcluir);

                AjaxLink ajaxLinkEditar = getBotaoEditar(listItem.getModelObject().getId());
                listItem.add(ajaxLinkEditar);
            }
        };
        add(listaResultados);
    }

    private AjaxLink getBotaoEditar(Long idMonitorador){
        return new AjaxLink<>("botaoEditar") {
            private static final long serialVersionUID = -8696846673137103920L;

            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                // TODO abrir formulário para editar monitorador
                System.out.println("PEDIU PARA EDITAR O MONITORADOR: " + idMonitorador);
            }
        };
    }

    private AjaxLink getBotaoExcluir(Long idMonitorador){
        return new AjaxLink<>("botaoExcluir") {
            private static final long serialVersionUID = -8696846673137103920L;

            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                try {
                    deletarMonitorador(idMonitorador);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
    }

    private void deletarMonitorador(Long idMonitorador) throws IOException {
        try {
            monitoradorApi.deletarMonitorador(idMonitorador);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
