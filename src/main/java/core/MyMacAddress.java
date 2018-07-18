package core;
 
import java.util.*;
 
public class MyMacAddress {
 
       public static void main(String[] args) throws Exception {
			String mac_address;
			String cmd_mac = "ifconfig en0";
			String cmd_win = "cmd /C for /f \"usebackq tokens=1\" %a in (`getmac ^| findstr Device`) do echo %a";
 
			if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {

				mac_address = new Scanner(Runtime.getRuntime().exec(cmd_win).getInputStream()).useDelimiter("\\A").next().split(" ")[1];}
				else {mac_address = new Scanner(Runtime.getRuntime().exec(cmd_mac).getInputStream()).useDelimiter("\\A").next().split(" ")[4];}
				mac_address = mac_address.toLowerCase().replaceAll("-", ":");
				System.out.println(mac_address);
				
       }
}
