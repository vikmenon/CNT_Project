package com.uf.cise.sp14.cnt.project;

import java.io.File;

import com.uf.cise.sp14.cnt.project.exception.FileShareException;
import com.uf.cise.sp14.cnt.project.util.FileUtils;

public class TestFileSharer {
	
	/** This code tests FileUtil.
	 * @param args
	 * @throws FileShareException
	 */
	public static void main(String args[]) throws FileShareException {
		String testFileName = "GDP.csv";
		File testFile = new File(testFileName);
		
		FileUtils.splitFile(testFile, 10);
		FileUtils.mergeFile(testFileName, testFile.length());
	}
}
