package com.withinet.opaas.controller.system;

import com.withinet.opaas.controller.common.ContainerLauncherException;
import com.withinet.opaas.model.domain.Instance;


/**
 * Provides functionality to start an OSGi Container based on Pax Runner Project
 * @author Folarin Omotoriogun
 *
 */
public interface ContainerLauncher {
    /**
     * Method provides functionality to start an OSGI process
     * @param instance 
     */
    public Process startContainer (Instance instance) throws ContainerLauncherException;
}
