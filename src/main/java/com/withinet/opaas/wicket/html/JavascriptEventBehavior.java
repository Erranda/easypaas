package com.withinet.opaas.wicket.html;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

class JavascriptEventBehaviour extends Behavior {
	/**
	 * 
	 */
	private static final long serialVersionUID = 196832951326145878L;
	private final String msg;
	private final String event;

	public JavascriptEventBehaviour(String event, String msg) {
		this.msg = msg;
		this.event = event;
	}

	protected void onComponentTag(ComponentTag tag) {
		String script = (String) tag.getAttributes().get(event);
		script = "if (!confirm('" + msg + "')) return false; " + script;
		tag.put(event, "onclick");
	}
}