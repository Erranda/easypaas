package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.WebPage;

/**
 * @author Martijn Dashorst
 */
public class AccountIndex extends Authenticated
{
    /**
     * Constructor.
     */
    public AccountIndex ()
    {
        add (new AccountUpdateWidget ("account-update-widget"));
    }
}