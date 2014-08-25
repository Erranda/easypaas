/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IClusterable;

import com.withinet.opaas.controller.RoleController;
import com.withinet.opaas.controller.common.RoleControllerException;
import com.withinet.opaas.model.PermissionRepository;
import com.withinet.opaas.model.domain.Permission;
import com.withinet.opaas.model.domain.Role;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin
 *
 */
public class RoleTableWidget extends Panel {
	
	private static final long serialVersionUID = -8043571455291012150L;
	
	@SpringBean
	private RoleController roleController;
	
	@SpringBean
	private PermissionRepository permRepo;
	
	private Map<String, Permission> permissionModel = new HashMap<String, Permission> ();
	
	private List<String> allPermissions = new ArrayList<String> ();
	
	private final List<IColumn<Role, String>> columns = Collections.synchronizedList(new ArrayList<IColumn<Role, String>>());
	
	private int resultSize = 20;
	
	private RoleModel roleModel = null;
	
	private RoleForm form = null;
	
	private RoleTableDataProvider provider = new RoleTableDataProvider ();
	
	private DefaultDataTable<Role, String> table = null;
	
	/**
	 * @param id
	 */
	public RoleTableWidget(String id) {
		super(id);
		for (Permission permission : permRepo.findAll()) {
			String key = permission.getDescription();
			allPermissions.add(key);
			permissionModel.put(key, permission);
		}
		roleModel = new RoleModel ();
		roleModel.user = UserSession.get().getUser();
		form = new RoleForm ("role-form");
		form.setDefaultModel(new CompoundPropertyModel<RoleModel> (roleModel));
		form.setOutputMarkupId(true);
		add (form);
		
		columns.add(new PropertyColumn<Role, String>(new Model<String>("Name"),
				"name"));
		
		columns.add(new AbstractColumn<Role, String>(new Model<String>(
				"Quick Action")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Role>> item,
					String componentId, final IModel<Role> model) {
				
				IndicatingAjaxLink<String> updateRole = new IndicatingAjaxLink <String>("update-role") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Long uid = UserSession.get().getUser().getID();
						try {
							Role role = roleController.readRole(model.getObject().getId(), uid);
							roleModel = new RoleModel ();
							roleModel.setId(role.getId());
							roleModel.setName(role.getName());
							List<Permission> permissions = roleController.readRolePermissions(role.getId(), uid);
							ArrayList<String> selected = new ArrayList<String> ();
							for (Permission  perm : permissions) {
								selected.add(perm.getDescription());
							}
							roleModel.setPermissions(selected);
							roleModel.setUId(uid);
							form.setDefaultModel(new CompoundPropertyModel<RoleModel> (roleModel));
							target.add(form);
							target.appendJavaScript("document.getElementById(\"showRoleForm\").click()");
						} catch (RoleControllerException e) {
							error (e.getMessage());
							target.add(((Authenticated) getPage ()).getFeedbackPanel());
							e.printStackTrace();
						}
					}
				};
				
				ConfirmationLink<String> deleteRole = new ConfirmationLink <String>("delete-role", "All members with this role will be disabled?") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Long uid = UserSession.get().getUser().getID();
						try {
							roleController.deleteRole(model.getObject().getId(), uid);
							info ("Role Deleted");
							target.add(((Authenticated) getPage ()).getFeedbackPanel());
							target.add(table);
						} catch (RoleControllerException e) {
							error (e.getMessage());
							target.add(((Authenticated) getPage ()).getFeedbackPanel());
							e.printStackTrace();
						}
					}
				};
				
				item.add (new RoleTableQuickAction(componentId, updateRole, deleteRole));
			
			}
		});
		add (table = new DefaultDataTable<Role, String>(
				"role-table", columns, provider, resultSize));
		table.setOutputMarkupId(true);
		
		add (new IndicatingAjaxLink ("add-role") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				roleModel.setName(null);
				roleModel.setId(null);
				roleModel.setUId(UserSession.get().getUser().getID());
				roleModel.setPermissions(Collections.<String> emptyList());
				form.setDefaultModel(new CompoundPropertyModel<RoleModel> (roleModel));
				target.add(form);
				target.appendJavaScript("document.getElementById(\"showRoleForm\").click()");
			}
		});
	}

	/**
	 * @param id
	 * @param model
	 */
	public RoleTableWidget(String id, IModel<?> model) {
		super(id, model);
	}
	
	@SuppressWarnings("unused")
	private class RoleModel implements IClusterable {
		
		private Long id;
		
		private User user;
		
		private Long uid;
		
		private String name;
	
		private List<String> permissions;
		
		public void setName (String name) {
			this.name = name;
		}
		
		public void setPermissions (List<String> permissions) {
			this.permissions = permissions;
		}
		
		public void setId (Long id) {
			this.id = id;
		}
		
		public void setUId (Long id) {
			this.uid = id;
		}
	}
	
	private class RoleForm extends Form<Void> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -6760682266838249612L;

		public RoleForm(String id) {
			super(id);
		}	
		
		public void onInitialize () {
			super.onInitialize();
			final CSSFeedbackPanel feedback = new CSSFeedbackPanel("feedback");
			feedback.setOutputMarkupPlaceholderTag(true);
			add(feedback);
			
			HiddenField<Long> wicketId = new HiddenField<Long>("id", Long.class);
		    add(wicketId);
		    
		    HiddenField<Long> wicketUId = new HiddenField<Long>("uid", Long.class);
		    add(wicketUId);
		    
		    TextField<String> wicketName = new TextField<String>("name");
		    wicketName.setRequired(true);
		    if (roleModel.name != null && roleModel.name.equals(""))
		    	wicketName.add(AttributeModifier.append("readonly", ""));
		    add(wicketName);
		    
		    CheckBoxMultipleChoice<String> permissions = new CheckBoxMultipleChoice<String>("permissions", allPermissions);
		    add (permissions);
		    
		    add (new IndicatingAjaxButton ("submit", this){

		    	private static final long serialVersionUID = -6415555183396288060L;

				@Override
	            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					Long uid = roleModel.uid;
					Long id = roleModel.id;
					
					if (id != null) {
						try {
							List<Permission> permissions = new ArrayList<Permission> ();
							for (String permission : roleModel.permissions) {
								if (permissionModel.get(permission) != null)
									permissions.add(permissionModel.get(permission));
							}
							roleController.addPermission(id, permissions, uid);
							TeamIndex teamIndex = new TeamIndex();
							teamIndex.info ("Role updated");
							setResponsePage (teamIndex);
						} catch (RoleControllerException e) {
							error (e.getMessage());
							target.add(feedback);
							e.printStackTrace();
						}
					} else {
						try {
							Role role = new Role ();
							role.setName(roleModel.name);
							role.setOwner(roleModel.user);
							role = roleController.createRole(role, roleModel.uid);
							List<Permission> permissions = new ArrayList<Permission> ();
							for (String permission : roleModel.permissions) {
								if (permissionModel.get(permission) != null)
									permissions.add(permissionModel.get(permission));
							}
							roleController.addPermission(role.getId(), permissions, uid);
							TeamIndex teamIndex = new TeamIndex();
							teamIndex.info ("Role updated");
							setResponsePage (teamIndex);
						} catch (RoleControllerException e) {
							error (e.getMessage());
							target.add(feedback);
							e.printStackTrace();
						}
					}
				}
				
				@Override
	            protected void onError(AjaxRequestTarget target, Form<?> form)
	            {
	            	target.add(feedback);
	            }
		    });
		}
		
	}
	
	private class RoleTableDataProvider extends SortableDataProvider <Role, String> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4921548419945198045L;
		List<Role> roles = new ArrayList<Role> ();
		
		@Override 
		public Iterator<? extends Role> iterator(long first, long count) {
			return roles.subList((int) first, Math.min((int) count, (int) size())).iterator(); 
		}

		@Override
		public long size() {
			Long uid = UserSession.get().getUser().getID();
			try {
				roles = roleController.readRolesByOwner(uid);
			} catch (RoleControllerException e) {
				error (e.getMessage());
				e.printStackTrace();
			}
			return roles.size();
		}

		@Override
		public IModel<Role> model(Role object) {
			return Model.of(object);
		}
		
	}
	
}
