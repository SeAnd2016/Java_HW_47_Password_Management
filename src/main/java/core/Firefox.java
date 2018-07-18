package core;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Firefox {
	
	static WebDriver driver;
	
	By by;
	
	public static boolean isPresent(final By by) {
		//driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		if (!driver.findElements(by).isEmpty()) return true;
		else return false;
	}
	
	public static String decrypt(String encryptedText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher;
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
		return decryptedText;
	}
	
	public static void main(String[] args) throws InterruptedException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
			
		// Disable the logs
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);
		
		String mac_address;
		String cmd_mac = "ifconfig en0";
		String cmd_win = "cmd /C for /f \"usebackq tokens=1\" %a in (`getmac ^| findstr Device`) do echo %a";
		if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_win).getInputStream()).useDelimiter("\\A").next().split(" ")[1].toLowerCase();
		} else {
			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_mac).getInputStream()).useDelimiter("\\A").next().split(" ")[4];
		}
		
		String email_field = "type_email"; // type email
		
		String password = decrypt("Mx0FXVdhmcebKAjv6ycA8w==", new SecretKeySpec(Arrays.copyOf(mac_address.getBytes("UTF-8"), 16), "AES"));
		
		String driverPath = "";
		
		String url = "http://facebook.com/";
		
		// We are checking, which system we are using for test execution
		if (System.getProperty("os.name").toUpperCase().contains("MAC"))
			driverPath = "./resources/webdrivers/mac/geckodriver.sh";

        else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) // If we have WebDrivers for PC
            driverPath = "./resources/webdrivers/pc/geckodriver.exe";

        else 
	        throw new IllegalArgumentException("Unknown OS. Script should be executed on Mac");
		
		System.setProperty("webdriver.gecko.driver", driverPath);
		
		// Disable notifications
		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("dom.webnotifications.enabled", false);
		
		// Firefox on foreground
		options.addArguments("-foreground"); 
		
		driver = new FirefoxDriver(options);

		// driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		WebDriverWait wait15 = new WebDriverWait(driver, 15);
		WebDriverWait wait30 = new WebDriverWait(driver, 30);
		
		// Default browser windows size
		Dimension windowSize = driver.manage().window().getSize();
		System.out.println("01. Windows size: " + windowSize);
		
		//Web Framework Benchmarks
		final long start = System.currentTimeMillis();

        driver.get(url);
        
		//User agent detection
		String userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
		System.out.println("02. User Agent is: " + userAgent);
		
		// driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait15.until(ExpectedConditions.titleIs("Facebook - Log In or Sign Up"));
        wait15.until(ExpectedConditions.titleContains("Log In"));
        String title = driver.getTitle();
        // driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        
		System.out.println("03. Browser is: Firefox");
		
		String result = null;
		if (!driver.getTitle().equals("Facebook - Log In or Sign Up")) {
			result = "Login failed"; System.out.println(result);}
		//else result = "Login success";
		
		System.out.println("04. Title of the page: " + title);
        
		String copyrightString = wait15.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pageFooter\"]/div[3]/div/span"))).getText();
		By copyright = By.xpath("//*[@id=\"pageFooter\"]/div[3]/div/span");
		System.out.println("05. Element [Copyright Text]: \"" + copyrightString + "\" " + (isPresent(copyright) ? "Exists" : "Not exists"));
        System.out.println("06. Size of [Copyright Text]: \"" + copyrightString + "\" " + (Dimension) driver.findElement(By.xpath("//*[@id=\"pageFooter\"]/div[3]/div/span")).getSize());
        System.out.println("07. Location of [Copyright Text]: \"" + copyrightString + "\" " + (Point) driver.findElement(By.xpath("//*[@id=\"pageFooter\"]/div[3]/div/span")).getLocation());
        
        // Log In
        wait15.until(ExpectedConditions.presenceOfElementLocated(By.id("email"))).clear();
        wait15.until(ExpectedConditions.presenceOfElementLocated(By.id("email"))).sendKeys(email_field);
        By email = By.id("email");
		System.out.println("08. Element [Email Field]: " + (isPresent(email) ? "Exists" : "Not exists"));
        System.out.println("09. Size of [Email Field]: " + (Dimension) driver.findElement(By.id("email")).getSize());
        System.out.println("10. Location of [Email Field]: " + (Point) driver.findElement(By.id("email")).getLocation());
        
        wait15.until(ExpectedConditions.presenceOfElementLocated(By.id("pass"))).clear();
        wait15.until(ExpectedConditions.presenceOfElementLocated(By.id("pass"))).sendKeys(password);
        
		System.out.println("11. Element [Password Field]: " + (isPresent(email) ? "Exists" : "Not exists"));
        System.out.println("12. Size of [Password Field]: " + (Dimension) driver.findElement(By.id("pass")).getSize());
        System.out.println("13. Location of [Password Field]: " + (Point) driver.findElement(By.id("pass")).getLocation());
        
        wait15.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"loginbutton\"]/input"))); 
        By loginButton = By.xpath("//*[@id=\"loginbutton\"]/input");
		System.out.println("14. Element [Login Button]: " + (isPresent(loginButton) ? "Exists" : "Not exists"));
        System.out.println("15. Size of [Login Button]: " + (Dimension) driver.findElement(By.xpath("//*[@id=\"loginbutton\"]/input")).getSize());
        System.out.println("16. Location of [Login Button]: " + (Point) driver.findElement(By.xpath("//*[@id=\"loginbutton\"]/input")).getLocation());
        wait15.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"loginbutton\"]/input"))).click(); 
        
        // Find friends
        wait15.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"u_0_a\"]/div[1]/div[1]/div/a/span/span"))); 
        By timelineButton = By.xpath("//*[@id=\"u_0_a\"]/div[1]/div[1]/div/a/span/span");
		System.out.println("17. Element [Timeline Button]: " + (isPresent(timelineButton) ? "Exists" : "Not exists"));
        System.out.println("18. Size of [Timeline Button]: " + (Dimension) driver.findElement(By.xpath("//*[@id=\"u_0_a\"]/div[1]/div[1]/div/a/span/span")).getSize());
        System.out.println("19. Location of [Timeline Button]: " + (Point) driver.findElement(By.xpath("//*[@id=\"u_0_a\"]/div[1]/div[1]/div/a/span/span")).getLocation());
        wait30.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"u_0_a\"]/div[1]/div[1]/div/a/span/span"))).click();
        
        String friends = wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class=\"_gs6\"]"))).getText();
        By friendsButton = By.xpath("//span[@class=\"_gs6\"]");
		System.out.println("20. Element [Friends Button]: " + (isPresent(friendsButton) ? "Exists" : "Not exists"));
        System.out.println("21. Size of [Friends Button]: " + (Dimension) driver.findElement(By.xpath("//span[@class=\"_gs6\"]")).getSize());
        System.out.println("22. Location of [Friends Button]: " + (Point) driver.findElement(By.xpath("//span[@class=\"_gs6\"]")).getLocation());
        
        wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id=\"logoutMenu\"]/a[1]"))); 
        By accountSettings = By.xpath("//div[@id=\"logoutMenu\"]/a[1]");
		System.out.println("23. Element [Account Settings]: " + (isPresent(accountSettings) ? "Exists" : "Not exists"));
        System.out.println("24. Size of [Account Settings]: " + (Dimension) driver.findElement(By.xpath("//div[@id=\"logoutMenu\"]/a[1]")).getSize());
        System.out.println("25. Location of [Account Settings]: " + (Point) driver.findElement(By.xpath("//div[@id=\"logoutMenu\"]/a[1]")).getLocation());
        wait30.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"logoutMenu\"]/a[1]"))).click();   
        
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        By logoutButton = By.xpath("//span[@class=\"_54nh\"][text()=\"Log Out\"]");
		System.out.println("26. Element [Log Out Button]: " + (isPresent(logoutButton) ? "Exists" : "Not exists"));
        System.out.println("27. Size of [Log Out Button]: " + (Dimension) driver.findElement(By.xpath("//span[@class=\"_54nh\"][text()=\"Log Out\"]")).getSize());
        System.out.println("28. Location of [Log Out Button]: " + (Point) driver.findElement(By.xpath("//span[@class=\"_54nh\"][text()=\"Log Out\"]")).getLocation());
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        WebElement logout = wait15.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class=\"_54nh\"][text()=\"Log Out\"]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logout);
        
		driver.quit();
		
		//Web Framework Benchmarks
		final long finish = System.currentTimeMillis();

		System.out.println("---------------------------------");
		System.out.println("29. Copyright: " + copyrightString);
		System.out.println("30. You have " + friends + " friends");
		System.out.println("31. Response time: " + (finish - start)/1000 + " sec");
	}
} 