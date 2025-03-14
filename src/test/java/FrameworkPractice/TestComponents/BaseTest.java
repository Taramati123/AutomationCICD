package FrameworkPractice.TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import FrameworkPractice.PageObjects.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest 
{
	public WebDriver driver;
	public LandingPage landingpage;
	
	public WebDriver initializeDriver() throws IOException
	{
		
		//properties class;
		Properties prod= new Properties();
		FileInputStream fip= new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\FrameworkPractice\\resources\\GlobalData.properties");
		prod.load(fip);
		
		String browserName=System.getProperty("browser")!=null ? System.getProperty("browser") : prod.getProperty("browser");
		//String browserName=prod.getProperty("browser");
		if(browserName.contains("chrome"))
		{
			ChromeOptions options = new ChromeOptions();
		WebDriverManager.chromedriver().setup();
		if(browserName.contains("headless"))
		{
		options.addArguments("headless");
		}
        driver= new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1440, 900));//full screen for headless option
        
		}
		driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
	}
	@BeforeMethod(alwaysRun = true)
    public LandingPage launchApplication() throws IOException
    {
    	driver=initializeDriver();
    	landingpage = new LandingPage(driver);
    	 landingpage.goTo();
    	return landingpage;
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown()
    {
    	driver.close();
    }
    
    public List<HashMap<String, String>> getJasonDataToMap(String filepath) throws IOException
	{
		//read json to string
		String jasonContent=FileUtils.readFileToString(new File(filepath),
				StandardCharsets.UTF_8);
		
		//String to hashmap
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String,String>> data= mapper.readValue(jasonContent,new TypeReference<List<HashMap<String, String>>>()
		{
		});
		return data;
		}
    
    public String getScreenshot(String testcaseName, WebDriver driver) throws IOException
    {
    	TakesScreenshot ts= (TakesScreenshot)driver;
    	File source=ts.getScreenshotAs(OutputType.FILE);
    	File file= new File(System.getProperty("user.dir")+"//reports//"+testcaseName+".png");
    	FileUtils.copyFile(source, file);
    	return System.getProperty("user.dir") + "//reports//" + testcaseName + ".png";
    	
			
			
    }
	}

