/**
 * 
 */
package com.withinet.opaas.controller.system;

import java.io.File;
import java.io.IOException;

/**
 * Object offering crud type interface for file system functions
 * @author Folarin
 *
 */
public interface FileService {
	public File createFile (String fileLocation);
	
	public boolean deleteFile (String fileLocation) throws IOException;
	
	public boolean updateFile (String fileLocation, File file) throws IOException; 
}
