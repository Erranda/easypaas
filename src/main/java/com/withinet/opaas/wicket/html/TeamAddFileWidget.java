package com.withinet.opaas.wicket.html;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.controller.FileController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.FileControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.common.UserParserException;
import com.withinet.opaas.controller.system.FileLocationGenerator;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.util.ExcelUserParser;
import com.withinet.opaas.wicket.services.UserSession;


/**
 * Login page
 * 
 * @author kloe and Folarin
 *
 */
public class TeamAddFileWidget extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6370041751528976156L;
	private FileUpload upload;
	
	@SpringBean
	private FileController fileController;
	
	@SpringBean
	private UserController userController;
	
	private Panel target;

	public TeamAddFileWidget (String id, Panel target) {
		super (id);
		this.setTarget(target);
	    Form<Void> uploadForm = new Form<Void>("addMemberFileForm");
	    add(uploadForm);
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    uploadForm.add(feedback);
	    final FileUploadField file = new FileUploadField ("file");
	    file.setRequired(true);
	    uploadForm.add(file);
	    UploadProgressBar  progress = new UploadProgressBar("progress", uploadForm, file);
	    uploadForm.add(progress);
	    uploadForm.add(new IndicatingAjaxButton ("upload", uploadForm){
	    	@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
	    		FileUpload upload = file.getFileUpload();
	    		if (upload == null) {
	    			info ("Nothing uploaded");
	    			target.add(feedback);
	    		} else {
	    			String fileName = upload.getClientFileName().toLowerCase()
							.trim();
					String extension = FilenameUtils.getExtension(fileName);
					extension = extension.toLowerCase().trim();
					if (!extension.toLowerCase().trim().equals("xls")) {
						error ("Only XLS format supported");
						target.add(feedback);
					}
					else {
						Long uid = UserSession.get().getUser().getID();
						try {
							String uploaded = fileController.uploadTempFile(uid, upload).getAbsolutePath();
							List<User> users = new ArrayList<User>();
							
								users = ExcelUserParser.parse(uploaded);
								int count = 0;
								for (User user : users) {
									try {
										userController.addTeamMember (user, uid, uid);
										count++;
									} catch (UserControllerException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										error (e.getMessage());
										target.add(feedback);
									}
								}
								info (count + " members added");
								target.add(feedback);
								target.add(getTarget());
						} catch (FileControllerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							error (e.getMessage());
							target.add(feedback);
						} catch (UserParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							error (e.getMessage());
							target.add(feedback);
						}
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

	public Panel getTarget() {
		return target;
	}

	public void setTarget(Panel target) {
		this.target = target;
	}
}
