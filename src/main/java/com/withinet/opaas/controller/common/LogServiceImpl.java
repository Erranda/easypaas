package com.withinet.opaas.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

	public Logger getLogger (String packageName) {
		 Logger logger = LoggerFactory.getLogger(packageName);
		 return logger;
	}
}
