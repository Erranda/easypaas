/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import com.withinet.opaas.WicketApplication;

/**
 * @author Folarin
 *
 */
public abstract class Stateless extends WebPage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3400154971882096429L;

	/**
	 * 
	 */
	public Stateless() {
		StatelessLink home = new StatelessLink("appNameLink") {
            public void onClick() {
                setResponsePage(WicketApplication.get().getHomePage());
            }
        };
        home.add(new Label ("appName", "Withinet OSGi Cloud"));
        add(home);
		
        StatelessLink register = new StatelessLink("register") {
            public void onClick() {
                setResponsePage(Register.class);
            }
        };
        add (register);
        
        StatelessLink cpanel = new StatelessLink("cpanel") {
            public void onClick() {
            	setResponsePage(ProjectIndex.class);
            }
        };
        add (cpanel);
	}

	protected void setPageTitle (IModel model) {
		get ("pageTitle").setDefaultModel (model);
	}
}
