package com.withinet.opaas.controller.system;

import java.io.File;

public interface FileLocationGenerator {
	public File getHomeDirectory ();
	
	public File getUserDrirectory (String uuid);
	
	public File getInstanceDirectory (String iiid);
	
	public File getTempDirectory ();
	
	public File getResourcesDirectory ();

	public String getTempDirectoryPath();

	public String getUserDrirectoryPath(String uuid);

	public String getHomeDirectoryPath();
}
