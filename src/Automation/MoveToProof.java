package Automation;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

import com.aventstack.extentreports.utils.FileUtil;

public class MoveToProof {
	public static void moveToProof(String forTHEfolder, int rowCount) {
		String proofDirectory = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\PROOF";
		String validation = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\ExtentReport\\";
		File valC = new File(validation);
		if (!valC.exists()) {
			valC.mkdir();
		}
		File theDirC = new File(proofDirectory);
		if (!theDirC.exists()) {
			try {
				theDirC.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		}
		try {
			// Copy1 TO Proof
			String source = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\"+forTHEfolder;
			File srcDir = new File(source);
			
			String copy1 = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\";
			File cpy = new File(copy1);

			String destination = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\PROOF\\"+forTHEfolder;
			File destDir = new File(destination);
			if(!destDir.exists()){
				destDir.mkdir();
			}
			int noOfFiles = new File(source).list().length;
			if(noOfFiles == 2){
				Files.move(new File(source).toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
			}else{
				FileUtils.deleteDirectory(srcDir);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
