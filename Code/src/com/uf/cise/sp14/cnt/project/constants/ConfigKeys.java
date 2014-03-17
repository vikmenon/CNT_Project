package com.uf.cise.sp14.cnt.project.constants;

/**
 * @author vikmenon
 *
 * This class holds keys which are present in the configuration file.
 */
public class ConfigKeys {
	/**
	 * This flag decides whether to run in debug mode, which prints additional logs.
	 */
	public static final String IsDebug = "isDebug";
	
	/**
	 * This is the directory where parts of files that we want to send/receive are stored.
	 */
	public static final String PartsDirectory = "PartsDirectory";
	
	/**
	 * This is the directory where we hold files that have finished downloading.
	 */
	public static final String DownloadsDirectory = "DownloadsDirectory";

	/**
	 * This is the directory where we write out logfiles.
	 */
	public static final String LogsDirectory = "LogsDirectory";
	
	/**
	 * This is the suffix used to indicate a file part.
	 */
	public static final String PartSuffix = "PartSuffix";
	
	/**
	 * This is the maximum size of each part file.
	 */
	public static final String PartSize = "PartSize";
	
	/**
	 * This is the size of the buffer used to read and write files.
	 */
	public static final String FileReadBufferSize = "FileReadBufferSize";
}
