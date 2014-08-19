package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.UserControllerException;


/**
 * @author Martijn Dashorst
 */
public class InstanceIndex extends Authenticated
{
	@SpringBean
	InstanceController instanceController;
    /**
     * Constructor.
     */
    public InstanceIndex ()
    {
    	try {
			add (new InstanceSetupWidget ("instance-setup-widget"));
		} catch (ProjectControllerException e) {
			error (e.getMessage());
			setResponsePage (getPage());
		} catch (UserControllerException e) {
			error (e.getMessage());
			setResponsePage (getPage());
		}
    	add (new InstanceTableWidget ("instance-table-widget"));
    }
    
    public InstanceIndex (PageParameters pageParameters) {
    	if (!pageParameters.get("pid").isNull()) {
    		Long pid = pageParameters.get("pid").toLong();
    		add (new InstanceTableWidget ("instance-table-widget", pid));
    	}
    	try {
			add (new InstanceSetupWidget ("instance-setup-widget"));
		} catch (ProjectControllerException e) {
			error (e.getMessage());
			setResponsePage (getPage());
		} catch (UserControllerException e) {
			error (e.getMessage());
			setResponsePage (getPage());
		}
    }
}