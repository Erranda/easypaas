/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin
 *
 */
public class BundleTableWidget extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6426148890485840838L;
	private transient SortableDataProvider<Bundle, String> provider = new BundleTableDataProvider ();
	private final List<IColumn<Bundle, String>> columns = Collections.synchronizedList(new ArrayList<IColumn<Bundle, String>>());
	private int resultSize = 20;
	
	@SpringBean
	private BundleController bundleController;
	/**
	 * @param id
	 */
	public BundleTableWidget(String id) {
		super(id);
		
		columns.add(new PropertyColumn<Bundle, String>(
				new Model<String>("Name"), "symbolicName"));
		columns.add(new PropertyColumn<Bundle, String>(
				new Model<String>("Updated"), "updated"));
		columns.add(new AbstractColumn<Bundle, String>(
				new Model<String>("Quick Action")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Bundle>> item,
					String componentId, IModel<Bundle> model) {
				BookmarkablePageLink<BundleIndex> deleteBundle = new BookmarkablePageLink<BundleIndex> ("delete-link", BundleIndex.class, setDeleteBundleLinkParameters (model.getObject()));
				BookmarkablePageLink<BundleIndex> updateBundle = new BookmarkablePageLink<BundleIndex> ("update-link", BundleIndex.class, setUpdateBundleLinkParameters (model.getObject()));
							
				BundleTableQuickAction button = new BundleTableQuickAction (componentId, updateBundle, deleteBundle);
				item.add(button);
			}
			private PageParameters setDeleteBundleLinkParameters (Bundle bundle) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", bundle.getID());
				linkParameters.add("action", "delete");
				return linkParameters;
			}
			
			private PageParameters setUpdateBundleLinkParameters (Bundle bundle) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", bundle.getID());
				linkParameters.add("action", "update");
				return linkParameters;
			}
		});
		DataTable<Bundle, String> dataTable = new DefaultDataTable <Bundle, String> ("bundle-table", columns, provider, resultSize);
		add (dataTable);
		
	}
	
	private class BundleTableDataProvider extends SortableDataProvider<Bundle, String> {
		
		private final long USER_ID = UserSession.get().getUser().getID();
		
		private List<Bundle> userBundles = null;
		
		@Override
		public Iterator<? extends Bundle> iterator(long arg0, long arg1) {
			return userBundles.subList((int) arg0, Math.min((int) userBundles.size(), (int) arg1)).iterator();
		}

		@Override
		public IModel<Bundle> model(Bundle arg0) {
			return Model.of(arg0);
		}

		@Override
		public long size() {
			try {
				userBundles = bundleController.listBundlesByOwner(USER_ID, USER_ID);
			} catch (BundleControllerException e) {
				error (e.getMessage ());
			}
			return userBundles.size();
		}

	}
}
