package com.withinet.opaas.wicket.html;

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;

public abstract class ConfirmationButton extends IndicatingAjaxButton
{
    private static final long serialVersionUID = 1L;
    private final String text;
 
    public ConfirmationButton( String id, String text )
    {
        super( id );
        this.text = text;
    }
    
    public ConfirmationButton( String id, String text, Form form )
    {
        super(id, form);
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