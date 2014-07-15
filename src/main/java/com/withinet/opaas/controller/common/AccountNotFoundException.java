package com.withinet.opaas.controller.common;

public class AccountNotFoundException extends AccountControllerException {

	private static final long serialVersionUID = -8692416416165881154L;
	
	public AccountNotFoundException (String message) {
		super (message);
	}

}
