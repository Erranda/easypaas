package com.withinet.opaas.controller.system;

public class Validation {
	
	public static void assertNotNull (Object o) throws IllegalArgumentException {
		if (o == null) throw new IllegalArgumentException ();
	}
	
}
