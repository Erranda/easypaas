package com.withinet.opaas.controller.system;

import java.io.File;

public interface FileLocationGenerator {
	public File getHomeDirectory ();
	
	public File getUserDrirectory (Long uuid);
	
	public File getInstanceDirectory (Long iiid);
	
	public File getTempDirectory ();
	
	public File getResourcesDirectory ();

	public String getTempDirectoryPath();

	public String getUserDrirectoryPath(Long uuid);

	public String getHomeDirectoryPath();

	public File getUserLibrary(Long uuid);
}
