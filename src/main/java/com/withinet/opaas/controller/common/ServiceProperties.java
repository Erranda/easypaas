package com.withinet.opaas.controller.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "service", ignoreUnknownFields = false)
@Component
public class ServiceProperties {

	private final String name = "Opaas";

	private final String salt = "$2a$10$C/O6cM/3gsYbve5.LEriJe";
	
	private final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"; 
	
	public String getName() {
		return this.name;
	}

	public String getSalt() {
		return salt;
	}

	public String getRegex() {
		return regex;
	}

}