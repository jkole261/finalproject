package edu.ramapo.jkole.cad;

import com.mongodb.MongoTimeoutException;

import edu.ramapo.jkole.alerting.AlertClient;

public class Main {

	private static boolean admin;
	public static Profile pro;
	public static AlertClient client;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		admin = true;
		pro = new Profile("P69ZAF", "13-26-1", 5, 5, 5, 5);
		try{
			Database.Connect();
			MainMenu.openMenu(args);
		}
		catch(MongoTimeoutException e){
			System.out.println("TIME OUT DB NOT CONNECT");
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
