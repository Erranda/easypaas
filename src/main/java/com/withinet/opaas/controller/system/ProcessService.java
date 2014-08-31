package com.withinet.opaas.controller.system;

import com.withinet.opaas.controller.system.impl.ProcessServiceException;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.User;

public interface ProcessService {
	
	public boolean startProcess (Instance instance, User user) throws ProcessServiceException;
	
	public boolean stopProcess (Long id);
	
}
