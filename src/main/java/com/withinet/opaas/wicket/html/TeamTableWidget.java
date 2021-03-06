/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.WicketApplication;
import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.RoleController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.RoleControllerException;
import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.system.FileLocationGenerator;
import com.withinet.opaas.model.domain.Role;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin
 * 
 */
public class TeamTableWidget extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6426148890485840838L;
	private transient SortableDataProvider<User, String> provider = new TeamTableDataProvider(
			1L);
	private final List<IColumn<User, String>> columns = Collections
			.synchronizedList(new ArrayList<IColumn<User, String>>());
	private int resultSize = 20;

	@SpringBean
	UserController userController;
	
	@SpringBean
	Authorizer authorizer;
	
	UpdateForm updateForm = null;
	
	@SpringBean
	private RoleController roleController;
	
	@SpringBean
	private FileLocationGenerator fileGenerator;

	private Map<String, Role> rolesModel = new HashMap<String, Role> ();
	
	private User user;
	
	private Boolean authorized;

	/**
	 * @param id
	 */
	public TeamTableWidget(String id) {
		super(id);
	}

	public TeamTableWidget(String id, boolean b) {
		this (id);
		authorized = b;
	}
	
	@Override
	public void onInitialize () {
		super.onInitialize();
		setVisible (authorized);
		columns.add(new PropertyColumn<User, String>(new Model<String>("Name"),
				"fullName"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Email"), "email"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Approved By"), "administrator.email"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Status"), "status"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Role"), "role"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Created"), "created"));
		columns.add(new PropertyColumn<User, String>(
				new Model<String>("Last Seen"), "lastSeen"));
		columns.add(new AbstractColumn<User, String>(new Model<String>(
				"Space Used (Bytes)")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<User>> item,
					String componentId, final IModel<User> model) {
				long size = FileUtils.sizeOfDirectory(fileGenerator.getUserDrirectory(model.getObject().getID()));
				item.add (new Label (componentId, size)); 
			}
		});
		
		columns.add(new AbstractColumn<User, String>(new Model<String>(
				"Quick Action")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<User>> item,
					String componentId, final IModel<User> model) {
				IndicatingAjaxLink updateUser = new IndicatingAjaxLink("update-user") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Long uid = UserSession.get().getUser().getID();
						try {
							user = userController.readAccount(model.getObject().getID(), uid);
							updateForm.setDefaultModel(new CompoundPropertyModel (user));
							target.add(updateForm);
							target.appendJavaScript("showUpdateUserModal()");
						} catch (UserControllerException e) {
							error (e.getMessage());
							setResponsePage (getPage ());
						}
					}
				};
				
				ConfirmationLink<String> deleteUser = new ConfirmationLink<String>("delete-user", "Caution: All user artifacts will be removed including team members?") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Long id = model.getObject().getID();
						Long uid = UserSession.get().getUser().getID();
						try {
							userController.deleteAccount(id, uid);
							getPage().info("User deleted successfully");
							setResponsePage(getPage());
						} catch (UserControllerException e) {
							error(e.getMessage());
						}
					}
				};
				// BookmarkablePageLink<InstanceIndex> flushInstance = new
				// BookmarkablePageLink<InstanceIndex> ("flush-instance",
				// InstanceIndex.class, setFlushInstanceLinkParameters
				// (model.getObject()));
				ConfirmationLink<String> resetUser = new ConfirmationLink<String>("reset-user", "User artifacts will be cleared and new password sent?") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Long id = model.getObject().getID();
						Long uid = UserSession.get().getUser().getID();
						try {
							userController.resetAccount(id, uid);
							getPage().info("User area cleared successfully");
							setResponsePage(getPage());
						} catch (UserControllerException e) {
							error(e.getMessage());
						}
					}
				};
				
				ConfirmationLink<String> resetPassword = new ConfirmationLink<String>("reset-password", "User password will be reset and sent?") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Long id = model.getObject().getID();
						Long uid = UserSession.get().getUser().getID();
						try {
							userController.passwordReset(id, uid);
							getPage().info("User password reset successfully");
							setResponsePage(getPage());
						} catch (UserControllerException e) {
							error(e.getMessage());
						}
					}
				};

				TeamTableQuickAction button = new TeamTableQuickAction(
						componentId, updateUser, deleteUser, resetUser, resetPassword);
				item.add(button);
			}
		});

		DataTable<User, String> dataTable = new DefaultDataTable<User, String>(
				"team-table", columns, provider, resultSize);
		add(dataTable);
		
		add (updateForm = new UpdateForm("update-user-form"));
	}
	
	private class UpdateForm extends Form<User> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2755408263154758096L;

		public UpdateForm(String id) {
			super(id);
			final CSSFeedbackPanel feedback = new CSSFeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			this.add(feedback);
			
			final EmailTextField wicketEmail = new EmailTextField(
					"email");
			this.add(wicketEmail);
			
			final RequiredTextField<Integer> wicketQuota = new RequiredTextField<Integer>(
					"quota", Integer.class);
			this.add(wicketQuota);

			try {
				DropDownChoice<String> wicketRole = new DropDownChoice<String>("role", initRoles ());
				add (wicketRole);
			} catch (RoleControllerException e1) {
				throw new RuntimeException (e1.getMessage ());
			}

			List<String> statuses = Arrays.asList("Active", "Disabled");
			final DropDownChoice wicketStatus = new DropDownChoice<String>("status",
					statuses);
			this.add(wicketStatus);

			this.add(new IndicatingAjaxButton("submit", this) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					Long uid = UserSession.get().getUser().getID();
					try {
						Role role = rolesModel.get(user.getRole());
						if (role != null) {
		    				user.setAssignedRole(role);
						}
						user.setEmail(wicketEmail.getValue());
						user.setQuota(Integer.parseInt(wicketQuota.getValue()));
						userController.updateAccount(user, user.getID(), uid);
						info("User updated");
						UserSession u =((WicketApplication) Application.get()).getUserSession(user.getID());
						if (u != null) 
							u.setUser(user);
						
						setResponsePage(getPage());
					} catch (UserControllerException e) {
						error(e.getMessage());
						target.add(feedback);
					}
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedback);
				}

			});
		}
		
		public List<String> initRoles () throws RoleControllerException {
			Long uid = UserSession.get().getUser().getID();
			List<String> roles = new ArrayList<String> ();
			if (authorized) {
				for (Role role : roleController.readRolesByOwner(uid)) {
					roles.add(role.getName());
					rolesModel.put(role.getName(), role);
				}
			}
			return roles;
	    }
	}

	public class TeamTableDataProvider extends SortableDataProvider<User, String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 101461442145829090L;

		public TeamTableDataProvider(Long userId) {

		}

		private List<User> teamMembers = new ArrayList<User>();

		@Override
		public Iterator<? extends User> iterator(long arg0, long arg1) {
			return teamMembers.subList((int) arg0,
					Math.min((int) teamMembers.size(), (int) arg1)).iterator();
		}

		@Override
		public IModel<User> model(User arg0) {
			return Model.of(arg0);
		}

		@Override
		public long size() {
			Long uid = UserSession.get().getUser().getID();
			if (authorized) {
				try {
					authorizer.authorize(ServiceProperties.SUPER_ADMIN, uid);
					teamMembers = userController.listAllUsers(uid);
				} catch (ControllerSecurityException e) {
					try {
						teamMembers = userController.listTeamMembers(uid, uid);
					} catch (UserControllerException e1) {
						e1.printStackTrace();
					}
				}
			} else {
				teamMembers = Collections.emptyList();
			}
			return teamMembers.size();
		}
	
	}
}
