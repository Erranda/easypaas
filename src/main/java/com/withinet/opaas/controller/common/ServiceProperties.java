package com.withinet.opaas.controller.common;

import java.util.Arrays;
import java.util.List;

public class ServiceProperties {

	public static final String name = "Opaas";

	public static final int REMEMBER_ME_DURATION_IN_SECONDS = 1 * 24 * 60 * 60;

	public static final String REMEMBER_ME_EMAIL_COOKIE = "opaasEmailCookie";

	public static final String REMEMBER_ME_PASSWORD_COOKIE = "opaasPasswordCookie";

    public static final String HOME = "/home/newuser/beta/cloud";
    
    //public static final String DOMAIN = "http://cloud.withinet.co.uk";
    
	//public static final String HOME = "C:/Users/Folarin/Desktop/opaas/";

	public static final String DOMAIN = "http://localhost";

	public static final int MINPORT = 9000;

	public static final int MAXPORT = 65535;

	public static final String EMAILER_USERNAME = "auto-respond@wandlist.com";

	public static final String EMAILER_PASSWORD = "!dSutjn0";

	public static final String EMAILER_SMTP_AUTH = "true";

	public static final String EMAILER_STARTTLS_ENABLE = "false";

	public static final String EMAILER_SMTP_HOST = "smtp.wandlist.com";

	public static final String EMAILER_SMTP_PORT = "587";

	public static final String SUPER_ADMIN_PASSWORD = "Folarin@123";

	public static final String SUPER_ADMIN_EMAIL = "folarinomotoriogun@gmail.com";

/*	public static final String salt = "$2a$10$C/O6cM/3gsYbve5.LEriJe";

	public static final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";*/

	public static final List<String> SIGNED_IN = Arrays.asList("signedIn", "admin");/*

	public static final List<String> UPDATE_ACCOUNT = "ACCOUNT.UPDATE.ALL";

	public static final List<String> DELETE_ACCOUNT = "ACCOUNT.DELETE.ALL";

	public static final List<String> READ_ACCOUNT = "ACCOUNT.READ.ALL";

	public static final List<String> LOGIN_ACCOUNT = "ACCOUNT.LOGIN.ALL";*/

	public static final List<String> CREATE_PROJECT = Arrays.asList("createProject", "projectAdmin", "admin");
	
	//public static final List<String> CREATE_PROJECT_BUNDLE = Arrays.asList("createProjectBundle", "projectAdmin", "admin");

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

	public static final List<String> SYSTEM_ADMIN = Arrays.asList("admin", "superAdmin");
	
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