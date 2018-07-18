package core;

import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Login {

	static WebDriver driver;
	static final String baseUrl = "http://alex.academy/exe/login/";
	final static long start = System.currentTimeMillis();
	
	String url = "http://alex.academy/exe/login/index.html";

	public static void getDriver() {
		Logger.getLogger("").setLevel(Level.OFF);
		
		WebDriver driver = new HtmlUnitDriver();
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		String url = "http://alex.academy/exe/login/index.html";
		driver.get(url);

	}

	public static String login(String username, String password) throws InterruptedException {
		Logger.getLogger("").setLevel(Level.OFF);
		
		WebDriver driver = new HtmlUnitDriver();
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		String url = "http://alex.academy/exe/login/index.html";
		driver.get(url);
		
		String result = null;
		driver.findElement(By.id("id_useranme")).sendKeys(username);
		driver.findElement(By.id("id_password")).sendKeys(password);
		driver.findElement(By.id("id_login_button")).submit();
		Thread.sleep(200);
		if (!driver.getTitle().equals("welcome")) {
			result = "Login failed: " + driver.findElement(By.id("ErrorLineEx")).getText();
		} else if (driver.getTitle().equals("welcome")) {
			result = "Login success";
		}
		driver.close();
		return result;
		
	}

	public static void main(String[] args) throws Exception {

		String username = "alex";
		String mac_address = new Scanner(Runtime.getRuntime().exec("ifconfig en0").getInputStream()).useDelimiter("\\A").next().split(" ")[4];

		System.out.println(login(username, decrypt("1eMNxhuboiJeFByYx24Pyw==", new SecretKeySpec(Arrays.copyOf(mac_address.getBytes("UTF-8"), 16), "AES"))));
	}

	public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
		Cipher cipher;
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
		return decryptedText;
	}
}
