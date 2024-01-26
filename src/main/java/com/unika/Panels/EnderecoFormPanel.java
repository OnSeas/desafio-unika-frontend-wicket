package com.unika.Panels;

import com.unika.forms.EnderecoForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class EnderecoFormPanel extends Panel {
    private static final long serialVersionUID = 8787731904574781396L;

    public EnderecoFormPanel(String id, EnderecoForm enderecoForm) {
        super(id);

        // Criando um endereço
        if(enderecoForm.getModelObject().getId() == null){
            add(new Label("enderecoFormTitle", Model.of("Criar novo endereço")));
        }
        // Editando um endereço
        else {
            add(new Label("enderecoFormTitle", Model.of("Editar endereço")));
        }

        add(enderecoForm);
    }
}
