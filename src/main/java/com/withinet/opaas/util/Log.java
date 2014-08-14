package com.withinet.opaas.util;

import java.io.IOException;

public class Log {
	public static String getLog (String workingDirectory) throws IOException {
		return EasyReader.getString(workingDirectory).toString();
	}
}