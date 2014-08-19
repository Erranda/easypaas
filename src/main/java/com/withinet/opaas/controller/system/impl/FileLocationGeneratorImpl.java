package com.withinet.opaas.controller.system.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.controller.system.FileLocationGenerator;

@Service
public class FileLocationGeneratorImpl implements FileLocationGenerator {
	
	public static final String systemHome = ServiceProperties.HOME;
	
	public final String userHome = systemHome + "/users";
	
	public final String tempHome = systemHome + "/temp";

	@Override
	public File getHomeDirectory() {
		File home = new File (systemHome);
		if (!home.exists()) home.mkdirs();
		return home;
	}
	
	@Override
	public String getHomeDirectoryPath() {
		return systemHome;
	}

	@Override
	public File getUserDrirectory(Long uuid) {
		File home = new File (userHome +"/"+uuid);
		if (!home.exists()) home.mkdirs();
		return home;
	}
	
	@Override
	public String getUserDrirectoryPath(Long uuid) {
		return userHome +"/"+uuid;
	}
	
	@Override
	public synchronized File getUserLibrary (Long uuid) {
		File home = new File (userHome + "/"+ uuid+"/library/" + System.currentTimeMillis());
		if (!home.exists()) home.mkdirs();
		return home;
	}

	@Override
	public File getInstanceDirectory(Long uid, Long iid) {
		File userDir = getUserDrirectory (uid);
		File instanceDir = new File (userDir.getAbsolutePath() + "/instances/" + iid);
		if (!instanceDir.exists()) instanceDir.mkdirs();
		return instanceDir;
	}
	
	@Override
	public File getInstanceLogFile(Long uid, Long iid) throws IOException {
		File instanceDir = getInstanceDirectory (uid, iid);
		File logFile = new File (instanceDir.getAbsolutePath() + "/log.txt");
		if (logFile.exists())
			logFile.createNewFile();
		return logFile;
	}

	
	@Override
	public File getResourcesDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getConcurrentTempDirectory(Long uid) {
		File home = new File (tempHome + "/" + uid + "/" + System.currentTimeMillis());
		if (!home.exists()) home.mkdirs();
		return home;
	}
	
}
