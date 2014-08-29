/**
 * 
 */
package com.withinet.opaas.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Folarin
 *
 */
public class EasyWriter {
	public static void write (List<String> lines, String location) throws IOException {
		File file = new File (location);
		new File (file.getParent()).mkdirs();
		file.createNewFile();
		PrintWriter writer = new PrintWriter(location, "UTF-8");
		for (String line : lines) {
			writer.println(line);
		}
		writer.close();
	}
}
