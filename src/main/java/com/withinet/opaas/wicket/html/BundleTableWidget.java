/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.system.FileService;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
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
	private transient SortableDataProvider<Bundle, String> provider = new BundleTableDataProvider();
	private final List<IColumn<Bundle, String>> columns = Collections
			.synchronizedList(new ArrayList<IColumn<Bundle, String>>());
	private int resultSize = 5;
	private Long selected = null;

	@SpringBean
	private BundleController bundleController;

	@SpringBean
	private FileService fileService;

	@SpringBean
	private ProjectController projectController;


	private Boolean authorized;

	private Long pid = null;

	/**
	 * @param id
	 */
	public BundleTableWidget(String id) {
		super(id);
		super.onInitialize();
	}

	public BundleTableWidget(String id, Long pid) {
		super(id);
		this.pid = pid;
	}

	public BundleTableWidget(String id, boolean b) {
		this (id);
		this.authorized = b;
	}
	
	public BundleTableWidget(String id, Long pid, boolean b) {
		this (id, pid);
		authorized = b;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		setVisible (authorized);
		columns.add(new PropertyColumn<Bundle, String>(
				new Model<String>("Name"), "symbolicName"));
		columns.add(new PropertyColumn<Bundle, String>(
				new Model<String>("Created By"), "owner.fullName"));
		columns.add(new PropertyColumn<Bundle, String>(new Model<String>(
				"Last Updated"), "updated"));

		final DataTable<Bundle, String> dataTable = new DefaultDataTable<Bundle, String>(
				"bundle-table", columns, provider, resultSize);
		dataTable.setOutputMarkupId(true);
		add(dataTable);
		columns.add(new AbstractColumn<Bundle, String>(new Model<String>(
				"Quick Action")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Bundle>> item,
					String componentId, final IModel<Bundle> model) {
				// BookmarkablePageLink<BundleIndex> deleteBundle = new
				// BookmarkablePageLink<BundleIndex> ("delete-link",
				// BundleIndex.class, setDeleteBundleLinkParameters
				// (model.getObject()));
				final ConfirmationLink<String> deleteBundle = new ConfirmationLink<String>(
						"update-instances-delete",
						"Projects and instances with bundle will be updated as well. Continue?") {

					/**
							 * 
							 */
							private static final long serialVersionUID = -6638248099291384547L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							Long uid = UserSession.get().getUser().getID();
							List<Project> projects = projectController
									.listProjectsByBundle(model.getObject()
											.getID(), uid);

							Long bid = model.getObject().getID();
							try {
								Long userId = UserSession.get().getUser().getID();
								bundleController.deleteBundle(bid, userId);
							} catch (BundleControllerException e) {
								getPage().error(e.getMessage());
								setResponsePage(getPage());
							}
							for (Project project : projects)
								projectController.refreshProjectInstancesDirty(
										project.getID(), uid);
							getPage()
									.info("Bundle deleted from library. All targets with bundle updated");
							target.add(((Authenticated) getPage())
									.getFeedbackPanel());
							setResponsePage(getPage());

						} catch (ProjectControllerException e) {
							e.printStackTrace();
							getPage().error(e.getMessage());
							target.add(((Authenticated) getPage())
									.getFeedbackPanel());
						}
					}
				};

				// BookmarkablePageLink<BundleIndex> updateBundle = new
				// BookmarkablePageLink<BundleIndex> ("update-link",
				// BundleIndex.class, setUpdateBundleLinkParameters
				// (model.getObject()));
				IndicatingAjaxLink<String> updateBundle = new IndicatingAjaxLink<String>("update-link") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						selected = model.getObject().getID();
						target.appendJavaScript("showModal ()");
					}
				};
				add(dataTable);

				updateBundle.add(AttributeModifier.replace("data-toggle",
						"modal"));
				updateBundle.add(AttributeModifier.replace("href",
						"#bundleUpdate"));
				BundleTableQuickAction button = new BundleTableQuickAction(
						componentId, updateBundle, deleteBundle);
				item.add(button);
			}
		});

		Form<Void> setupForm = new Form<Void>("update");
		add(setupForm);

		final CSSFeedbackPanel feedback = new CSSFeedbackPanel("feedback");
		feedback.setOutputMarkupPlaceholderTag(true);
		setupForm.add(feedback);

		final FileUploadField wicketFileUploadField = new FileUploadField(
				"file");
		wicketFileUploadField.setRequired(true);
		setupForm.add(wicketFileUploadField);

		setupForm.setMaxSize(Bytes.megabytes(10000));
		UploadProgressBar progress = new UploadProgressBar("progress",
				setupForm, wicketFileUploadField);
		setupForm.add(progress);

		setupForm.add(new ConfirmationButton("submit", "All instances will be updated?" , setupForm) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7432809572142555276L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (selected == null)
					throw new RuntimeException();
				FileUpload upload = wicketFileUploadField.getFileUpload();
				if (upload != null) {
					String fileName = upload.getClientFileName().toLowerCase()
							.trim();
					String extension = FilenameUtils.getExtension(fileName);
					extension = extension.toLowerCase().trim();

					if (extension.equals("jar")) {
						try {
							Long userId = UserSession.get().getUser().getID();
							Bundle bundle = bundleController.readBundle(selected, userId);
							String location = bundle.getLocation();
							fileService.deleteFile(location);
							location = FilenameUtils.getFullPathNoEndSeparator(location) + "/" + upload.getClientFileName();
							upload.writeTo(fileService.createFile(location));
							bundle.setSymbolicName(upload.getClientFileName());
							bundle.setLocation(location);
							bundleController.updateBundle(bundle, userId);
							info(bundle.getSymbolicName() + " updated.");
							target.add(feedback);
							bundleController.refreshBundleInstances(bundle.getID(), userId);
							info("Targets updated");
							target.add(feedback);
						} catch (BundleControllerException e) {
							e.printStackTrace();
							error(e.getMessage());
							target.add(feedback);
						} catch (IOException e) {
							e.printStackTrace();
							error(e.getMessage());
							target.add(feedback);
						}
					} else {
						error(extension + " not jar");
						target.add(feedback);
					}
				}
			}
		});
	}

	protected void processUpload(FileUpload upload, AjaxRequestTarget target)
			throws BundleControllerException, IOException {
		
	}

	private class BundleTableDataProvider extends
			SortableDataProvider<Bundle, String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6823150414258297627L;

		private final long USER_ID = UserSession.get().getUser().getID();

		private List<Bundle> userBundles = null;

		@Override
		public Iterator<? extends Bundle> iterator(long arg0, long arg1) {
			return userBundles.subList((int) arg0,
					Math.min((int) userBundles.size(), (int) (arg0 + arg1)))
					.iterator();
		}

		@Override
		public IModel<Bundle> model(Bundle arg0) {
			return Model.of(arg0);
		}

		@Override
		public long size() {
			if (authorized) {
				if (pid != null) {
					try {
						userBundles = bundleController.listBundlesByProject(pid,
								USER_ID);
						// pid = null;
					} catch (BundleControllerException e1) {
						// error (e1.getMessage ());
						try {
							userBundles = bundleController.listBundlesByOwner(
									USER_ID, USER_ID);
						} catch (BundleControllerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						e1.printStackTrace();
					}
				} else {
					try {
						userBundles = bundleController.listBundlesByOwner(USER_ID,
								USER_ID);
					} catch (BundleControllerException e) {
						error(e.getMessage());
					}
				}
			} else {
				userBundles = Collections.emptyList();
			}
			
			return userBundles.size();
		}
	}
}
