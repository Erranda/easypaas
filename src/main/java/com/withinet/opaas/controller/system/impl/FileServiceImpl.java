/**
 * 
 */
package com.withinet.opaas.controller.system.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.system.FileService;
import com.withinet.opaas.controller.system.Validation;

/**
 * @author Folarin
 *
 */
@Service
public class FileServiceImpl implements FileService {

	/**
	 * 
	 */
	public FileServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.system.FileService#createFile(java.lang.String)
	 */
	@Override
	public File createFile(String fileLocation) {
		Validation.assertNotNull(fileLocation);
		File forCreate = new File (fileLocation);
		if (!forCreate.exists()) {
			try {
				forCreate.createNewFile();
				return forCreate;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return forCreate;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.system.FileService#deleteFile(java.lang.String)
	 */
	@Override
	public boolean deleteFile(String fileLocation) {
		Validation.assertNotNull(fileLocation);
		File forDelete = new File (fileLocation);
		if (forDelete.exists())
			return forDelete.delete();
		else 
			return true;
	}

	@Override
	public boolean updateFile(String fileLocation, File file) {
		if (deleteFile (fileLocation)) {
			file.renameTo(new File (fileLocation));
			return true;
		}
		return false;
	}
	
	

}
