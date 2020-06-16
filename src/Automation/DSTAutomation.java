package Automation;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.ibm.icu.impl.duration.TimeUnit;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.utils.FileUtil;
import com.codoid.products.fillo.Connection;

public class DSTAutomation  extends LoginEFS  {

	public static Recordset recordset;
	public static Fillo fillo = new Fillo();
	public static File fl = new File(Constant.ResourcePath.ColumnListFromExcelFile);
	public ExtentReportGenerationHelperClass extentReportGenerationHelperClass = new ExtentReportGenerationHelperClass();
	static int LoopCounter = 0;
	public static int ExecutedCounter = 1;


	// Duplicate policy
	public static final Set<String> set = new HashSet<String>();

	public static void main(String[] args) throws Exception {
		Instant start =Instant.now();
		// Creating Proof
		File pf1 = new File("\\\\entsserver85\\cognizant\\DSTScriptRunner");
		if(!pf1.exists()){
			pf1.mkdir();
		}

		File pf = new File("\\\\entsserver85\\cognizant\\DSTScriptRunner\\PROOF");
		if(!pf.exists()){
			pf.mkdir();
		}

		File executed1 = new File("\\\\entsserver85\\cognizant\\DSTScriptRunner\\executed");
		if(!executed1.exists()){
			executed1.mkdir();
		}

//		File executed = new File("\\\\entsserver85\\cognizant\\DSTScriptRunner\\executed\\PROOF");


		// UNzip the file
		Unziper.unZiper();

		// LogIN
//		LoginEFS.loginProcess(args[0]);  //Test
//		LoginEFS.loginProcess("Test");
		listtheColumnnames();

		// Get the fields details in the console
		int noofrows = noOfrows();
//		System.out.println("No of rows : " + noofrows);
//		System.out.println("No of columns " + noOfcolumns());
		// Tresses the EXECUTED folder number
		int verifier = 1;
		int noOfFolders = 0;
		for (int i = 1; i <= noOfrows(); i++) {
			WebDriver driver = LoginEFS.loginProcess("Test");
//			if (!executed.exists()) {
//				executed.mkdir();
//			}
			
			String downloadFilepath = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\TemporaryDownloader\\";
			File theDirs = new File(downloadFilepath);
			if(!theDirs.exists()){
				//theDir.deleteOnExit();
				theDirs.mkdir();
			}

			
			String policy = fieldvalues(i).get("Policy#");
			// validated the duplicate policy field : add to set and get true and false in return
//			System.out.println("for loop");
			boolean det = set.add(policy);
			if (det) {
				String path = fieldvalues(i).get("Path");
				try{
					int plcc = Integer.parseInt(fieldvalues(i).get("PLCC"));

					if (path != null) {
						// Extracts Folder name
						String forTHEfolder = path.substring(0, path.lastIndexOf("\\"));

						// Extracts FILE name
						String forTHEfile = path.substring(path.lastIndexOf("\\")+1, path.length());

						MoveFolder.moveFolder(forTHEfolder, forTHEfile, fieldvalues(i), i, driver);

//						System.out.println("Status Checked");
					}
				} catch(NumberFormatException nf){
//					break;
					nf.printStackTrace();
				}
			}
//			System.out.println("Has more then 5 files");
			try{
				// Count no of folders
				noOfFolders = new File("\\\\entsserver85\\cognizant\\DSTScriptRunner\\PROOF\\").list().length;
				File repFile = new File("\\\\entsserver85\\cognizant\\DSTScriptRunner\\Reports\\");
				if (!repFile.exists()) {
					repFile.mkdir();
				}

				// Execute when folder count is 2
				if(noOfFolders == 1){
//					System.out.println("Gone for execution");
					ListFilesUtil.Converter("\\\\entsserver85\\cognizant\\DSTScriptRunner\\PROOF\\", "\\\\entsserver85\\cognizant\\DSTScriptRunner\\Reports\\");


					FileUtils.deleteDirectory(new File("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1"));
					// Creates a folder for each round of execution and moves the sub folders 
					String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
					File executedRounder = new File("\\\\entsserver85\\cognizant\\DSTScriptRunner\\executed\\executed_Round_"+timeStamp);
					if(!executedRounder.exists()){
						executedRounder.mkdir();
						ExecutedCounter++;
					}
//					System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
					Thread.sleep(100);
//					FileUtils.copyDirectoryToDirectory(pf, executedRounder);
					
					Files.move(new File("\\\\entsserver85\\cognizant\\DSTScriptRunner\\PROOF\\").toPath(), executedRounder.toPath(), StandardCopyOption.REPLACE_EXISTING);
//					FileUtils.moveDirectory(pf, executedRounder);
					Thread.sleep(2000);
//					System.out.println("File has been moved to EXECUTED");
					FileUtils.deleteDirectory(new File("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\PROOF"));
					System.out.println("File has been deleted");
					System.out.println("Executed : to be moved and COPY1, PROOF folder is deleted");
				}
			} catch(Exception e){
				e.printStackTrace();
			}
//			System.out.println("Came to end");
			LoopCounter++;
			verifier++;
			
			FileUtils.deleteDirectory(new File("\\\\entsserver85\\cognizant\\DSTScriptRunner\\PROOF\\"));
			FileUtils.deleteDirectory(new File("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\COPY1"));
			System.out.println("Round " + i + " Completed");
			
			driver.close();

		}
//		System.out.println("Execution ended :::::::::::::::::::: " + LoopCounter);

//		System.out.println("Extent Report");
		//TimeUnit.SECOND.wait(20);
//		driver.close();
		ExtentReportGenerationHelperClass.getExtentReport();
		
		File file = new File("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\ExtentReport\\Result.html");
		Desktop.getDesktop().browse(file.toURI());
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
//		System.out.println("Time taken: "+ timeElapsed.toMinutes() +" Minutes");
		JOptionPane.showMessageDialog(null, "Total Execution Time : "+ timeElapsed.toMinutes() +" Minutes", "Action Time", JOptionPane.INFORMATION_MESSAGE);
	}

	// deleting file
	public static void deleteDirectoryRecursion(Path path) throws IOException {
		if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
				for (Path entry : entries) {
					deleteDirectoryRecursion(entry);
				}
			}
		}
		Files.delete(path);
	}

	// checking for DUPLICATE : of a particular folder
	public static boolean validator(String policy) {
		try {
//			System.out.println("Set val :" + set.add(policy));
			return true;
		} catch (Exception e) {
//			System.out.println("Duplicate : " + policy);
			return false;
		}
	}

	// get row data as per the row
	public static Map<String, String> fieldvalues(int rowcount) throws Exception {
		String query = "select * from " + propertiesExternal("excelSheetname") + " where Status='TRUE' and PLCC < 10";
		Connection connection = getFilloconnection();
		Recordset rs = connection.executeQuery(query);
		List<String> elements = rs.getFieldNames();
		Map<String, String> rowmap = new HashMap<String, String>();
		int counter = 1;
		// As the number of the properties will always be same as the no of columns
		int noofproperties = noOfcolumns();
		while (rs.next()) {
			if (counter == rowcount) {
				for (int i = 0; i < noofproperties; i++) {
					// prepare the key to pull value from ColumnListfromexcel.properties file
					String prepareKey = properties(elements.get(i));
					rowmap.put(prepareKey, rs.getField(properties(prepareKey)));
				}
			}
			counter++;
		}
		connection.close();
		return rowmap;
	}

	// Read The property file
	public static String properties(String key) {
		try{
			FileInputStream file = new FileInputStream(fl);
			Properties rpop = new Properties();
			rpop.load(file);
			String data = rpop.getProperty(key);
			return data;
		} catch(Exception e){
			return null;
		}
	}

	// Read The property file FROM EXTERNAL USER
	public static String propertiesExternal(String key) {
		try{
			//File files = new File("\\\\ENTSSERVER85\\Cognizant\\Automation\\AutomationGeek\\DataBankToDST\\DATA_BANK_TO_DST\\DST_EFS_Automation_Script\\Config\\config.properties");
			File files = new File(Constant.ResourcePath.Configfile);
			FileInputStream file = new FileInputStream(files);
			Properties rpop = new Properties();
			rpop.load(file);
			String data = rpop.getProperty(key);
			return data;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	// Counts the column
	public static int noOfcolumns() {
		try{
			String query ="select * from " + propertiesExternal("excelSheetname") + " where Status='TRUE' and PLCC < 10";
			Connection connection = getFilloconnection();
			Recordset rs = connection.executeQuery(query);
			int count = rs.getFieldNames().size();
			return count;
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	// lists the column names and writes it it external properties file 
	public static ArrayList<String> listtheColumnnames() {
		ArrayList<String> elements = null;
		try{
			String query = "select * from " + propertiesExternal("excelSheetname") + " where Status='TRUE' and PLCC < 10";
			Connection connection = getFilloconnection();
			Recordset rs = connection.executeQuery(query);
			elements = rs.getFieldNames();
			Properties properties = new Properties();
			for (int i = 0; i < elements.size(); i++) {
				String key = elements.get(i);
				properties.setProperty(key, elements.get(i));
			}
			FileOutputStream fileOutputStream = new FileOutputStream(fl);
			properties.store(fileOutputStream, "Columns from the excel sheet");
			fileOutputStream.close();
			return elements;
		} catch(Exception e){
			return elements;
		}
	}

	// Counts the number of rows
	public static int noOfrows() {
		try{
			String query ="select * from " + propertiesExternal("excelSheetname") + " where Status='TRUE' and PLCC < 10";
			Connection connection = getFilloconnection();
			int rowcount = connection.executeQuery(query).getCount();
			System.out.println("Number of rows : " + rowcount);
			return rowcount;
		} catch(Exception e){
			return 0;
		}
	}

	// Gets the fillo connection : when the file will be moved from main dump and will be stored in specific folder, so it has 1 file at any given time
	public static Connection getFilloconnection() {
		try {
			File folder = new File("\\\\ENTSSERVER85\\Cognizant\\Automation\\AutomationGeek\\DataBankToDST\\DATA_BANK_TO_DST\\DST_EFS_Automation_Script\\Put_The_Excel_File\\");
//			System.out.println("Connected Excel : " + folder.getAbsolutePath());
			File[] listOfFiles = folder.listFiles();
			// System.out.println("File " + listOfFiles[0].getName());
			Connection connection = fillo.getConnection("\\\\ENTSSERVER85\\Cognizant\\Automation\\AutomationGeek\\DataBankToDST\\DATA_BANK_TO_DST\\DST_EFS_Automation_Script\\Put_The_Excel_File\\"+listOfFiles[0].getName());
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

}
