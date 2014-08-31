package com.withinet.opaas.controller.system.impl;

import org.ops4j.pax.runner.Run;
import org.ops4j.pax.runner.platform.PlatformException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.common.ContainerLauncherException;
import com.withinet.opaas.controller.system.ContainerLauncher;
import com.withinet.opaas.model.domain.Instance;

import java.util.ArrayList;
import java.util.List;
/**
 * Provides functionality to start an OSGi Container based on Pax Runner
 * This component is not multithreaded and requires multithreading to be implemented by its caller
 * @author Folarin Omotoriogun
 * 
 */

public class PaxRunner extends Run {
	/**
	 * Method provides functionality to start an OSGI process
	 * 
	 * @param parameters
	 * @return
	 * @throws ContainerLauncherException
	 */
	public static Process startContainer(String... parameters) throws PlatformException {
		PaxJavaRunner runner = new PaxJavaRunner(false);
		main(runner, parameters);
		return runner.getFrameWorkProcess();
	}
}
