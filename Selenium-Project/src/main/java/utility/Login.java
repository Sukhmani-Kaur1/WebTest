package utility;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Login {
	private WebDriver driver;
	private Actions action;
	public Login(WebDriver driver) {
		this.driver = driver; 
		this.action=new Actions(driver);
		PageFactory.initElements(driver, this);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
	@FindBy(xpath = "//input[@id='user-name']")
	private WebElement userName;
	@FindBy(xpath = "//input[@id='password']")
	private WebElement password;
	@FindBy(xpath = "//input[@id='login-button']")
	private WebElement loginButton;
	
	public void LoginToSwagLab(String username,String password) {
		this.userName.sendKeys(username);
		this.password.sendKeys(password);
		loginButton.click();
		
	}

}
