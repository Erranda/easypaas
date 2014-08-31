/**
 * 
 */
package com.withinet.opaas.controller.system.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ops4j.pax.runner.platform.PlatformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.controller.system.ProcessService;
import com.withinet.opaas.controller.system.Validation;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.ProjectBundle;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.util.EasyWriter;

/**
 * @author Folarin
 *
 */
@Service
public class ProcessServiceImpl implements ProcessService {
	
	
	BundleController bundleController;
	
	@Autowired
	public void setBundleController (BundleController bundleController) {
		this.bundleController = bundleController;
	}
	
	Map<Long, Process> liveProcesses = new ConcurrentHashMap <Long, Process> ();
	
	Map<Long, Pipe> logPipes = new ConcurrentHashMap <Long, Pipe> ();
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.system.ProcessService#startProcess(com.withinet.opaas.model.domain.Instance)
	 */
	@Override
	public boolean startProcess(Instance instance, User user) throws ProcessServiceException {
		Validation.assertNotNull(instance);
		if (instance.getWorkingDirectory() == null)
			throw new ProcessServiceException ("Working directory cannot be null for instance");
		Long pid = instance.getProject().getID();
		Validation.assertNotNull(pid);
		Long uid = instance.getOwner().getID();
		Validation.assertNotNull(uid);
		Long iid = instance.getId();
		Validation.assertNotNull(iid);
		String logLocation = instance.getLogFile();
		Validation.assertNotNull(logLocation);
		try {
			List<String> config = new ArrayList<String> ();
			String configDir = instance.getWorkingDirectory() + "/config_instance";
			String policyLocation = configDir + "/all.policy"; 
			String instanceConfigLocation = configDir + "/org.apache.felix.webconsole.internal.servlet.OsgiManager.cfg";
			writeConfig (instance,  instanceConfigLocation, policyLocation);
			for (ProjectBundle bundle : instance.getProject().getProjectBundles()) {
				config.add(bundle.getBundle().getLocation() + "@10");
			}
			config.add(ServiceProperties.SECURITY_BUNDLE_LOCATION);
			config.add("--dir=" + instance.getWorkingDirectory());
			if (instance.getPort() == null)
				throw new ProcessServiceException ("Port number cannot be null");
			config.add("--vmo=-Dorg.osgi.service.http.port=" + instance.getPort() + " " +
							   "-Dfelix.fileinstall.dir="+ configDir + " " +
							   "-Dinstance.home=" + instance.getWorkingDirectory() + " " +
							   "-Djava.security.policy="+policyLocation + " " +
							   "-Dorg.osgi.framework.security=osgi" + " " +
							   "-Dinstance.username=" + user.getEmail() + " " +
							   "-Dinstance.password=" + user.getPassword()
					);
			config.add("--skipInvalidBundles");
			config.add("--platform="+instance.getContainerType().toLowerCase().trim());	
			config.add("--usePersistedState="+ !instance.isDirty());
			config.addAll(Profiles.getInstance().getWeb());
			String[] configs = config.toArray(new String[config.size()]);
			Process thisProcess = PaxRunner.startContainer(configs);
			liveProcesses.put(iid, thisProcess);
			beginLogging (thisProcess, iid, logLocation);
			return true;
		} catch (IOException e) {
			throw new ProcessServiceException (e.getMessage());
		} catch (RuntimeException e) {
			throw new ProcessServiceException (e.getMessage());
		} catch (PlatformException e) {
			throw new ProcessServiceException (e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.system.ProcessService#stopProcess(java.lang.Long)
	 */
	@Override
	public boolean stopProcess(Long id) {
		if (liveProcesses.containsKey(id)) {
			liveProcesses.get(id).destroy();
			if (logPipes.containsKey(id)) {
				logPipes.get(id).killMonitors();
				
			} else { 
				//throw new ProcessServiceException ("Process not found in registry");
			}	
		} else {
			
			//throw new ProcessServiceException ("Process not found in registry");
		}
		return true;
	}
	
	/**
	 * Creates pipes and adds the process to list of managed processes
	 */
	private synchronized void beginLogging (Process process, Long instanceId, String logFileLocation) throws IOException {
		File logFile = new File (logFileLocation);
		Pipe pipe = new Pipe(process, logFile);
		logPipes.put(instanceId, pipe);
		pipe.start();		
	}
	
	private void writeConfig (Instance instance, String location, String policyLocation) throws IOException {
		List<String> config = Arrays.asList("username=" + instance.getOwner().getEmail(), "password=" + instance.getOwner().getPassword());
		EasyWriter.write(config, location);
		List<String> policy = Arrays.asList("grant {", "permission java.security.AllPermission;", "};");
		EasyWriter.write(policy, policyLocation);
	}

}
