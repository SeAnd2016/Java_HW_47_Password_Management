package core;
 
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
 
public class Encrypt {
       static Cipher cipher;
       public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
              cipher.init(Cipher.ENCRYPT_MODE, secretKey);
              String encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
              return encryptedText;}
 
       public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
              cipher = Cipher.getInstance("AES");
              cipher.init(Cipher.DECRYPT_MODE, secretKey);
              String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
              return decryptedText;}
 
       public static void main(String[] args) throws Exception {
              cipher = Cipher.getInstance("AES");
              String password = "password"; //type password here
              String key = "8c:85:90:09:ad:33";
              key = key.replaceAll("-", ":");  // 00-0c-29-3e-07-e6  =>  00:0c:29:3e:07:e6
              SecretKeySpec sk = new SecretKeySpec(Arrays.copyOf(key.getBytes("UTF-8"), 16), "AES");
              System.out.println("Password: \t" + password);
              System.out.println("MAC Address: \t" + key);
              String encryptedpassword = encrypt(password, sk);
              System.out.println("Encrypted: \t" + encryptedpassword);
              String decryptedpassword = decrypt(encryptedpassword, sk);
              System.out.println("Test: \t\t" + decryptedpassword);
       }
}
