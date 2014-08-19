package com.withinet.opaas.controller.system;

import java.io.File;
import java.io.IOException;

public interface FileLocationGenerator {
	public File getHomeDirectory ();
	
	public File getUserDrirectory (Long uuid);
	
	public File getInstanceDirectory (Long uuid, Long iiid);
	
	public File getConcurrentTempDirectory (Long uid);
	
	public File getResourcesDirectory ();

	public String getUserDrirectoryPath(Long uuid);

	public String getHomeDirectoryPath();

	public File getUserLibrary(Long uuid);

	public File getInstanceLogFile(Long uid, Long iid) throws IOException;
}
