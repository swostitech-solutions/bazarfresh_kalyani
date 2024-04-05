package OnlineStore.qa.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Date;
import java.sql.Driver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.http.UrlTemplate.Match;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.bazarfresh.qa.commonutility.WebEventListener;


import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;





public class BaseTest {
	
	public static WebDriver driver;
	public static Properties prop;
	
	public static WebEventListener eventListener;
	public static EventFiringWebDriver e_driver;
	public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<WebDriver>();
	public  int ThreadSleepTime = 2000;	
	public static JavascriptExecutor js;

	public BaseTest() {
		String Path= System.getProperty("user.dir");
		System.out.println(Path);
		
		try {
			prop= new Properties();
			FileInputStream ip = new FileInputStream (Path+"D:\\Automation\\BazarFresh\\OnlineStore\\src\\main\\java\\com\\bazarfresh\\qa\\config");
			prop.load(ip);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
			
			}
			}
	public static void initialization() {
		String path = System.getProperty("user.dir");
		System.out.println(path);
		String downloadpath=path+"\\Downloads";
		String browserName=prop.getProperty("browser");
		System.out.println(browserName);
		if(browserName.equalsIgnoreCase("chrom")) {
		
			ChromeOptions opt = new ChromeOptions ();
			opt.addArguments("disable-extensions");
			opt.addArguments("--start-maximized");
			Map<String,Object>prefs=new HashMap<String, Object>();
			prefs.put("profile.default_content_settings.popups", 0);
			prefs.put("download.default_directory",downloadpath);
			prefs.put("profile.default_content_setting_values.automatic_downloads",1);
			opt.setExperimentalOption("prefs", prefs);
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability(ChromeOptions.CAPABILITY, opt);
			opt.merge(cap);
			//webDriverManager.chromdriver().setup();
			System.setProperty("webdriver.chrome.driver",path+"D:\\Automation\\BazarFresh\\OnlineStore\\driver\\chromedriver.exe");
			driver= new ChromeDriver(opt);
			
			}
		else if (browserName.equalsIgnoreCase("firefox")) {
			//System.setProperty("webdriver.gecko.driver", path+"\\driver\\geckodriver.exe");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		
		else if (browserName.equalsIgnoreCase("edge"))
		{
			//System.setproperty("webdriver.edge.driver",path+"D:\Automation\BazarFresh\OnlineStore\driver\msedgedriver.exe");
		WebDriverManager.edgedriver().setup();
		driver =new EdgeDriver();
		
		}
		e_driver=new EventFiringWebDriver(driver);
		eventListener=new WebEventListener();
		e_driver.register(eventListener);
		driver=e_driver;
		
		
		driver.manage().window().maximize();
		driver.manage().window().maximize();
		driver.get(prop.getProperty("url"));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10000));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		}
	//Generic functions
	public void Sendkeys(WebElement element,String value) {//Function to enter in textbox
    VisibleElement(driver, element, Duration.ofSeconds(20));
	
    try {
    	element.clear();
    	element.sendKeys(value);
    }catch(Exception e) {
    	System.out.println("Exception in Sendkey" + " "+e.getMessage());
    	
    }
	}
 private void EnterKey(WebElement element,Keys key) { //Function to use keyboard keys
	 VisibleElement(driver, element,Duration.ofSeconds(20) );
	 try {
		 element.sendKeys(key);
	 }catch(Exception e) {
		 System.out.println( "Excepetion in EnterKey"+ ""+e.getMessage());
	 }
 }
 public void ClearValues(WebElement element) {
	 VisibleElement(driver,element,Duration.ofSeconds(20));
	 try {
		 element.sendKeys(Keys.CONTROL+"a");
		 element.sendKeys(Keys.DELETE);
	 } catch (Exception e) {
		 System.out.println("Exception in"+""+e.getMessage());
		 
	 }
 }

	public String getCurrentDate() { // Function to get current date
	DateFormat format=new SimpleDateFormat("MM/dd/yyyy");
	Date systemDate=new Date();
	String CurrentDate=format.format(systemDate);
	return CurrentDate;
 }
	

	public String getCurrentDate(String dateFormat) { // Function to get current date
		DateFormat format=new SimpleDateFormat(dateFormat);
		Date systemDate=new Date();
		String CurrentDate=format.format(systemDate);
		return CurrentDate;
	}
	 
		public void selectValue(WebElement element,String values) {// Function to select a value from drop down
        Select dropDown = new Select(element);
        dropDown.deselectByVisibleText(values);
        
 }
		public void Log(String log) {// HTML Log statements
			
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat formatter=new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			String datetime=formatter.format(date);
			System.out.println(datetime+"::"+log);
			Reporter.log(datetime+"::"+log);
			
			
			
		}
		
		//Get currentdate as string in mm//dd/yyyy format
		
		public String currentDate() {
			LocalDate date =LocalDate.now();
			DateTimeFormatter f =DateTimeFormatter.ofPattern("MM/dd/YYY");
			String strDate= f.format(date);
			return strDate;}
		//Generate a random value by passing the range
		public int generateRandomValue(int max, int min) {	
			int randomValue = (int)(Math.random()*(max-min+1)+min); 
			return randomValue;}
		//Call mouse action by passing x and y coordinates 
		public void mouseActions(int x,int y) {
			Actions action =new Actions(driver);
			Action mouseHover = action.moveByOffset(x,y)
					.click().build();
			mouseHover.perform();}
		// Javascript execution by passing element
		public void jScriptExecution(WebElement element) {
			JavascriptExecutor js =(JavascriptExecutor)driver;
			js.executeScript("arguments[0].click(),element");}
		//Javascript to verify CheckBox is selected
		public void jScriptChkBox(WebElement element) {
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("return arguments[0].checked=true;",element);
			System.out.println("checkbox is checked");
		}
		// Page Scroll Down	
		public void pageScrollDown() {
			Actions action = new Actions(driver);
			action.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
		}
		@Step("Taking screenshots on failure")
		public static WebDriver takeScreenshotAtEndOfTest()throws IOException{
			File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String currentDir = System.getProperty("User.dir");
			FileUtils.copyFile(scrFile,new File(currentDir +"/screenshot/"+ System.currentTimeMillis()+".png" ));
		return driver;
		
			}
		@Step("Waiting for element to load..")
		public static void VisibleElement(WebDriver driver,WebElement locater,Duration i) {
			new WebDriverWait(driver,i).ignoring(StaleElementReferenceException.class)
			.until(ExpectedConditions.visibilityOf(locater));
		}
		//Scroll until visible Element
		public void scrollByVisibleElement(WebElement element) {
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].scrollIntoView();", element);
		}
}	

 
 

