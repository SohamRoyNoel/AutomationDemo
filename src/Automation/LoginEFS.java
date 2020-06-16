package Automation;

import java.io.File;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class LoginEFS {

	static WebDriver driver = null;
	public static WebDriver loginProcess(String Env) {
		try{
			String downloadFilepath = "\\\\entsserver85\\Cognizant\\DSTScriptRunner\\TemporaryDownloader\\";
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", downloadFilepath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("disable-infobars");
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);

			XconfigProperties xc = new XconfigProperties();
//			System.setProperty(DSTAutomation.propertiesExternal("basedriver"),DSTAutomation.propertiesExternal("driverpath"));
			System.setProperty("webdriver.chrome.driver","\\\\ENTSSERVER85\\Cognizant\\Automation\\AutomationGeek\\DataBankToDST\\DATA_BANK_TO_DST\\DST_EFS_Automation_Script\\Chromedriver\\chromedriver.exe");


			driver = new ChromeDriver(cap);
			/*driver.get("chrome://settings/clearBrowserData");
	        Thread.sleep(5000);*/
			if(Env.equalsIgnoreCase("Test"))
			{
				//driver.get(DSTAutomation.propertiesExternal("getloginurl"));
				driver.get("http://efsjhann-np.dstcorp.net/testefsapp/efss/displayLogon?cz=702110804131905&redirectLogon=true");
				driver.manage().window().maximize();
//				driver.findElement(By.xpath(xc.properties("getusername"))).sendKeys(DSTAutomation.propertiesExternal("username"));
//				driver.findElement(By.xpath(xc.properties("getpassword"))).sendKeys(DSTAutomation.propertiesExternal("password"));
				driver.findElement(By.xpath(xc.properties("getusername"))).sendKeys("BSOURAV");
				driver.findElement(By.xpath(xc.properties("getpassword"))).sendKeys("June@2020");
				driver.findElement(By.xpath(xc.properties("getbutton"))).click();
			}else
			{

				driver.get(DSTAutomation.propertiesExternal("getloginurlprod"));
				driver.manage().window().maximize();
				driver.findElement(By.xpath(xc.properties("getusername"))).sendKeys(DSTAutomation.propertiesExternal("Pusername"));
				driver.findElement(By.xpath(xc.properties("getpassword"))).sendKeys(DSTAutomation.propertiesExternal("Ppassword"));
				driver.findElement(By.xpath(xc.properties("getbutton"))).click();
			}
//			driver.manage().window().maximize();
//			driver.findElement(By.xpath(xc.properties("getusername"))).sendKeys(DSTAutomation.propertiesExternal("username"));
//			driver.findElement(By.xpath(xc.properties("getpassword"))).sendKeys(DSTAutomation.propertiesExternal("password"));
//			driver.findElement(By.xpath(xc.properties("getbutton"))).click();
			

		} catch (Exception e){
			e.printStackTrace();
		}
		return driver;
	}

}
