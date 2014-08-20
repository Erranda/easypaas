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


/**
 * @author Martijn Dashorst
 */
public class TeamIndex extends Authenticated
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3967639349012716065L;

	/**
     * Constructor.
     */
    public TeamIndex ()
    {
    	TeamTableWidget table = new TeamTableWidget ("team-table-widget");
    	table.setOutputMarkupId(true);
        add (table);
        add (new TeamAddMemberSectionWidget ("team-add-member-section", table));
    }
}