/**
 * 
 */
package com.withinet.opaas.controller.system.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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
	public boolean deleteFile(String fileLocation) throws IOException {
		Validation.assertNotNull(fileLocation);
		File forDelete = new File (fileLocation);
		if (forDelete.exists()) {
			if (forDelete.isDirectory())
				FileUtils.deleteDirectory(forDelete);
			else
				return forDelete.delete();
		}
		return true;
	}

	@Override
	public boolean updateFile(String fileLocation, File file) throws IOException {
		return file.renameTo(new File (fileLocation));
	}
	
	@Override
	public void copyFile(String dest, String src) throws IOException {
		FileUtils.copyFile(new File (src), new File (dest));
	}
}
