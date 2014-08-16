/**
 * 
 */
package com.withinet.opaas.controller.system.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.system.ProcessService;
import com.withinet.opaas.controller.system.Validation;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Instance;

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
	public boolean startProcess(Instance instance) throws ProcessServiceException {
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
			List<Bundle> bundles = bundleController.listBundlesByProject(pid, uid);
			List<String> config = new ArrayList<String> ();
			for (Bundle bundle : bundles) {
				config.add(bundle.getLocation());
			}
			config.add("--dir=" + instance.getWorkingDirectory());
			if (instance.getPort() == null)
				throw new ProcessServiceException ("Port number cannot be null");
			config.add("--vmo=-Dorg.osgi.service.http.port=" + instance.getPort()
					);
			//config.add("--noBundleValidation");
			config.add("--platform="+instance.getContainerType().toLowerCase().trim());
			if (instance.getStatus().equals("Dead"))
				config.add("--usePersistedState=true");
			config.addAll(Profiles.getInstance().getWeb());
			String[] configs = config.toArray(new String[config.size()]);
			Process thisProcess = PaxRunner.startContainer(configs);
			liveProcesses.put(iid, thisProcess);
			beginLogging (thisProcess, iid, logLocation);
			return true;
		} catch (BundleControllerException e) {
			throw new ProcessServiceException (e.getMessage());
		} catch (IOException e) {
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

}
