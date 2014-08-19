/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.io.File;
import java.io.IOException;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.withinet.opaas.controller.FileController;
import com.withinet.opaas.controller.common.FileControllerException;
import com.withinet.opaas.controller.system.FileLocationGenerator;
/**
 * @author Folarin
 *
 */
@Controller
public class FileControllerImpl implements FileController {
	
	@Autowired
	private FileLocationGenerator fileMan;
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.FileController#getUsedSpace(java.lang.Long)
	 */
	@Override
	public String getUsedSpace(Long uid) throws FileControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.FileController#flushUserDirectory(java.lang.Long)
	 */
	@Override
	public boolean flushUserDirectory(Long uid) throws FileControllerException{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.FileController#flushProjectDirectory(java.lang.Long)
	 */
	@Override
	public boolean flushProjectDirectory(Long pid) throws FileControllerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.FileController#uploadFile(java.lang.Long, org.apache.wicket.markup.html.form.upload.FileUpload, java.lang.String)
	 */
	@Override
	public File uploadTempFile(Long uid, FileUpload upload) throws FileControllerException{
		// If project creation succeeds, check for file upload
		File thisFile = new File((fileMan.getConcurrentTempDirectory (uid)).getAbsolutePath() + "/"
				+ upload.getClientFileName());
		try {
			if (!thisFile.createNewFile())
				throw new FileControllerException ("File upload failed");
			else {
				upload.writeTo(thisFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileControllerException ("File upload failed");
		}
		return thisFile;
	}
	
	@Override
	public File uploadToDestFile(Long uid, FileUpload upload, String dest) throws FileControllerException {
		// If project creation succeeds, check for file upload
		File thisFile = new File(dest + "/"
				+ upload.getClientFileName());
		try {
			if (!thisFile.createNewFile())
				throw new FileControllerException ("File upload failed");
			else {
				upload.writeTo(thisFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileControllerException ("File upload failed");
		}
		return thisFile;
	}

}
