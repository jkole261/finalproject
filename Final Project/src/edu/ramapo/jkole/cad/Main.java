/**/
/**
 * Main.java
 * 
 * @author Jason Kole
 * 
 * the Main class contains the starting point for the program initalizes all database
 * and server connections. for testing purposes the login is bipassed and the Profile 
 * is manually started within this class.
 */
/**/
package edu.ramapo.jkole.cad;
 
import com.mongodb.MongoTimeoutException;

import edu.ramapo.jkole.alerting.AlertCheck;
import edu.ramapo.jkole.alerting.AlertClient;

public class Main {

	private static boolean admin;
	public static Profile pro;
	public static AlertClient client;
	static AlertCheck chk;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		admin = true;
		pro = new Profile("P69ZAF", "13-26-1", 5, 5, 5, 5);
		try{
			Database.Connect();
			chk = new AlertCheck(Main.pro.getAgency());
			chk.start();
			client = new AlertClient(Main.pro.getAgency());
			client.start();
			MainMenu.openMenu(args);
		}
		catch(MongoTimeoutException e){
			System.err.println("TIME OUT DB NOT CONNECT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isAdmin() {
		return admin;
	}

	public static void setAdmin(boolean admin) {
		Main.admin = admin;
	}
}
