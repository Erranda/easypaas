package com.withinet.opaas.controller.system;

import com.withinet.opaas.controller.system.impl.ProcessServiceException;
import com.withinet.opaas.model.domain.Instance;

public interface ProcessService {
	
	public boolean startProcess (Instance instance) throws ProcessServiceException;
	
	public boolean stopProcess (Long id);
	
}
