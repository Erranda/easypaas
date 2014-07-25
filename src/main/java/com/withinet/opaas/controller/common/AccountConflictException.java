package com.withinet.opaas.controller.common;

public class AccountConflictException extends UserControllerException {

	private static final long serialVersionUID = -8692416416165881154L;
	
	public AccountConflictException (String message) {
		super (message);
	}

}
