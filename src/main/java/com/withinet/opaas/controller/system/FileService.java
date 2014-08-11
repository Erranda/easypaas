/**
 * 
 */
package com.withinet.opaas.controller.system;

import java.io.File;

/**
 * Object offering crud type interface for file system functions
 * @author Folarin
 *
 */
public interface FileService {
	public File createFile (String fileLocation);
	
	public boolean deleteFile (String fileLocation);
	
	public boolean updateFile (String fileLocation, File file); 
}
