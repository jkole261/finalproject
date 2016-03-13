package edu.ramapo.jkole.alerting;

import java.io.IOException;
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
	public void addApp(Apparatus e){
		apps.add(e);
		String t = e.getUnitLocCoun()+"-"+
				e.getUnitLocMuni()+"-"+e.getUnitLocDist();
		if(!appLocs.contains(t)){
			appLocs.add(t);
		}
	}
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
				c.getCall().get("callInfo"));	
		alert.append("\n !!!!");
		try {
			Main.client.setMessage(alert.toString());
			Main.client.run();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
}