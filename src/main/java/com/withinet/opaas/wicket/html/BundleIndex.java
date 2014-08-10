package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.WebPage;

/**
 * @author Martijn Dashorst
 */
public class BundleIndex extends Authenticated
{
    /**
     * Constructor.
     */
    public BundleIndex ()
    {
        add (new BundleTableWidget ("bundle-table-widget"));
    }
}