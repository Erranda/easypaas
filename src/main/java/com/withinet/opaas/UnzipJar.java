package com.withinet.opaas;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Enumeration;
 
public class UnzipJar {
 
	public static void unzipJar(String jarPath, String searchName) throws IOException {
		File file = new File(jarPath);
		JarFile jar = new JarFile(file);
 
		//now create all files
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			JarEntry entry = (JarEntry) enums.nextElement();
			if (entry.getName().equals(searchName) ) {
				String fileName = entry.getName();
				File f = new File(fileName);
				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(f);
				while (is.available() > 0) {
						fos.write(is.read());
				}
	 
					fos.close();
					is.close();
				}
			}
			
		}
	}