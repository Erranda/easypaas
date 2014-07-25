package com.withinet.opaas.controller.system.impl;

import java.io.File;
import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.system.FileLocationGenerator;

@Service
public class FileLocationGeneratorImpl implements FileLocationGenerator {
	
	public static final String systemHome = "C:/Users/Folarin/Desktop/opaas";
	
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
	public File getInstanceDirectory(Long iiid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized File getTempDirectory() {
		File home = new File (tempHome + "/" + System.currentTimeMillis());
		if (!home.exists()) home.mkdirs();
		return home;
	}
	
	@Override
	public synchronized String getTempDirectoryPath () {
		return tempHome + "/"+ System.currentTimeMillis();
	}
	
	@Override
	public File getResourcesDirectory() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
