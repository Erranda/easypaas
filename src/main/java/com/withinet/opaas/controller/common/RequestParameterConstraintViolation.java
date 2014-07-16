package com.withinet.opaas.controller.common;

public class RequestParameterConstraintViolation extends ControllerException {

	private static final long serialVersionUID = -8692416416165881154L;
	
	public RequestParameterConstraintViolation (String message) {
		super (message);
	}

}
