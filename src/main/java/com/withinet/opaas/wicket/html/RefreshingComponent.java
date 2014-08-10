package com.withinet.opaas.wicket.html;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class RefreshingComponent extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5910219263552469505L;

	public RefreshingComponent(String id) {
		super(id, new  RefreshingComponentModel ());
	}
	
	private static class RefreshingComponentModel extends AbstractReadOnlyModel<String> {
		
		public String[] values = {"Value one", "Value two", "Value three"};
		
		public int count = 0;
		
		public RefreshingComponentModel() {
			
		}

		@Override
		public String getObject() {
			count++;
			return new Date ().toString();
		}

	}
}


