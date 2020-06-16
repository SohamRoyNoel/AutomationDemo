package Automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import bsh.This;

public class ExtentReportGenerationHelperClass {

	public static Recordset recordset;
	public static Fillo fillo = new Fillo();
	public static File fl = new File(Constant.ResourcePath.ColumnListFromExcelFileReport);

	// Generates extent report
	public static void getExtentReport() throws Exception{

		ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\ExtentReport\\Result.html");

		ExtentReports extentReports = new ExtentReports();
		extentReports.attachReporter(extentHtmlReporter);

		extentReports.setSystemInfo("Host Name", "DataBankToDstTestReport");
		String username = System.getProperty("user.name");
		extentReports.setSystemInfo("User Name", username);
		extentHtmlReporter.config().setDocumentTitle("DST Report"); 
		extentHtmlReporter.config().setReportName("Comparison Status"); 
		// Name of the report
		extentHtmlReporter.config().setReportName("DataBank TO DST Tiff To Tiff Comparison Report"); 


		listtheColumnnames();
		for(int i=1; i <= noOfrows(); i++){
			ExtentTest extentTest = extentReports.createTest("Test Report "+fieldvalues(i).get("keys1"));
			if(fieldvalues(i).get("keys4").equalsIgnoreCase("Pass")){
				extentTest.log(Status.PASS, fieldvalues(i).get("keys5"));
			}
			if(fieldvalues(i).get("keys4").equalsIgnoreCase("Fail")){
				extentTest.log(Status.FAIL, fieldvalues(i).get("keys5"));
			}

			extentReports.flush();
		}
	}

	// Gets the fillo connection
	public static Connection getFilloconnection() throws Exception {
		String username = System.getProperty("user.name");
		Connection connection = fillo.getConnection("\\\\entsserver85\\Cognizant\\DSTScriptRunner\\Reports\\TIFtoTIFCompare\\"+username+"\\Run\\Overall_test_report.xlsx");
		return connection;
	}

	// Counts the number of rows
	public static int noOfrows() throws Exception{
		String query = "select * from Summary";
		Connection connection = getFilloconnection();
		int rowcount = connection.executeQuery(query).getCount();
		return rowcount;
	}

	// lists the column names and writes it it external properties file 
	public static ArrayList<String> listtheColumnnames() throws Exception {
		String query = "select * from Summary";
		Connection connection = getFilloconnection();
		Recordset rs = connection.executeQuery(query);
		ArrayList<String> elements = rs.getFieldNames();
		Properties properties = new Properties();
		for (int i = 0; i < elements.size(); i++) {
			String key = "key" + i;
			properties.setProperty(key, elements.get(i));
		}
		FileOutputStream fileOutputStream = new FileOutputStream(fl);
		properties.store(fileOutputStream, "Columns from the Report Generation excel sheet");
		fileOutputStream.close();
		return elements;
	}

	// Counts the column
	public static int noOfcolumns() throws Exception {
		String query = "select * from Summary";
		Connection connection = getFilloconnection();
		Recordset rs = connection.executeQuery(query);
		int count = rs.getFieldNames().size();
		return count;
	}

	// Read The property file
	public static String properties(String key) throws Exception {
		FileInputStream file = new FileInputStream(fl);
		Properties rpop = new Properties();
		rpop.load(file);
		String data = rpop.getProperty(key);
		return data;
	}

	// get row data as per the row
	public static Map<String, String> fieldvalues(int rowcount) throws Exception {
		String query = "select * from Summary";
		Connection connection = getFilloconnection();
		Recordset rs = connection.executeQuery(query);
		Map<String, String> rowmap = new HashMap<String, String>();
		int counter = 1;
		// As the number of the properties will always be same as the no of columns
		int noofproperties = noOfcolumns();
//		System.out.println("no of props : " + noofproperties);
		while (rs.next()) {
			if (counter == rowcount) {
				for (int i = 0; i < noofproperties; i++) {
					// prepare the key
					String prepareKey = "keys"+i;
					rowmap.put(prepareKey, rs.getField(properties("key"+i)));
				}
			}
			counter++;
		}
		return rowmap;
	}
}
