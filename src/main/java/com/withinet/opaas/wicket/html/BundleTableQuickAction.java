/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Folarin
 *
 */
public class BundleTableQuickAction extends Panel {

	public BundleTableQuickAction(String id, WebMarkupContainer deleteBundleLink, WebMarkupContainer updateBundleLink) {
		super(id);
		add (updateBundleLink);
		add (deleteBundleLink);
	}

}
