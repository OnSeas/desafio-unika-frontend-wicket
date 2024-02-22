package com.unika.dialogs;

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import java.io.Serial;


// Fonte: https://cwiki.apache.org/confluence/display/WICKET/Getting+user+confirmation
public abstract class ConfirmationLink<T> extends AjaxLink<T> {
    @Serial
    private static final long serialVersionUID = -7900767262884771026L;
    private final String text;

    public ConfirmationLink(String id, String text )
    {
        super( id );
        this.text = text;
    }

    @Override
    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
    {
        super.updateAjaxAttributes( attributes );

        AjaxCallListener ajaxCallListener = new AjaxCallListener();
        ajaxCallListener.onPrecondition( "return confirm('" + text + "');" );
        attributes.getAjaxCallListeners().add( ajaxCallListener );
    }
}