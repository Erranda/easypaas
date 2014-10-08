package com.withinet.opaas.controller.common;

import java.util.Arrays;
import java.util.List;

import com.withinet.opaas.PropertiesInstaller;

public class ServiceProperties {

	public static final String name = "EasyPaaS Cloud";

	public static final int REMEMBER_ME_DURATION_IN_SECONDS = 1 * 24 * 60 * 60;

	public static final String REMEMBER_ME_EMAIL_COOKIE = "opaasEmailCookie";

	public static final String REMEMBER_ME_PASSWORD_COOKIE = "opaasPasswordCookie";

    public static final String HOME = "jarcloud";
    
    public static final String DOMAIN = PropertiesInstaller.get("domain");
    
	//public static final String HOME = "C:/Users/Folarin/Desktop/opaas/";

	//public static final String DOMAIN = "http://localhost";

	public static final int MINPORT = Integer.parseInt (PropertiesInstaller.get("min.port"));

	public static final int MAXPORT = Integer.parseInt(PropertiesInstaller.get("max.port"));

	public static final String EMAILER_USERNAME = PropertiesInstaller.get("mailer.email");

	public static final String EMAILER_PASSWORD = PropertiesInstaller.get("mailer.password");

	public static final String EMAILER_SMTP_AUTH = PropertiesInstaller.get("mailer.auth");

	public static final String EMAILER_STARTTLS_ENABLE = PropertiesInstaller.get("mailer.ttls");

	public static final String EMAILER_SMTP_HOST = PropertiesInstaller.get("mailer.host");

	public static final String EMAILER_SMTP_PORT = PropertiesInstaller.get("mailer.port");

	public static final String SUPER_ADMIN_PASSWORD = PropertiesInstaller.get("password");

	public static final String SUPER_ADMIN_EMAIL = PropertiesInstaller.get("email");
	
	public static final String SUPER_ADMIN_REAL_NAME = PropertiesInstaller.get("name");
	
	public static final String SECURITY_BUNDLE_LOCATION = PropertiesInstaller.get("security.bundle");

/*  public static final String salt = "$2a$10$C/O6cM/3gsYbve5.LEriJe";

	public static final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"; */

	public static final List<String> SIGNED_IN = Arrays.asList("signedIn", "admin");/*

	public static final List<String> UPDATE_ACCOUNT = "ACCOUNT.UPDATE.ALL";

	public static final List<String> DELETE_ACCOUNT = "ACCOUNT.DELETE.ALL";

	public static final List<String> READ_ACCOUNT = "ACCOUNT.READ.ALL";

	public static final List<String> LOGIN_ACCOUNT = "ACCOUNT.LOGIN.ALL";*/

	public static final List<String> CREATE_PROJECT = Arrays.asList("createProject", "projectAdmin", "admin");
	
	// public static final List<String> CREATE_PROJECT_BUNDLE = Arrays.asList("createProjectBundle", "projectAdmin", "admin");

	public static final List<String> READ_PROJECT = Arrays.asList("readProject", "projectAdmin", "admin");

	public static final List<String> UPDATE_PROJECT = Arrays.asList("updateProject", "projectAdmin", "admin");
	
	public static final List<String> DISABLE_PROJECT = Arrays.asList("disableProject", "projectAdmin", "admin");
	
	public static final List<String> DELETE_PROJECT = Arrays.asList("deleteProject", "projectAdmin", "admin");

	public static final List<String> CREATE_BUNDLE = Arrays.asList("createProject", "projectAdmin", "createBundle", "bundleAdmin", "admin");

	public static final List<String> DELETE_BUNDLE = Arrays.asList("deleteBundle", "bundleAdmin", "admin");

	public static final List<String> UPDATE_BUNDLE = Arrays.asList("updateBundle", "bundleAdmin", "admin");

	public static final List<String> READ_BUNDLE = Arrays.asList("createProject", "projectAdmin", "readBundle", "bundleAdmin", "admin");

/*	public static final List<String> CREATE_COLLABORATOR = "COLLABORATOR.CREATE.ALL";

	public static final List<String> READ_COLLABORATOR = "COLLABORATOR.READ.ALL";

	public static final List<String> UPDATE_COLLABORATOR = "COLLABORATOR.UPDATE.ALL";

	public static final List<String> DELETE_COLLABORATOR = "COLLABORATOR.DELETE.ALL";*/
	
	public static final List<String> ACCESS_INSTANCE_LOG = Arrays.asList("instanceLog", "instanceAdmin", "admin");

	public static final List<String> ACCESS_INSTANCE_CONSOLE = Arrays.asList("instanceConsole", "instanceAdmin", "admin");
	
	public static final List<String> START_INSTANCE = Arrays.asList("startInstance", "instanceAdmin", "admin");
	
	public static final List<String> STOP_INSTANCE = Arrays.asList("deleteInstance", "stopInstance", "instanceAdmin", "admin");

	public static final List<String> CREATE_INSTANCE = Arrays.asList("createInstance", "instanceAdmin", "admin");
	
	public static final List<String> READ_INSTANCE = Arrays.asList("readInstance", "instanceAdmin", "admin");

	public static final List<String> UPDATE_INSTANCE = Arrays.asList("updateInstance", "instanceAdmin", "admin");

	public static final List<String> DELETE_INSTANCE = Arrays.asList("deleteInstance", "instanceAdmin", "admin");

/*	public static final List<String> CREATE_ORGANISATION = "ORGANISATION.CREATE.ALL";

	public static final List<String> READ_ORGANISATION = "ORGANISATION.READ.ALL";

	public static final List<String> UPDATE_ORGANISATION = "ORGANISATION.UPDATE.ALL";

	public static final List<String> DELETE_ORGANISATION = "ORGANISATION.DELETE.ALL";

	public static final List<String> CREATE_PERMISSION = "PERMISSION.CREATE.ALL";

	public static final List<String> READ_PERMISSION = "PERMISSION.READ.ALL";

	public static final List<String> UPDATE_PERMISSION = "PERMISSION.UPDATE.ALL";

	public static final List<String> DELETE_PERMISSION = "PERMISSION.DELETE.ALL";

	public static final List<String> CREATE_ROLE = "ROLE.CREATE.ALL";

	public static final List<String> READ_ROLE = "ROLE.READ.ALL";

	public static final List<String> UPDATE_ROLE = "ROLE.UPDATE.ALL";

	public static final List<String> DELETE_ROLE = "ROLE.DELETE.ALL";*/

	public static final List<String> SYSTEM_ADMIN = Arrays.asList("admin");
	
	public static final List<String> SUPER_ADMIN = Arrays.asList("superAdmin");
	
	public static final String SUPER_ADMIN_NAME = "SUPER ADMINISTRATOR";
	
	public static final String ADMINISTRATOR_NAME = "ADMINISTRATOR";
	
	public static final String AUTHENTICATED_NAME = "AUTHENTICATED";

	// public static final List<String> ORG_ADMIN = "ORG.ADMIN";

	public String getName() {
		return this.name;
	}

/*	public String getSalt() {
		return salt;
	}

	public String getRegex() {
		return regex;
	}*/

}