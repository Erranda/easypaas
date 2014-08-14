/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.util.EasyReader;
import com.withinet.opaas.util.Log;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin
 *
 */
public class InstanceTableWidget extends Panel {
	@SpringBean
	InstanceController instanceController;
	
	Long selected = null;
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
		
		final Label logLabel = new Label ("log");
		logLabel.setOutputMarkupId(true);
		logLabel.setEscapeModelStrings(false);
		add (logLabel);
		
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
					String componentId, final IModel<Instance> model) {
				//BookmarkablePageLink<InstanceIndex> stopInstance = new BookmarkablePageLink<InstanceIndex> ("stop-instance", InstanceIndex.class, setStopInstanceLinkParameters (model.getObject()));
				AjaxLink stopInstance = new AjaxLink("stop-instance"){                                                                                                                                                                                                                                                                                                  
					private static final long serialVersionUID = 1L;                                                                                                       

					@Override                                                                                                                                              
					public void onClick(AjaxRequestTarget target) {                                                                                                        
						 Long id = model.getObject().getId();
						 Long uid = UserSession.get().getUser ().getID();
						 try {
							instanceController.stopInstance(id, uid);
							info ("Success, instance stopped");
							setResponsePage (getPage());
						} catch (InstanceControllerException e) {
							error (e.getMessage());
						}
					}                                                                                                                                                      
				};
				//BookmarkablePageLink<InstanceIndex> startInstance = new BookmarkablePageLink<InstanceIndex> ("start-instance", InstanceIndex.class, setStartInstanceLinkParameters (model.getObject()));
				AjaxLink startInstance = new AjaxLink("start-instance"){                                                                                                                                                                                                                                                                                                  
					private static final long serialVersionUID = 1L;                                                                                                       

					@Override                                                                                                                                              
					public void onClick(AjaxRequestTarget target) {                                                                                                        
						 Long id = model.getObject().getId();
						 Long uid = UserSession.get().getUser ().getID();
						 try {
							instanceController.startInstance(id, uid);
							info ("Success, instance live");
							setResponsePage (getPage());
						} catch (InstanceControllerException e) {
							error (e.getMessage());
						}
					}                                                                                                                                                      
				};
				//BookmarkablePageLink<InstanceIndex> flushInstance = new BookmarkablePageLink<InstanceIndex> ("flush-instance", InstanceIndex.class, setFlushInstanceLinkParameters (model.getObject()));
				AjaxLink flushInstance = new AjaxLink("flush-instance"){                                                                                                                                                                                                                                                                                                  
					private static final long serialVersionUID = 1L;                                                                                                       

					@Override                                                                                                                                              
					public void onClick(AjaxRequestTarget target) {                                                                                                        
						 Long id = model.getObject().getId();
						 Long uid = UserSession.get().getUser ().getID();
						 try {
							instanceController.deleteInstance(id, uid);
							info ("Success, instance deleted");
							setResponsePage (getPage());
						} catch (InstanceControllerException e) {
							error (e.getMessage());
						}
					}                                                                                                                                                      
				};
				
				AjaxLink log = new AjaxLink("view-log"){                                                                                                                                                                                                                                                                                                  
					private static final long serialVersionUID = 1L;                                                                                                       

					@Override                                                                                                                                              
					public void onClick(AjaxRequestTarget target) {                                                                                                        
						 Long id = model.getObject().getId();
						 Long uid = UserSession.get().getUser ().getID();
						 Model labelModel = new Model (){
								@Override
							    public Serializable getObject() {
							        try {
										return Log.getLog(model.getObject().getLogFile());
									} catch (IOException e) {
										error (e.getMessage());
										return "";
									}
							    }
						 };
						 logLabel.setDefaultModel(labelModel);
						 target.add(logLabel, logLabel.getMarkupId());
						 target.appendJavaScript("showModal()");
						 target.appendJavaScript("removeLoader()");
					}                                                                                                                                                      
				};
				ExternalLink cpanelLink = new ExternalLink ("cpanel-link", model.getObject().getCpanelUrl());
				flushInstance.setVisible(false);
				startInstance.setVisible(false);
				stopInstance.setVisible(false);
				cpanelLink.setVisible(false);
				log.setVisible(true);
				
				if (model.getObject().getStatus().equals("Live")) {
					stopInstance.setVisible(true);
					cpanelLink.setVisible(true);
				}		
				if (model.getObject().getStatus().equals("Dead")) {
					flushInstance.setVisible(true);
					startInstance.setVisible(true);
				}
				
				InstanceTableQuickAction button = new InstanceTableQuickAction (componentId, stopInstance, flushInstance, startInstance, cpanelLink, log);
				item.add(button);
			}
		});
		DataTable<Instance, String> dataTable = new DefaultDataTable <Instance, String> ("instance-table", columns, provider, resultSize);
		add (dataTable);
		
	}
	
	private class InstanceTableDataProvider extends SortableDataProvider<Instance, String> {
		
		public InstanceTableDataProvider (Long userId) {
			
		}
		
		private List<Instance> userInstances = new ArrayList<Instance> ();
		
		@Override
		public Iterator<? extends Instance> iterator(long arg0, long arg1) {
			return userInstances.subList((int) arg0, Math.min((int) userInstances.size(), (int) arg1)).iterator();
		}

		@Override
		public IModel<Instance> model(Instance arg0) {
			return Model.of(arg0);
		}

		@Override
		public long size() {
			Long userId = UserSession.get().getUser ().getID();
			try {
				userInstances = instanceController.listInstancesByUser(userId, userId);
			} catch (InstanceControllerException e) {
				error (e.getMessage());
			}
			return userInstances.size();
		}

	}

}
