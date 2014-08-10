/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.withinet.opaas.model.domain.Instance;

/**
 * @author Folarin
 *
 */
public class InstanceTableWidget extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6426148890485840838L;
	private transient SortableDataProvider<Instance, String> provider = new InstanceTableDataProvider (1L);
	private final List<IColumn<Instance, String>> columns = Collections.synchronizedList(new ArrayList<IColumn<Instance, String>>());
	private int resultSize = 20;
	/**
	 * @param id
	 */
	public InstanceTableWidget(String id) {
		super(id);
		
		columns.add(new PropertyColumn<Instance, String>(
				new Model<String>("Project Name"), "projectName"));
		columns.add(new PropertyColumn<Instance, String>(
				new Model<String>("Type"), "containerType"));
		columns.add(new PropertyColumn<Instance, String>(
				new Model<String>("Status"), "status"));
		columns.add(new PropertyColumn<Instance, String>(
				new Model<String>("Created by"), "ownerName"));
		columns.add(new PropertyColumn<Instance, String>(
				new Model<String>("Since"), "created"));
		columns.add(new AbstractColumn<Instance, String>(
				new Model<String>("Quick Action")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Instance>> item,
					String componentId, IModel<Instance> model) {
				BookmarkablePageLink<InstanceIndex> stopInstance = new BookmarkablePageLink<InstanceIndex> ("stop-instance", InstanceIndex.class, setStopInstanceLinkParameters (model.getObject()));
				BookmarkablePageLink<InstanceIndex> startInstance = new BookmarkablePageLink<InstanceIndex> ("start-instance", InstanceIndex.class, setStartInstanceLinkParameters (model.getObject()));
				BookmarkablePageLink<InstanceIndex> flushInstance = new BookmarkablePageLink<InstanceIndex> ("flush-instance", InstanceIndex.class, setFlushInstanceLinkParameters (model.getObject()));
				ExternalLink cpanelLink = new ExternalLink ("cpanel-link", model.getObject().getCpanelUrl());
				if (model.getObject().getStatus().equals("Live")) {
					flushInstance.setVisible(false);
					startInstance.setVisible(false);
					stopInstance.setVisible(true);
					cpanelLink.setVisible(true);
				}		
				if (model.getObject().getStatus().equals("Dead")) {
					flushInstance.setVisible(true);
					startInstance.setVisible(true);
					stopInstance.setVisible(false);
					cpanelLink.setVisible(false);
				}
				
				InstanceTableQuickAction button = new InstanceTableQuickAction (componentId, stopInstance, flushInstance, startInstance, cpanelLink);
				item.add(button);
			}
			private PageParameters setStopInstanceLinkParameters (Instance instance) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", instance.getId());
				linkParameters.add("action", "stop");
				return linkParameters;
			}
			
			private PageParameters setFlushInstanceLinkParameters (Instance instance) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", instance.getId());
				linkParameters.add("action", "flush");
				return linkParameters;
			}
			
			private PageParameters setStartInstanceLinkParameters (Instance instance) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", instance.getId());
				linkParameters.add("action", "start");
				return linkParameters;
			}
		});
		DataTable<Instance, String> dataTable = new DefaultDataTable <Instance, String> ("instance-table", columns, provider, resultSize);
		add (dataTable);
		
	}
	
}
