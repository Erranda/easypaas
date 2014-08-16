/**
 * 
 */
package com.withinet.opaas.controller.system.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple class the pipes the output of a process to file
 * 
 * @author Folarin Omotoriogun
 * @since August 23, 2013
 */
public class Pipe {
	
	private File outputLog = null;
	
	private Process process = null;

	private volatile List<Thread> ioThreads;
	/**
	 * Constructor 
	 * @param process
	 * @param errorLog
	 * @param outputLog
	 * @throws IOException
	 */
	public Pipe(Process process, File outputLog)
			throws IOException {
		ioThreads = new ArrayList<Thread> ();
		if (!outputLog.exists())
			outputLog.createNewFile();
			this.outputLog = outputLog;
		if (process != null)
			this.process = process;
	}
	/**
	 * Starts the logging process to file
	 */
	public void start() {
		InputStream errorInputStream = process.getErrorStream();
		InputStream inputStream = process.getInputStream();
		ioThreads.add(new Thread(new WriteToFileJob(outputLog, errorInputStream, 0)));
		ioThreads.add(new Thread(new WriteToFileJob(outputLog, inputStream, 1)));	
		ioThreads.get(0).start();
		ioThreads.get(1).start();
	}
	/**
	 * Stop threads
	 */
	@SuppressWarnings("deprecation")
	public void killMonitors () {
		Thread t0 = ioThreads.get(0);
		Thread t1 = ioThreads.get(1);
		t0.stop();
		t1.stop();
	}
	/**
	 * Inner class thread job to write to file
	 * 
	 * @author Folarin Omotoriogun
	 * @since August 23, 2013
	 */
	private class WriteToFileJob implements Runnable {
		private File file;
		private InputStream inputStream;
		private volatile int threadLocation; 
		
		public WriteToFileJob(File file, InputStream inputStream, int threadLocation) {
			this.file = file;
			this.inputStream = inputStream;
			this.threadLocation = threadLocation;
		}
		
		public void run() {
			while (ioThreads.get(threadLocation) == Thread.currentThread()) {
				BufferedReader bis = new BufferedReader(new InputStreamReader(
						inputStream));
				BufferedWriter wis = null;
				String line = null;
				try {
					wis = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
					while (true) {
						if ((line = bis.readLine()) != null) {
							line = "<br/><hr/><p style=\"color:#FF0\">[" + new Date () + "]</p>" + line + "\n";
							wis.write(line);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					try {
						bis.close();
						wis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	
				}
			}
		}
	}
}
