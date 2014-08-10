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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 *
 */
public class TeamTableWidget extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6426148890485840838L;
	private transient SortableDataProvider<User, String> provider = new TeamTableDataProvider (1L);
	private final List<IColumn<User, String>> columns = Collections.synchronizedList(new ArrayList<IColumn<User, String>>());
	private int resultSize = 20;
	/**
	 * @param id
	 */
	public TeamTableWidget(String id) {
		super(id);
		
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Name"), "fullName"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Email"), "email"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Status"), "status"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Since"), "created"));
		columns.add(new AbstractColumn<User, String>(
				new Model<String>("Quick Action")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<User>> item,
					String componentId, IModel<User> model) {
				BookmarkablePageLink<TeamIndex> updateUser = new BookmarkablePageLink<TeamIndex> ("update-user", TeamIndex.class, setUpdateUserLinkParameters (model.getObject()));
				BookmarkablePageLink<TeamIndex> deleteUser = new BookmarkablePageLink<TeamIndex> ("delete-user", TeamIndex.class, setDeleteUserLinkParameters (model.getObject()));
				BookmarkablePageLink<TeamIndex> cleanUser = new BookmarkablePageLink<TeamIndex> ("clean-user", TeamIndex.class, setCleanUserLinkParameters (model.getObject()));
			
				TeamTableQuickAction button = new TeamTableQuickAction (componentId, updateUser, deleteUser, cleanUser);
				item.add(button);
			}
			private PageParameters setUpdateUserLinkParameters (User user) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", user.getID());
				linkParameters.add("action", "update");
				return linkParameters;
			}
			
			private PageParameters setCleanUserLinkParameters (User user) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", user.getID());
				linkParameters.add("action", "clean");
				return linkParameters;
			}
			
			private PageParameters setDeleteUserLinkParameters (User user) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", user.getID());
				linkParameters.add("action", "delete");
				return linkParameters;
			}
		});
		
		DataTable<User, String> dataTable = new DefaultDataTable <User, String> ("team-table", columns, provider, resultSize);
		add (dataTable);
		
	}
	
}
