package com.uf.cise.sp14.cnt.project.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.uf.cise.sp14.cnt.project.constants.ConfigKeys;

public class ConfigUtils {
	private static final String ConfigsFile = "configs.properties";
	private static final Properties properties = new Properties();
	
	static {
		try {
			properties.load(new FileInputStream(ConfigUtils.ConfigsFile));
			ApplicationUtils.printLine("Configs: " + properties.toString());
		} catch (IOException e) {
			ApplicationUtils.exit(1, "ERR: Could not load: " + ConfigsFile);
		}
	}
	
	public static String getConfig(String key) {
		String retVal = properties.getProperty(key);
		if (null == retVal) {
			// If the requested configuration was not set, then we should terminate.
			ApplicationUtils.exit(1, "ERR: Configuration is not set: " + key);
		}
		return retVal;
	}
	
	public static Boolean getBoolean(String key) {
		return Boolean.valueOf( getConfig(key) );
	}
	
	public static String getString(String key) {
		return getConfig(key);
	}
	
	public static Integer getInteger(String key) {
		return Integer.valueOf( getConfig(key) );
	}
	
	public static Boolean isDebug() {
		return ConfigUtils.getBoolean(ConfigKeys.IsDebug);
	}
}
