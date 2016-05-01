/**/
/** Alert.java
 * 
 * @author Jason Kole
 * 
 * The alert class creates an object that stores a call, apparatus, and locations
 * of that apparatus to send an alert to the respecive locations to nofity apparatus
 * of a call. all locations and apparatus are stored in ArrayList formats. 
 **/
/**/

package edu.ramapo.jkole.alerting;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

import edu.ramapo.jkole.cad.Apparatus;
import edu.ramapo.jkole.cad.Call;
import edu.ramapo.jkole.cad.Database;
import edu.ramapo.jkole.cad.Main;

public class Alert {
	List<Apparatus> apps;
	List<String> appLocs;
	Call c;
	/**/
	/*
	 * NAME
	 * 		Alert(); Alert(String s); Alert(Call c)
	 * SYNOPSIS
	 * 		String s -> cadid of call to create alert for
	 * 		Call c -> call to create alert for
	 * DESCRIPTION
	 * 		this constuctor creates an object of an alert, if the constructor is passed through
	 * 		with a string (a cadid) it will search for the call within the mongo database and 
	 * 		return the call. if the constructor is created with a call then that step is skipped.
	 * RETURNS
	 * 		an alert object
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public Alert(){
		apps = new ArrayList<Apparatus>();
		appLocs = new ArrayList<String>();
	}
	public Alert(String s){
		c = new Call((BasicDBObject)Database.getCol("Calls", "basicInfo").findOne(new BasicDBObject("cadid", s)));
		apps = new ArrayList<Apparatus>();
		appLocs = new ArrayList<String>();
	}
	public Alert(Call c){
		this.c = c;
		apps = new ArrayList<Apparatus>();
		appLocs = new ArrayList<String>();
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.Alert.addApp(Apparatus e)
	 * SYNOPSIS
	 * 		Apparatus e -> apparatus to be added
	 * DESCRIPTION
	 * 		adds Apparatus e to List<Apparatus> apps and their locations to 
	 * 		List<String> appLocs to the alert object
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public void addApp(Apparatus e){
		apps.add(e);
		String t = e.getUnitLocCoun()+"-"+
				e.getUnitLocMuni()+"-"+e.getUnitLocDist();
		if(!appLocs.contains(t)){
			appLocs.add(t);
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.Alert.removeApp(Apparatus e)
	 * SYNOPSIS
	 * 		Apparatus e -> apparatus to be removed from list
	 * DESCRIPTION
	 * 		removes apparatus e from List<Apparatus> apps
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public void removeApp(Apparatus e){
		apps.remove(e);
	}
	public void setCall(Call c){
		this.c = c;
	}
	public List<Apparatus> getApps(){
		return apps;
	}
	public Call getCall(){
		return c;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.Alert.sendAlert()
	 * SYNOPSIS
	 * 		this.Alert 					-> alert to be sent to server
	 * 			Call c 					->	call that alert is build for
	 * 			List<Apparatus> apps	->	list of apparatus assigned to call c
	 * 			List<Apparatus> appLocs	->	list of locations of apps
	 * 		StringBuilder alert 		->	stringbuilder that will contain the text for the alert
	 * DESCRIPTION
	 * 		goes through each apparatus in apps locations from appLocs and adds them to alert 
	 * 		to be send through the server. 
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public void sendAlert(){
		StringBuilder alert = new StringBuilder("** ");
		for(String e : appLocs){
			alert.append(e +" ");
		}
		alert.append("**\n");
		for(Apparatus a: apps){
			alert.append(a.getUnitString() +" ");
		}
		alert.append("\n"+c.getCall().get("addr")+"\n"+
				c.getCall().get("nature")+"\n"+
				c.getCall().get("type")+"\n"+
				c.getCall().get("callInfo"));	
		alert.append("\n !!!!");
		try {
			Main.client.out.println(alert);
		} catch(NullPointerException e){
			System.err.println("NO ALERT SENT");
		}
	}
}