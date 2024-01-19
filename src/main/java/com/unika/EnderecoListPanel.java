package com.unika;

import com.unika.model.Endereco;
import com.unika.model.UF;
import com.unika.model.apiService.EnderecoApi;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class EnderecoListPanel extends Panel {
    private static final long serialVersionUID = -1429184536413087837L;
    EnderecoApi enderecoApi = new EnderecoApi();
    WebMarkupContainer enderecoListWMC = new WebMarkupContainer("enderecoListWMC");

    // Editar Monitorador
    public EnderecoListPanel(String id, Long idMonitorador) {
        super(id);
        enderecoListWMC.setVisible(true);
        ListView<Endereco> enderecoListView = construirLista(idMonitorador);
        enderecoListWMC.add(enderecoListView);
        enderecoListWMC.setVisible(!enderecoListView.getList().isEmpty());
        add(enderecoListWMC);
    }

    // Construir a lista de endereços
    private ListView<Endereco> construirLista(Long idMonitorador){
        try{
            return new ListView<Endereco>("enderecos", enderecoApi.listarEnderecos(idMonitorador)) {
                private static final long serialVersionUID = 7611437418587202266L;

                @Override
                protected void populateItem(ListItem<Endereco> listItem) {
                    listItem.add(new Label("index", Model.of(listItem.getIndex() + 1)));
                    listItem.add(new Label("endereco", new PropertyModel<Endereco>(listItem.getModel(),"endereco")));
                    listItem.add(new Label("cep", new PropertyModel<Endereco>(listItem.getModel(),"cep")));
                    listItem.add(new Label("bairro", new PropertyModel<Endereco>(listItem.getModel(),"bairro")));
                    listItem.add(new Label("cidade", new PropertyModel<Endereco>(listItem.getModel(),"cidade")));
                    listItem.add(new Label("estado", new PropertyModel<UF>(listItem.getModel(),"estado").getObject().getSigla()));
                }
            };
        } catch (Exception e){
            throw new RuntimeException("Não foi possível buscar seus endereços");
        }
    }
}
