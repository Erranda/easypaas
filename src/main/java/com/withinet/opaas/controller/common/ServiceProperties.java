package com.withinet.opaas.controller.common;

public class ServiceProperties {

	public final String name = "Opaas";
	
    public static final int REMEMBER_ME_DURATION_IN_SECONDS = 1 * 24 * 60 * 60;
    
    public static final String REMEMBER_ME_EMAIL_COOKIE = "opaasEmailCookie";
    
    public static final String REMEMBER_ME_PASSWORD_COOKIE = "opaasPasswordCookie";
    
    public static final String HOME = "C:/Users/Folarin/Desktop/opaas/";
    
    public static final String DOMAIN = "http://localhost";
    
    public static final int MINPORT = 9000;
    
    public static final int MAXPORT = 65535;
    
    public static final String EMAILER_USERNAME = "withinetsolutions@gmail.com";
    
    public static final String EMAILER_PASSWORD = "Xda3txyn";
    
  public static final String  EMAILER_SMTP_AUTH = "true";
    
    public static final String EMAILER_STARTTLS_ENABLE =  "true";
    
    public static final String EMAILER_SMTP_HOST = "smtp.gmail.com";
    
    public static final String EMAILER_SMTP_PORT = "587";

	public static final String SUPER_ADMIN_PASSWORD = "ieO8UD5j60";

	public static final String SUPER_ADMIN_EMAIL = "folarinomotoriogun@gmail.com";
    
    

	public final String salt = "$2a$10$C/O6cM/3gsYbve5.LEriJe";
	
	public final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	
	public final String PERM_CREATE_ACCOUNT = "ACCOUNT.REGISTER.ALL";
	
	public final String PERM_UPDATE_ACCOUNT = "ACCOUNT.UPDATE.ALL";
	
	public final String PERM_DELETE_ACCOUNT = "ACCOUNT.DELETE.ALL";
	
	public final String PERM_READ_ACCOUNT = "ACCOUNT.READ.ALL";
	
	public final String PERM_LOGIN_ACCOUNT = "ACCOUNT.LOGIN.ALL";
	
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
	
	public final String PERM_SYSTEM_ADMIN = "MASTER.ADMIN";
	
	public final String PERM_ORG_ADMIN = "ORG.ADMIN";
	
	
	
	
	
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