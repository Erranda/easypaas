/**
 * 
 */
package com.withinet.opaas.controller.system.impl;

import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.system.ProcessService;
import com.withinet.opaas.model.domain.Instance;

/**
 * @author Folarin
 *
 */
@Service
public class ProcessServiceImpl implements ProcessService {

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.system.ProcessService#startProcess(com.withinet.opaas.model.domain.Instance)
	 */
	@Override
	public boolean startProcess(Instance instance) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.system.ProcessService#stopProcess(java.lang.Long)
	 */
	@Override
	public boolean stopProcess(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
