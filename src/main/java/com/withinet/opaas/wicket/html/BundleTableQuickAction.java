/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Folarin
 *
 */
public class BundleTableQuickAction extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8155156904127650558L;

	public BundleTableQuickAction(String id, WebMarkupContainer deleteBundleLink, WebMarkupContainer updateBundleLink) {
		super(id);
		add (updateBundleLink);
		add (deleteBundleLink);
	}
	
	private class BundleUpdateForm extends Form<Void> {

		public BundleUpdateForm(String id) {
			super(id);
			
			final CSSFeedbackPanel feedback = new CSSFeedbackPanel("feedback");
			feedback.setOutputMarkupPlaceholderTag(true);
			add(feedback);
			
			
		}
		
	}
	
}
