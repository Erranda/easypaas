/**
 * 
 */
package com.withinet.opaas.controller.system.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Folarin
 *
 */
public class Profiles {
	
	private static Profiles instance = null;
	
	private static final List<String> CORE = new ArrayList<String> ();
	
	private Profiles () {}
	
	public static Profiles getInstance () {
		if (instance == null) {
			instance = new Profiles ();
			init ();
		}
		return instance;
	}
	
	private static void init() {
		CORE.add("mvn:org.apache.felix/org.osgi.compendium@1");
		CORE.add("mvn:org.ops4j.pax.logging/pax-logging-api/1.4@2");
		CORE.add("mvn:org.ops4j.pax.logging/pax-logging-service/1.4@2");
		CORE.add("mvn:org.apache.felix/org.apache.felix.configadmin/1.6.0@2");
		CORE.add("mvn:org.apache.felix/org.apache.felix.framework.security/2.2.0@3");
		CORE.add("mvn:org.apache.felix/org.apache.felix.scr@5");
		CORE.add("mvn:org.ops4j.pax.web/pax-web-jetty-bundle/1.0.2@6");
		CORE.add("mvn:org.apache.felix/org.apache.felix.webconsole/3.0.0@6");
		CORE.add("mvn:org.apache.felix/org.apache.felix.webconsole.plugins.event/1.0.2@6");
		CORE.add("mvn:org.apache.felix/org.apache.felix.webconsole.plugins.memoryusage/1.0.0@6");
	}

	public List<String> getWeb () {
		return CORE;
	}

}
