package Automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class XconfigProperties {
	
	
	public static String properties(String key) {
		File fl = new File(Constant.ResourcePath.XConfigFile);
		FileInputStream file;
		try {
			file = new FileInputStream(fl);
			Properties rpop = new Properties();
			try {
				rpop.load(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String data = rpop.getProperty(key);
			return data;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
