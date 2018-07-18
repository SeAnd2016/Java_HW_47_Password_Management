package core;

import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.*;
import java.util.concurrent.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class HtmlUnit {

	public static String decrypt(String encryptedText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher;
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
		return decryptedText;
	}
	
	public static void main(String[] args) throws InterruptedException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		

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

		WebDriver driver = new HtmlUnitDriver();
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
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
			result = "Login failed: " + driver.findElement(By.id("ErrorLineEx")).getText();}
		else result = "Login success";

		System.out.println(result);
		
		wait15.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id=\"logoutMenu\"]/a[1]"))); 
        wait15.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"logoutMenu\"]/a[1]"))).click();   
        
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        WebElement logout = wait15.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class=\"_54nh\"][text()=\"Log Out\"]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logout);
		
		driver.quit();
	}
}
