package com.swagLabs;

import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.idealized.OpaqueKey;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.ous.jtoml.impl.SymbolToken;
import utility.Login;

public class swagLabs {
	public static String baseURL = "https://www.saucedemo.com/";
	public static WebDriver driver;
	public static Login login;
//	
	@BeforeTest
	public static void setupDriver() {
//		ChromeOptions co = new ChromeOptions();
//		co.addArguments("--remote-allow-origins=*"); 
		System.setProperty("webdriver.http.factory", "jdk-http-client");
		WebDriverManager.edgedriver().setup();
		driver=new EdgeDriver();
		login=new Login(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
	@BeforeMethod
	public void getToSwagLabs() {
		
		driver.get(baseURL);
	}
	@DataProvider(name = "credentials")
	public Object[][] LoginData() {
		
		return new Object[][] {
				{"valid","standard_user","secret_sauce"},
				{"invalid","something","standard_user"},
				{"invalidu","","hello"},
				{"valid","performance_glitch_user","secret_sauce"},
				{"invalidp","something",""},
				{"invalidu","",""}
				
		};
		
	}
	@Test(dataProvider = "credentials",priority = 0)
	public void testingLogin(String scenario,String username,String password) {
		if(scenario.equals("valid")) {
		login.LoginToSwagLab(username, password);
		String logo = driver.findElement(By.xpath("//div[@class='app_logo']")).getText();
		Assert.assertEquals(logo, "Swag Labs");
		}else if(scenario.equals("invalidu")){
			login.LoginToSwagLab(username, password);
			Boolean error = driver.findElement(By.xpath("//h3[@data-test='error']")).getText().contains("Username is required");
			Assert.assertTrue(error);
		}else if(scenario.equals("invalidp")){
			login.LoginToSwagLab(username, password);
			Boolean error = driver.findElement(By.xpath("//h3[@data-test='error']")).getText().contains("Password is required");
			Assert.assertTrue(error);
		}else {
			login.LoginToSwagLab(username, password);
			Boolean error = driver.findElement(By.xpath("//h3[@data-test='error']")).getText().contains("Username and password do not match any user in this service");
			Assert.assertTrue(error);
		}
	}
	public static void LoginWithValidData() {
		
		login.LoginToSwagLab("standard_user", "secret_sauce");
	}
	@Test(priority = 1)
	public void verifyLogoOnHomepage() {
		LoginWithValidData();
		String logo = driver.findElement(By.xpath("//div[@class='app_logo']")).getText();
		Assert.assertEquals(logo, "Swag Labs");
	}
	@Test(priority = 2)
	public void verifyHamburgerMenu() {
		LoginWithValidData();
		WebElement menu = driver.findElement(By.xpath("//button[@id='react-burger-menu-btn']"));
//		Assert.assertTrue(menu.isDisplayed());
		menu.click();
//		Assert.assertTrue(driver.findElement(By.className("bm-item-list")).isDisplayed());
		
		List<WebElement> elements = driver.findElements(By.xpath("//a[@class='bm-item menu-item']"));
		for(int i=0;i<elements.size();i++) {
			System.out.println(elements.get(i).getText());
			if(i==2) {
				Assert.assertEquals(elements.get(i).getText(), "Logout");
			}else if(i==3) {
				Assert.assertEquals(elements.get(i).getText(), "Reset App State");
			}
		}
	}
	@Test(priority = 3)
	public void verifyProductsOnHomepage() {
		LoginWithValidData();
		List<WebElement> elList = driver.findElements(By.className("inventory_item"));
		Assert.assertEquals(elList.size(), 6);
		for(WebElement el: elList) {
		Assert.assertTrue(el.findElement(By.className("inventory_item_name")).isDisplayed());
		Assert.assertTrue(el.findElement(By.className("inventory_item_price")).getText().contains("$"));
			
		}
	}
	@Test(priority = 3)
	public void testingTheFilterSorting() {
		LoginWithValidData();
		List<WebElement> elList = driver.findElements(By.className("inventory_item"));
		List<String> list = new ArrayList<String>();
		for(WebElement el: elList) {
			list.add(el.findElement(By.className("inventory_item_name")).getText());
		}
		Select select = new Select(driver.findElement(By.className("product_sort_container")));
			select.selectByValue("az");
			
		}
	
	@AfterTest
	public void shutdown() {
		driver.quit();
	}
	

}
