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
			ApplicationUtils.printLine("ERR: Could not load: " + ConfigsFile);
			System.exit(1);
		}
	}
	
	public static Boolean isDebug() {
		return ConfigUtils.getBoolean(ConfigKeys.IsDebug);
	}
	
	public static Boolean getBoolean(String key) {
		return Boolean.valueOf( properties.getProperty(key) );
	}
	
	public static String getString(String key) {
		return properties.getProperty(key);
	}
	
	public static Integer getInteger(String key) {
		return Integer.valueOf( properties.getProperty(key) );
	}
}
