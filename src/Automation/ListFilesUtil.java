package Automation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ListFilesUtil {
	/**
	 * List all the files and folders from a directory
	 * @param directoryName to be listed
	 */
	static ReportGenerationHelperClass reportGenerationHelperObj = new ReportGenerationHelperClass();
	String Target_file_name="";
	String Target_Directory_name="";
	String Target_path="";
	String Target_Output_path="";
	String Source_file_name="";
	String Source_Directory_name="";
	String Source_path="";
	String Result_path="";
	String Source_Output_path="";  
	static String arr[]=new String[100];
	static String filename[]=new String[100];
	static JFrame frame;
	static int Counter = 1; // Counts the ROUND of execution
		
	public void listFilesAndFolders(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			// System.out.println(file.getName());
		}
	}
	/**
	 * List all the files under a directory
	 * @param directoryName to be listed
	 */
	public void listFiles(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			if (file.isFile()){
				//  System.out.println(file.getName());
			}
		}
	}
	/**
	 * List all the folder under a directory
	 * @param directoryName to be listed
	 */
	public void listFolders(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			if (file.isDirectory()){
				//  System.out.println(file.getName());
			}
		}
	}

	public static  void CreateUserDirectory(String usrDir) {

		File theDir = new File(usrDir);
		if (!theDir.exists()) {
			try {
				theDir.mkdir();
				// System.out.println("User Directoty: "+usrDir+" Sucessfully Created ");

			} catch (SecurityException se) {
				//handle it
				//System.out.println("User Directoty: "+usrDir+" Failed to be Created "+se);
				throw se;

			}
		}
	}
	/**
	 * List all files from a directory and its subdirectories
	 * @param directoryName to be listed
	 */
	int i=0;
	public void listFilesAndFilesSubDirectories(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){

			if (file.isFile() && !(file.getAbsolutePath().endsWith(".db")) && !(file.getAbsolutePath().endsWith(".doc"))){
				///target
				if(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length()).toUpperCase().contains("TARGET")){
					Target_file_name=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length());
					Target_Directory_name=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length()).substring(0,file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length()).lastIndexOf("_"));
					Target_path=file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"))+"\\"+"Target";
					Result_path=file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"))+"\\"+"Result";
					Target_Output_path=file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"))+"\\"+"Target"+"\\"+Target_file_name.substring(0, Target_file_name.lastIndexOf("."))+".pdf";
					CreateUserDirectory(Target_path);
					CreateUserDirectory(Result_path);
					
//					System.out.println("TARGET TRIGGERED");
					
					TIFF_To_PDF.tiffTOPDFConvert(file.getAbsolutePath(),Target_Output_path);
				}
				///source
				if(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length()).toUpperCase().contains("SOURCE")){
					Source_file_name=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length());
					Source_Directory_name=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length()).substring(0,file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length()).lastIndexOf("_"));
					Source_path=file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"))+"\\"+"Source";
					Result_path=file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"))+"\\"+"Result";
					Source_Output_path=file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"))+"\\"+"Source"+"\\"+Source_file_name.substring(0, Source_file_name.lastIndexOf("."))+".pdf";
					CreateUserDirectory(Source_path);
					CreateUserDirectory(Result_path);
					
//					System.out.println("TARGET TRIGGERED");
					
					TIFF_To_PDF.tiffTOPDFConvert(file.getAbsolutePath(),Source_Output_path);
				}


				arr[i]=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length()).substring(0,file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1, file.getAbsolutePath().length()).lastIndexOf("_"));

				i=i+1;
			} else if (file.isDirectory()){
				listFilesAndFilesSubDirectories(file.getAbsolutePath());
			}
		}
	}
	static int m=0;
	public void listFilesAndFilesSubDirectories3(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){

			if (file.isFile() && !(file.getAbsolutePath().endsWith(".db")) && !(file.getAbsolutePath().endsWith(".doc")) && !(file.getAbsolutePath().toUpperCase().endsWith(".TIF"))){

				filename[m]=file.getAbsolutePath();
				m=m+1;
			} else if (file.isDirectory()){
				listFilesAndFilesSubDirectories3(file.getAbsolutePath());
			}
		}
	}

	static int n=0;
	public void listFilesAndFilesSubDirectories4(String directoryName){
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){

			if (file.isFile() && !(file.getAbsolutePath().endsWith(".db")) && !(file.getAbsolutePath().endsWith(".doc")) && !(file.getAbsolutePath().toUpperCase().endsWith(".TIF"))){


				n=n+1;
			} else if (file.isDirectory()){
				listFilesAndFilesSubDirectories4(file.getAbsolutePath());
			}
		}
	}


	public static void Converter(String args,String args1) throws IOException, InterruptedException {

		ListFilesUtil listFilesUtil = new ListFilesUtil();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String test=args;
		System.out.println(test);
		// final String directoryWindows ="\\\\entsserver85\\USISQAQC\\Group\\QA Testing\\Cognizant\\JH DST to Databank\\Test";
		final String directoryWindows =test;
		// CreateUserDirectory(directoryWindows+"\\"+args1);
		String outputDirLocation=args1+"\\";
		String temp_file1=args1+"\\Temp1.txt";
		File f1 = new File(temp_file1);
		boolean determine = false;

//		System.out.println("LIST FILE PUSHED");
		
		listFilesUtil.listFilesAndFilesSubDirectories(directoryWindows);

		listFilesUtil.listFilesAndFilesSubDirectories4(directoryWindows);

		String srcfile="";
		String trgfile="";
		String output="";
		String path="";
		String temp_name="";
		String folder_name="";

//		System.out.println("Source Target started");
		
		filename=new String [n];
		listFilesUtil.listFilesAndFilesSubDirectories3(directoryWindows);
//		System.out.println("File name :::: " + filename.length);

		for(int j=0;j<filename.length;){

			if(filename[j].toUpperCase().toString().contains("SOURCE")){
				srcfile= filename[j].trim();
			}else if (filename[j+1].toUpperCase().toString().contains("SOURCE"))
			{
				srcfile= filename[j+1].trim();
			}
			if (filename[j].toUpperCase().toString().contains("TARGET"))
			{
				trgfile=filename[j].trim(); 
			}else if (filename[j+1].toUpperCase().toString().contains("TARGET"))
			{
				trgfile=filename[j+1].trim(); 
			}
			path=srcfile.substring(0, srcfile.lastIndexOf("\\"));
			temp_name=path.substring(0, path.lastIndexOf("\\"));
			folder_name=temp_name.substring(temp_name.lastIndexOf("\\")+1, temp_name.length());
			output=path.substring(0, path.lastIndexOf("\\"))+"\\Result";

//			System.out.println("Compare PDF*************");
			PdfTPdf.compare(srcfile, trgfile,output,temp_file1,folder_name);
			j=j+2;

			// It took 7 fu*king hours of my life to find out these kids.
			n=0;
			m=0;
		}


		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet XSSFSheetSummary = reportGenerationHelperObj.createReportSheet("Summary", workbook);
		createSummaryReport(XSSFSheetSummary, workbook);

		BufferedReader bufferedReader = new BufferedReader(new FileReader(temp_file1));
		String line = null;
		int line_count=1;
		String[] temp;
		//delimiter 
		String delimiter = ",";
		String str = " ";
		String str1 = " ";
		String str2 = " ";
		String str3 = " ";
		String str4 = " ";
		String str5 = " "; // Recheck Status Unique ID
		
		if (str3=="FAIL") {
			determine = true;
		}
		
		while ((line = bufferedReader.readLine()) != null) {
			// given string will be split by the argument delimiter provided. 
			temp = line.split(delimiter);
			//print substrings 
			for(int i =0; i < temp.length ; i++){
				str = temp[0];
				str1 = temp[1];
				str2 = temp[2];
				str3 = temp[3];
				str4 = temp[4];
				str5 = str1.substring(str1.lastIndexOf("\\")+1, str1.lastIndexOf("_"));
			}
			
			writeDataIntoCellsMismatch(line_count, XSSFSheetSummary, workbook, str, str1, str2, str3, str4, str5);
			line_count++;
		}
		bufferedReader.close();
			
		String path1= reportGenerationHelperObj.createReportDirectoryAndGetPath("\\\\entsserver85\\cognizant\\DSTScriptRunner\\Reports\\");
//		String path1= reportGenerationHelperObj.createReportDirectoryAndGetPath(outputDirLocation);

		// write to excel file
		reportGenerationHelperObj.writeWorkbook(path1 + "\\Overall_test_report" + ".xlsx", workbook);
		File file = new File(temp_file1);

//		System.out.println("Source Target COMPLETED");

		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		///
		workbook.close();
		///
		if (file.exists() && file.isFile())
		{
			//file.delete();
			n =0;
//			System.out.println("Done");
			Counter++;
		}
		
	}
	private static void createSummaryReport(XSSFSheet XSSFSheetMismatch, XSSFWorkbook workbook) {

		String[] columnNames = {"Folder Name", "Source File Name", "Target File Name","Status","Comment","UniqueID"};
		reportGenerationHelperObj.createRowInExcel(0, columnNames, XSSFSheetMismatch, workbook, reportGenerationHelperObj.createCellStyle(workbook));

	}
	static int rownum = 1;

	private static void writeDataIntoCellsMismatch(int row, XSSFSheet XSSFSheet, XSSFWorkbook workbook, String Folder_Name, String Source_File_Name, String Target_File_Name, String Status, String Comment, String UniqueID) {

		String[] rowData = {Folder_Name, Source_File_Name, Target_File_Name,Status,Comment,UniqueID};
		reportGenerationHelperObj.createRowInExcel(row, rowData, XSSFSheet, workbook, reportGenerationHelperObj.createCellStyle1(workbook));
	}
}
