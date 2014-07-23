package com.withinet.opaas.controller.common;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

public interface LogService {

	public Logger getLogger (String packageName) ;
}
