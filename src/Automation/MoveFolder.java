package Automation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;

public class MoveFolder {
	public static List validpol = new ArrayList<>();

	public static void moveFolder(String forTHEfolder, String forTHEfile, Map<String, String> mp, int rowCount, WebDriver driver) {
		try {
			File cpy1 = new File("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\");
			if(!cpy1.exists()){
				cpy1.mkdir();
			}
			
			// get the single file from the folder
			String copy1folderpath = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\"+ forTHEfolder;

			// Check if the File is already there
			File theDir = new File(copy1folderpath);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
//				System.out.println("creating directory: " + theDir.getName());
				boolean result = false;
				try {
					theDir.mkdirs();
					result = true;
				} catch (SecurityException se) {
					se.printStackTrace();
//					System.out.println(se.getMessage());
				}
				if (result) {
//					System.out.println("Folder created");
					// copies file without creating folder
					File sources = new File("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY\\"+ forTHEfolder);
					File dests = new File("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1");
					if(sources.exists()){
						String forTHEfile_replace = forTHEfile.replace(".tif", "_source");
						
						try {
							if(!dests.exists()){
								dests.mkdir();
							}
							// copy the files
							Path temp = Files.move(Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY\\"+forTHEfolder+"\\"+forTHEfile), Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\"+forTHEfolder+"\\"+forTHEfile_replace+".tif"));
							System.err.println("Move folder path " + temp);
							String downloadFilepath = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\TemporaryDownloader\\";
							File theDirs = new File(downloadFilepath);
							if(!theDirs.exists()){
								//theDir.deleteOnExit();
								theDirs.mkdir();
							}

							if(temp != null) { 

								/////////////////////////////////////////////// Application DST.

								int c = validPolicy(mp.get("Policy#"));
								//System.out.println("C: " + c);
								Findthefile.searchElementByPolicyId(mp, c, driver);
								Thread.sleep(3000);
								/////////////////////////////////////////////// Download part
//								System.out.println("**********Download Successful***************");
								// MOVE the .tif file from TemporaryDownloader > COPY1 > Folder 
								String recentDownload = "EFSFile.tif";
								
								String prepareDownload = forTHEfile.substring(0, forTHEfile.lastIndexOf(".")) + "_target";
								String prepareDownload1 = forTHEfile.substring(0, forTHEfile.lastIndexOf(".")) + "_target"+rowCount;
								//////////////////////////////////////////////////
								/*
								 * 
								 * String sourcess = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\"+forTHEfolder+"\\"+recentDownload.replace("EFSFile", prepareDownload1);
								File srcDir1 = new File(sourcess);

								String destination = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\trash";
								File destDir1 = new File(destination);

								try {
									FileUtils.copyFile(srcDir1, destDir1);
								} catch (IOException e) {
								    e.printStackTrace();
								}*/
								
								//////////////////////////////////////////
								
								Path temps = Files.move(Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\TemporaryDownloader\\"+recentDownload), Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\"+forTHEfolder+"\\"+recentDownload.replace("EFSFile", prepareDownload)));
								//Files.copy(Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\TemporaryDownloader\\"+recentDownload), Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\trash"));
								System.out.println("***************************"+ temps);
								/* String sourcess = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\"+forTHEfolder+"\\"+recentDownload.replace("EFSFile", prepareDownload1);
									File srcDir1 = new File(sourcess);

									String destination = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\trash";
									File destDir1 = new File(destination);

									try {
										FileUtils.copyFile(srcDir1, destDir1);
									} catch (IOException e) {
									    e.printStackTrace();
									}*/
								System.out.println("***************************"+ temps);
								Thread.sleep(2000);
								
								// delete TemporaryDownloader Folder
								File tempDownloads = new File("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\TemporaryDownloader\\");
								FileUtils.deleteDirectory(tempDownloads);
//								FileUtils.forceDelete(tempDownloads);

								// Move file copy1 to PROOF : Empties the copy1 folder
//								System.out.println("Row to be executed : " + rowCount);
								Thread.sleep(1000);
								MoveToProof.moveToProof(forTHEfolder, rowCount);
								FileUtils.deleteDirectory(theDirs);
								// Copy To Sayan Automation New*****************
							} else { 
								Findthefile.logError(mp.get("Policy#"), rowCount, "Failed to move the file");
								FileUtils.deleteDirectory(theDirs);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else{
						Findthefile.logError(mp.get("Policy#"), rowCount, "Folder not found on DUMP");
						FileUtils.deleteDirectory(cpy1);
					}

				}
			} else if (theDir.exists()) {
				String forTHEfile_replace = forTHEfile.replace(".tif", "_source");
				
				try{
					Path temp = Files.move(Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY\\"+forTHEfolder+"\\"+forTHEfile), Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\"+forTHEfolder+"\\"+forTHEfile_replace+".tif"));
					//System.out.println("Temp  check: " + temp);
					if(temp != null) { 
//						System.out.println("File renamed and moved successfully"); 

						/////////////////////////////////////////////// Application DST
						int c = validPolicy(mp.get("Policy#"));
						Findthefile.searchElementByPolicyId(mp, c, driver);
						/////////////////////////////////////////////// Download part

						// MOVE the .tif file from TemporaryDownloader > COPY1 > Folder 
						String recentDownload = "EFSFile.tif";
						String prepareDownload = forTHEfile.substring(0, forTHEfile.lastIndexOf(".")) + "_target";
						Files.move(Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\TemporaryDownloader\\"+recentDownload), Paths.get("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1\\"+forTHEfolder+"\\"+recentDownload.replace("EFSFile", prepareDownload)));
						MoveToProof.moveToProof(forTHEfolder, rowCount);
//						System.out.println("Row Count : " + rowCount);
					} 
					else { 
						Findthefile.logError(mp.get("Policy#"), rowCount, "Failed to move the file");
					}
				}catch(Exception e){
					e.printStackTrace();
					Findthefile.logError(mp.get("Policy#"), rowCount, "Folder not found on DUMP");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int validPolicy(String policy){
		validpol.add(policy);
		int count = validpol.size();
		return count;
	}

}
