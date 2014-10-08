package com.withinet.opaas;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertiesInstaller {

	public static void install() {			
		String input = null;
		if (new File ("application.properties").exists()) {
			input = PropertiesInstaller.ask ("Continue previous application context? (Y or N)", Arrays.asList("Y", "N", "y", "n")).toUpperCase();
			if (input.toUpperCase().equals("N")) {
				run ();
			} 
		} else {
			 run ();
		}	
		verifyConfig ();
	}
	
	private static void verifyConfig () {
		String s = PropertiesInstaller.get("domain");
		if (s == null) throw new RuntimeException ("Domain name not configured check application.properties file");
		try {
			s = PropertiesInstaller.get("min.port");
			Integer.parseInt(s);
			s = PropertiesInstaller.get("max.port");
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new RuntimeException ("Port number not integer check application.properties file");
		}
		s = PropertiesInstaller.get("security.bundle");
		if (s == null) throw new RuntimeException ("Security bundle location not configured check application.properties file");
		if (!new File (s).exists()) throw new RuntimeException ("Security bundle not in configured location " + s );
		
			
			
	}
	
	private static void run () {
		String input = null;
		Properties p = new Properties();
		if (!new File ("security-1.0.0.jar").exists()){
			try {
				extractSecurityBundle (p);
			} catch (IOException e) {
				throw new RuntimeException (e.getMessage());
			}
		}
		try {
			p.setProperty("security.bundle", new File ("security-1.0.0.jar").getCanonicalPath());
		} catch (IOException e1) {
			throw new RuntimeException (e1.getMessage());
		}
		input = ask ("What is your name?");
		System.out.println ("******************************************************");
		p.setProperty("name", input);
		p.setProperty("email", ask ("What is your email address?"));
		System.out.println ("******************************************************");
		p.setProperty("password", ask ("What is your password?"));
		System.out.println ("******************************************************");
		p.setProperty("domain", ask("What is the domain name or ip of this server?"));
		System.out.println ("******************************************************");
		String minport = ask("Minimum port for deployed application containers? (1 - 65535)");
		System.out.println ("******************************************************");
		p.setProperty("min.port", minport);
		p.setProperty("max.port", ask("Maximum port for deployed application containers? (" + minport + " - 65535)"));
		System.out.println ("******************************************************");
		p.setProperty("spring.jpa.hibernate.ddl-auto", "update");
		p.setProperty("spring.jpa.show-sql", "false");
		p.setProperty("spring.datasource.driverClassName",
				"org.hsqldb.jdbcDriver");
		p.setProperty("server.port", "80");
		p.setProperty("server.sessionTimeout", "100000");
		p.setProperty("spring.datasource.username", "sa");
		p.setProperty("spring.datasource.password", "");
		p.setProperty("spring.datasource.url", "jdbc:hsqldb:mem:aname");
		OutputStream o = null;
		try {
			o = new FileOutputStream("application.properties");
			input = ask(
					"Run in default mode (Y or N)? \n Default is non persistent and port 80 must be open", Arrays.asList("Y", "N", "y", "n"))
					.toUpperCase();
			if (input.toUpperCase().equals("Y")) {
				email (p);
				p.store(o, "Default");
			} else if (input.toUpperCase().equals("N")) {
				input = ask("What's your web server port for this application?");
				System.out.println ("******************************************************");
				p.setProperty("server.port", input);
				input = ask("Do you have a running database server? Y or N", Arrays.asList("Y", "N", "y", "n"));
				System.out.println ("******************************************************");
				if (input.toUpperCase().equals("Y")) {
					input = ask("What's the jdbc data source url e.g jdbc:hsqldb:hsql://localhost:9001?");
					System.out.println ("******************************************************");
					p.setProperty("spring.datasource.url", input);
					input = ask("What's the database username?");
					System.out.println ("******************************************************");
					p.setProperty("spring.datasource.username", input);
					input = ask("What's the database password?");
					System.out.println ("******************************************************");
					p.setProperty("spring.datasource.password", input);
				} else if (input.toUpperCase().equals("N")) {
					p.setProperty("spring.datasource.url",
							"jdbc:hsqldb:mem:aname");
				}
				email (p);
				p.store(o, "Non default settings");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (o != null)
					o.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void extractSecurityBundle (Properties p) throws IOException {
		String location = new File(PropertiesInstaller.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath().toString();
		location = location.split("file:")[1].replace("!", "");
		UnzipJar.unzipJar(location, "security-1.0.0.jar");
	}
	
	private static void email (Properties p) {
		String input = null;
		input = ask ("Would you like to setup email \n-> Needed for team functionality? (Y or N)" ,Arrays.asList("Y", "N", "y", "n"));
		if (input.toUpperCase().equals("Y")) {
			input = ask ("What's the email account to dispatch emails:");
			p.setProperty("mailer.email", input);
			input = ask ("What's the email password?");
			p.setProperty("mailer.password", input);
			input = ask ("What's the host smtp address?");
			p.setProperty("mailer.host", input);
			input = ask ("SMTP Port Number?");
			p.setProperty("mailer.port", input);
			input = ask ("Startttls enabled (true or false)?",Arrays.asList("true", "false"));
			p.setProperty ("mailer.ttls", input);
			p.setProperty("mailer.auth", "true");
		}		
	}

	private static String ask(String question) {
		System.out.println(question);
		Console console = System.console();
		String response = console.readLine("Enter input:");
		if (response != null && response.length() > 0)
			return response;
		else {
			System.out.println("\nError: Your response is required\n");
			ask (question);
		}
			
		return response;
	}
	
	private static String ask(String question, List<String> expected) {
		System.out.println(question);
		Console console = System.console();
		String response = console.readLine("Enter input:");
		if (expected.contains(response))
			return response;
		else {
			System.out.println("\nError: That input is not recognized\n");
			ask (question, expected);
		}
			
		return response;
	}

	public static String get(String property) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("application.properties");
			prop.load(input);
			return (String) prop.get(property);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	} 
}
