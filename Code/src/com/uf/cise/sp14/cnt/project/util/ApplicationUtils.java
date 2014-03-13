package com.uf.cise.sp14.cnt.project.util;

import java.io.PrintStream;

public class ApplicationUtils {
	private static PrintStream printStream = System.out;
	
	public static void printLine(String line) {
		printStream.println(line);
	}
}
