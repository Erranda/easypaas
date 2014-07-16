package com.withinet.opaas.controller.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "service", ignoreUnknownFields = false)
@Component
public class ServiceProperties {

	public final String name = "Opaas";

	public final String salt = "$2a$10$C/O6cM/3gsYbve5.LEriJe";
	
	public final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	
	public final String PERM_REGISTER_ACCOUNT = "ACCOUNT.REGISTER.ALL";
	
	public final String PERM_UPDATE_ACCOUNT = "ACCOUNT.UPDATE.ALL";
	
	public final String PERM_DELETE_ACCOUNT = "ACCOUNT.DELETE.ALL";
	
	public final String PERM_READ_ACCOUNT = "ACCOUNT.READ.ALL";
	
	public final String PERM_CREATE_PROJECT = "PROJECT.CREATE.ALL";
	
	public final String PERM_READ_PROJECT = "PROJECT.READ.ALL";
	
	public final String PERM_UPDATE_PROJECT = "PROJECT.UPDATE.ALL";
	
	public final String PERM_DELETE_PROJECT = "PROJECT.DELETE.ALL";
	
	public final String PERM_CREATE_BUNDLE = "BUNDLE.CREATE.ALL";
	
	public final String PERM_DELETE_BUNDLE = "BUNDLE.DELETE.ALL";
	
	public final String PERM_UPDATE_BUNDLE = "BUNDLE.UPDATE.ALL";
	
	public final String PERM_READ_BUNDLE = "BUNDLE.READ.ALL";
	
	public final String PERM_CREATE_COLLABORATOR = "COLLABORATOR.CREATE.ALL";
	
	public final String PERM_READ_COLLABORATOR = "COLLABORATOR.READ.ALL";
	
	public final String PERM_UPDATE_COLLABORATOR = "COLLABORATOR.UPDATE.ALL";
	
	public final String PERM_DELETE_COLLABORATOR = "COLLABORATOR.DELETE.ALL";
	
	public final String PERM_CREATE_INSTANCE = "INSTANCE.CREATE.ALL";
	
	public final String PERM_READ_INSTANCE = "INSTANCE.READ.ALL";
	
	public final String PERM_UPDATE_INSTANCE = "INSTANCE.UPDATE.ALL";
	
	public final String PERM_DELETE_INSTANCE = "INSTANCE.DELETE.ALL";
	
	public final String PERM_CREATE_ORGANISATION = "ORGANISATION.CREATE.ALL";
	
	public final String PERM_READ_ORGANISATION = "ORGANISATION.READ.ALL";
	
	public final String PERM_UPDATE_ORGANISATION = "ORGANISATION.UPDATE.ALL";
	
	public final String PERM_DELETE_ORGANISATION = "ORGANISATION.DELETE.ALL";
	
	public final String PERM_CREATE_PERMISSION = "PERMISSION.CREATE.ALL";
	
	public final String PERM_READ_PERMISSION = "PERMISSION.READ.ALL";
	
	public final String PERM_UPDATE_PERMISSION = "PERMISSION.UPDATE.ALL";
	
	public final String PERM_DELETE_PERMISSION = "PERMISSION.DELETE.ALL";
	
	public final String PERM_CREATE_ROLE = "ROLE.CREATE.ALL";
	
	public final String PERM_READ_ROLE = "ROLE.READ.ALL";
	
	public final String PERM_UPDATE_ROLE = "ROLE.UPDATE.ALL";
	
	public final String PERM_DELETE_ROLE = "ROLE.DELETE.ALL";
	
	
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