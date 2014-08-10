package com.withinet.opaas.controller.system;

import com.withinet.opaas.model.domain.Instance;

public interface ProcessService {
	
	public boolean startProcess (Instance instance);
	
	public boolean stopProcess (Long id);
	
}
