/**
 * 
 */
package com.withinet.opaas.controller.system;

import java.io.File;
import java.io.IOException;

/**
 * @author Folarin
 *
 */
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
	public boolean createFile(String fileLocation) {
		Validation.assertNotNull(fileLocation);
		File forDelete = new File (fileLocation);
		if (!forDelete.exists()) {
			forDelete.mkdirs();
			try {
				return forDelete.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
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
		return false;
	}

}
