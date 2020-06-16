package Automation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Unziper {

	public static void unZiper() {
		try{
			
			// location of the zip folder
			String zipFilePath = DSTAutomation.propertiesExternal("zippath");
			// location of destination folder
			// folder to be created on ENTSServer
			String destDir = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\DUMP";
			File theDirC = new File(destDir);

			// Restricting the multiple unzip option
			if(!theDirC.exists()) {
				// call unzip method and copy all file to the DUMP Folder
				UnzipFile.unzip(zipFilePath, destDir);
				System.out.println("Unziped");
			}
			
//			System.out.println("Zip has been Unzipped");
			// Copy all folder from DUMP to COPY(Backup)
			File srcDir = new File(destDir);
			// Destination
			String destination = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY";
			File copyDir = new File(destination);
			if(!copyDir.exists()){
				DumpToCopy.dumpToCopy(srcDir, copyDir);
			}
			Thread.sleep(500);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
