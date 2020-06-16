package Automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFile {

	public static void unzip(String zipFilePath, String destDir) throws InterruptedException {
		File dir = new File(destDir);
		// move the excel from DUMP to Put_The_Excel_File
		String mvExcel = Constant.ResourcePath.mvExcel;
		//File theDirCExcel = new File(mvExcel);
		// create output directory if it doesn't exist
		if(!dir.exists()) dir.mkdirs();
		FileInputStream fis;
		//buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while(ze != null){
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				//System.out.println("Unzipping to "+newFile.getAbsolutePath());

				File theSRCdir = new File(newFile.getAbsolutePath());

				//create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				//close this ZipEntry
				zis.closeEntry();
				//Move the file
				if(newFile.getName().trim().endsWith(".xlsx")){
					String putTheExcelFile =Constant.ResourcePath.mvExcel;
					File fs = new File(putTheExcelFile);
					if (!fs.exists()) {
						fs.mkdir();	
					}
					Files.move 
					(Paths.get(newFile.getAbsolutePath()), Paths.get(Constant.ResourcePath.mvExcel+newFile.getName()));

				}
				ze = zis.getNextEntry();
			}
			//close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

}
