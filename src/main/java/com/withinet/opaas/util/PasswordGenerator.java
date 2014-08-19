/**
 * 
 */
package com.withinet.opaas.util;

/**
 * @author Folarin
 *
 */
public class PasswordGenerator {
	
	public static String getRandomPassword () {
		char[] chars = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
				'0','1','2','3','4','5','6','7','8','9','0'};
		
		StringBuffer password = new StringBuffer ();
		
		for(int i = 0; i < 10; i++) {
			password.append(chars[(int) (Math.random()*chars.length)]);
		}
		
		return password.toString();
		
	}
}
