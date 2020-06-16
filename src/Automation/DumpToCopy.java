package Automation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class DumpToCopy {

	public static void dumpToCopy(File srcDir, File destDir) {
		try {
		    FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
}
