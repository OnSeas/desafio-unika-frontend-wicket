package com.unika.components;

import com.unika.BasePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.palette.theme.DefaultTheme;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class Index extends BasePage {
    private static final long serialVersionUID = 7306463745845254610L;

    public Index(){
        add(new Label("id", Model.of("Teste!")));

        final ModalWindow modal = new ModalWindow("modal");
        modal.add(new DefaultTheme());
        Label label = new Label(ModalWindow.CONTENT_ID, "I'm a modal window!");

        modal.setContent(label);

        add(modal);
        add(new AjaxLink<Void>("open") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                modal.show(ajaxRequestTarget);
            }
        });
    }
}
