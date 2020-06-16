package Automation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Findthefile {
	public static File fl = new File("\\\\ENTSSERVER85\\Cognizant\\Automation\\AutomationGeek\\DataBankToDST\\DATA_BANK_TO_DST\\DST_EFS_Automation_Script\\Config\\Errorlog.properties");
	static XconfigProperties xc = new XconfigProperties();
//	public static Actions actions = new Actions(driver);

//	public WebDriver getWebdriver() {
//		try {
//			System.setProperty(DSTAutomation.propertiesExternal("basedriver"),DSTAutomation.propertiesExternal("driverpath"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		WebDriver driver = new ChromeDriver();
//		return driver;
//	}

	public static void searchElementByPolicyId (Map<String, String> mp, int rowCount, WebDriver driver) throws InterruptedException {
		Actions actions = new Actions(driver);
		WebDriverWait wait=new WebDriverWait(driver, 40);

		try {
			String baseUrl = xc.properties("getbaseurl").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			int plcc =Integer.parseInt(mp.get("PLCC"));
			String policy = mp.get("Policy#");
			String dosc = mp.get("DOSC"); // security level
			String doc = mp.get("DocType");
			String policynew = "";
			String dbtn = mp.get("DBTN"); // BCVal
			//System.out.println("entered : " + mp);
			// checking if executable if and only if the plcc value is less than 10
			if (plcc < 10) {		
				if (policy.length() <= 9) {
					int gap = (9-policy.length());
					if (policy.length() == 4) { policynew = "00000" + policy; }
					if (policy.length() == 5) { policynew = "0000" + policy; }
					if (policy.length() == 6) { policynew = "000" + policy; }
					if (policy.length() == 7) { policynew = "00" + policy; }
					if (policy.length() == 8) { policynew = "0" + policy; }
					if (policy.length() == 9) { policynew = policy; }
				}
				System.err.println("Policy No : " + policynew);
				String policy_plcc = policynew + " "+ plcc + " ";
				// to open the foldable respective to the row number

				if (rowCount == 1) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xc.properties("xpathoffolder")))).click();
//					System.out.println("when row no is 1");
				}
				if(rowCount > 1){
//					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xc.properties("xpathoffolder1")))).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xc.properties("xpathoffolder")))).click();
//					System.out.println("when row no is 2");
				}

				// xpath of the search field

				Thread.sleep(1000);
				String xxpath = xc.properties("searchfield");
//				System.out.println("policy to be entered");
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xc.properties("searchfield")))).clear();

				Thread.sleep(5000);
				// passing the value to the search field
				WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xc.properties("searchfield"))));
				actions.moveToElement(el).click().build().perform();

				Thread.sleep(1000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xc.properties("searchfield")))).sendKeys(policynew);
//				System.out.println("policy to be entered 24A **" +policynew + " map val : " + policy);
				
				Thread.sleep(1000);
				// searching the respective policy : ButtonClick
				String btn_xpath =xc.properties("getbuttonXpath");
				WebElement bel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xc.properties("getbuttonXpath"))));
				actions.moveToElement(bel).click().build().perform();

				Thread.sleep(1000);
				// Finding the numbers of existing folder in the DST application
				List elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xc.properties("chooseListelement"))));
				int elcount = elements.size();
				
				// validate : mismatch between excel policy and policy not found
				boolean determiner = true;

				if (determiner == true) {
					// List to get the happy path
					List elm = new ArrayList();
					// list to get the elements from the folded folders
					int n = 1; 				
					// Adding the elements in a list from the DST application
					while (elcount >= n) {
						try{
							String st = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xc.properties("getThetextvalue")+n+xc.properties("getThetextvalue1")))).getText();
							elm.add(st);
						}catch(StaleElementReferenceException st){ st.printStackTrace(); }
						n++;
					} 

					// In case of multiple folder
					// determine the exact policy number, if that is more than 1, that indicates multiple file issue
					int c = 0;
					for (int i = 0; i < elm.size(); i++) {
						String token = elm.get(i).toString().trim();
						String policycont = policynew+" "+plcc;
						String str = token.substring(0, policynew.length());
						if (str.equals(policynew)) {
							c = c+1;
						}
					}		
					// execution of multiple folder value
					if (c > 1) {
						for (int x = 0; x < elm.size(); x++) {

							if (elm.get(x).toString().equals(policy_plcc.trim())) {
								// taking the y variable to prevent the list out of bound exception
								int y = x+1;
								// Code to click the + button
								driver.findElement(By.xpath(xc.properties("getexpandButton")+ y + xc.properties("getexpandButton1"))).click();
								Thread.sleep(1000);
								// choosing the file from the clicked folder
								String testnm = "";
								do{
									
									// listing all of them
									testnm = driver.findElement(By.xpath(xc.properties("getFilespath")+y+xc.properties("getFilespath1"))).getText().trim();
//									System.out.println("*****Test NAme : " + testnm);
//									System.out.println("*****Doc : " + doc);
									// Click the icon
									if (token(testnm).contains(doc)) {
										// Call the file to unfold
										unfoldthefile(y, dbtn, dosc, policy, rowCount, driver, actions);
										
										break;
									}
									y++;
								}while(testnm != null);
							}
						}
					}

					// following happy path
					for (int i = 0; i <  elm.size(); i++) { 
						Thread.sleep(2000);
						String holder = token(elm.get(i).toString());
						System.err.println("Holder name ; " + holder);
						if (holder.contains(doc)) {
							int count = i+1; 
							// Call the file to unfold
							System.err.println("Following happy path");
							unfoldthefile(count, dbtn, dosc, policy, rowCount, driver, actions);
							break;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			Findthefile.logError(mp.get("Policy#"), rowCount, "PLCC Value is greater then 10");
//			e.printStackTrace();
		}
//		driver.navigate().refresh();
		Thread.sleep(5000);
	}

	// extract string from a particular position : used for file name comparison between DST app and excel file
	public static String token(String st) {
		st=st.substring(st.lastIndexOf(" ")+1,st.length());
		String abc=st;
		return abc;
	}

	// Unfolds the folder and clicks on the file
	public static void unfoldthefile(int file_no, String BCVAL, String seclvl, String policy, int rowcount, WebDriver driver, Actions actions) {	
		try{
			Thread.sleep(1000);
			// Unfolds the folder
			WebElement button = driver.findElement(By.xpath(xc.properties("getexpandButton")+ file_no + xc.properties("getexpandButton1")));
//			System.out.println("Left Click");
			actions.moveToElement(button).click().build().perform();

			Thread.sleep(1000);
			// clicks the file
			WebElement but_file = driver.findElement(By.xpath(xc.properties("getThefileclicked")+(file_no+1)+xc.properties("getThefileclicked1")));
//			System.out.println("Right Click");
			actions.moveToElement(but_file).click().build().perform();

			// validating BCVal and Security level
			int flag = validateFields(BCVAL, seclvl, policy, rowcount, driver);
			String reason = "BC Val not matched";
			if (flag == 1) { logError(policy, rowcount, reason); }
			if (flag == 0) {
				
				String downloadFilepath = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\TemporaryDownloader\\";
				File fs = new File(downloadFilepath);
				if(!fs.exists()){
					fs.mkdir();
				}
				
				Thread.sleep(1000);
				WebElement but_files = driver.findElement(By.xpath(xc.properties("getThefileclicked")+(file_no+1)+xc.properties("getThefileclicked1")));
				actions.contextClick(but_files).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.RETURN).build().perform();
				System.err.println("Downloaded File");
				//driver.navigate().refresh();
//				driver.quit();
//				driver.close();
				
				/*driver.get("chrome://settings/clearBrowserData");
		        Thread.sleep(5000);
		        driver.switchTo().activeElement();
		        driver.findElement(By.cssSelector("* /deep/ #clearBrowsingDataConfirm")).click();
		        Thread.sleep(20000);
		        driver.navigate().back();*/
			}
			flag = 0;
		} catch(Exception e){
			e.printStackTrace();
			logError(policy, rowcount, "File not found in Application");
		}
	}

	// return the value of disabled field : logs value in properties file
	public static int validateFields(String bcval, String secno, String policy, int rowcount, WebDriver driver) {
		try{
			Thread.sleep(2000); 
			//System.out.println("Validation method");	
			String receivedbcval = driver.findElement(By.name("BCDV")).getAttribute("value").trim();
			String receivedsecno = driver.findElement(By.name("SECLEVEL")).getAttribute("value").trim();
			if (receivedbcval.trim().contains(bcval.trim()) && receivedsecno.trim().contains(secno.trim())) { return 0; } else{ return 1; }
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	// Logs the mismatched files into the dot txt file 
	public static void logError(String filename, int rowcount, String reason) {

		try{
			File dir = new File("\\\\ENTSSERVER85\\Cognizant\\Automation\\AutomationGeek\\DataBankToDST\\DATA_BANK_TO_DST\\DST_EFS_Automation_Script\\Config\\Errorlog.txt");
			BufferedWriter bufferedWriter = null;
			String strContent = "Policy id : "+filename+ " located at : "+(rowcount+1)+" "+reason+"\n";
			if (!dir.exists()) {
				dir.createNewFile();
				try{
					Writer writer = new FileWriter(dir);
					bufferedWriter = new BufferedWriter(writer);
					bufferedWriter.write(strContent);
				}catch(Exception e){
					e.printStackTrace();
				}finally {
					bufferedWriter.close();
				}
			}else {
				try{
					FileWriter writer = new FileWriter(dir.getAbsoluteFile(), true);
					bufferedWriter = new BufferedWriter(writer);
					bufferedWriter.write("");
					bufferedWriter.write(strContent);
//					System.out.println("Error Logged in existing file");
				}catch(Exception e){
					e.printStackTrace();
				}finally {
					bufferedWriter.close();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
