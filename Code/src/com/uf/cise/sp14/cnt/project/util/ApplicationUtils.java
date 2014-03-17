package com.uf.cise.sp14.cnt.project.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import com.uf.cise.sp14.cnt.project.constants.ConfigKeys;

public class ApplicationUtils {
	private static PrintStream printStream = System.out;
	
	public static void printLine(String line) {
		printStream.println(line);
	}
	
	public static void exit(int status, String exitMessage) {
		printStream.println(exitMessage);
		System.exit(status);
	}

	/**
	 * Log events at the server running on a port into a file.
	 * 
	 * @param msg
	 * @param fileName
	 */
	public static void logServerPortEvent(String msg, Integer port) {
		ApplicationUtils.printLine(msg);
		
		try {
			// Create the log file if it doesn't exist yet.
			String fileName = ConfigUtils
					.getString(ConfigKeys.LogsDirectory) + "/" + port + "_ServerPort.log";
			File logFile = new File(fileName);
			logFile.getParentFile().mkdirs();
			logFile.createNewFile();
			
			// Write the requested line to the log file.
			BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(logFile, true));
			bufferWritter.write(msg + "\n");
			bufferWritter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
