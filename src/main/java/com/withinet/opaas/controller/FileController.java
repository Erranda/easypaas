package com.withinet.opaas.controller;

import java.io.File;

import org.apache.wicket.markup.html.form.upload.FileUpload;

import com.withinet.opaas.controller.common.FileControllerException;

public interface FileController {
	
	public String getUsedSpace (Long uid)throws FileControllerException;;
	
	public boolean flushUserDirectory (Long uid)throws FileControllerException;;
	
	public boolean flushProjectDirectory (Long pid)throws FileControllerException;;
	
	public File uploadTempFile (Long uid, FileUpload upload)throws FileControllerException;

	public File uploadToDestFile(Long uid, FileUpload upload, String dest)
			throws FileControllerException;
	
}
