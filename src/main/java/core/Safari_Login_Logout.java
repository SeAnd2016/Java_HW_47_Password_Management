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
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
 
public class Safari_Login_Logout {
	
		public static String decrypt(String encryptedText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
			Cipher cipher;
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
			return decryptedText;
		}
	
		static WebDriver driver;
		
		public static void main(String[] args) throws InterruptedException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
			
	    	// Disable the logs
	   		Logger logger = Logger.getLogger("");
	   		logger.setLevel(Level.OFF);
    	    
    	    // We are checking, if system is Mac
    		if (!System.getProperty("os.name").toUpperCase().contains("MAC"))
    			throw new IllegalArgumentException("Safari is available only on Mac");
    	    
    		String mac_address;
    		String cmd_mac = "ifconfig en0";
    		String cmd_win = "cmd /C for /f \"usebackq tokens=1\" %a in (`getmac ^| findstr Device`) do echo %a";
    		if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
    			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_win).getInputStream()).useDelimiter("\\A").next().split(" ")[1].toLowerCase();
    		} else {
    			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_mac).getInputStream()).useDelimiter("\\A").next().split(" ")[4];
    		}
    				
    		String email = "type_email"; // type email
    		
    		String password = decrypt("Mx0FXVdhmcebKAjv6ycA8w==", new SecretKeySpec(Arrays.copyOf(mac_address.getBytes("UTF-8"), 16), "AES"));

    		Thread.sleep(200);
    		Logger.getLogger("").setLevel(Level.OFF);
    		String url = "http://facebook.com/";

    		// We are checking, if system is Mac
    		if (!System.getProperty("os.name").toUpperCase().contains("MAC"))
    			throw new IllegalArgumentException("Safari is available only on Mac");
    	    
			driver = new SafariDriver();
			// driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			
    		driver.get(url);
    		
    		WebDriverWait wait15 = new WebDriverWait(driver, 15);

    		String result = null;
    		
    		wait15.until(ExpectedConditions.titleIs("Facebook - Log In or Sign Up"));
            wait15.until(ExpectedConditions.titleContains("Log In"));
           
            wait15.until(ExpectedConditions.presenceOfElementLocated(By.id("email"))).clear();
            wait15.until(ExpectedConditions.presenceOfElementLocated(By.id("email"))).sendKeys(email);
            
            wait15.until(ExpectedConditions.presenceOfElementLocated(By.id("pass"))).clear();
            wait15.until(ExpectedConditions.presenceOfElementLocated(By.id("pass"))).sendKeys(password);
            
            wait15.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"loginbutton\"]/input"))); 
            wait15.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"loginbutton\"]/input"))).click();
    		
    		Thread.sleep(200);

    		if (!driver.getTitle().equals("Facebook - Log In or Sign Up")) {
    			result = "Login failed";}
    		else result = "Login success";

    		System.out.println(result);
    		
    		wait15.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id=\"logoutMenu\"]/a[1]"))); 
            wait15.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"logoutMenu\"]/a[1]"))).click();   
            
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            WebElement logout = wait15.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class=\"_54nh\"][text()=\"Log Out\"]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logout);
                      
    		driver.quit();

    	}
    } 