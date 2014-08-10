/**
 * 
 */
package com.withinet.opaas.wicket.services;

/**
 * Decouples feedback messages from pages
 * 
 * @author Folarin
 *
 */
public class FeedbackMessage {
	
	private String message;
	
	private int level;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
