/**
 * 
 */
package com.withinet.opaas.controller.system;

/**
 * Object offering crud type interface for file system functions
 * @author Folarin
 *
 */
public interface FileService {
	public boolean createFile (String fileLocation);
	
	public boolean deleteFile (String fileLocation);
}
