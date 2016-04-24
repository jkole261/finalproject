package edu.ramapo.jkole.cad.locAlert;

import java.util.HashMap;

import com.mongodb.BasicDBObject;

import edu.ramapo.jkole.cad.Database;

public class LocationAlert {
	private static String alertID;
	private static String location;
	private static String city;
	private static int munic;
	private static String state;
	private static String type;
	private static String info;
	
	public LocationAlert(){
		
	}
	
	public LocationAlert(HashMap<String, String> map){
		alertID = map.get("id");
		location = map.get("addr"); //formatted address
		city = map.get("city");
		munic = (int)Double.parseDouble(map.get("munic"));
		state = map.get("state");
		type = map.get("type");
		info = map.get("info");
	}

	public static String getAlertID() {
		return alertID;
	}

	public static void setAlertID(String alertID) {
		LocationAlert.alertID = alertID;
	}

	public static String getLocation() {
		return location;
	}

	public static void setLocation(String location) {
		LocationAlert.location = location;
	}

	public static String getCity() {
		return city;
	}

	public static void setCity(String city) {
		LocationAlert.city = city;
	}

	public static int getMunic() {
		return munic;
	}

	public static void setMunic(int munic) {
		LocationAlert.munic = munic;
	}

	public static String getState() {
		return state;
	}

	public static void setState(String state) {
		LocationAlert.state = state;
	}

	public static String getType() {
		return type;
	}

	public static void setType(String type) {
		LocationAlert.type = type;
	}

	public static String getInfo() {
		return info;
	}

	public static void setInfo(String info) {
		LocationAlert.info = info;
	}
	
	public static HashMap<String, String> toMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", alertID);
		map.put("addr", location);
		map.put("city", city);
		map.put("munic", munic+"");
		map.put("state", state);
		map.put("type", type);
		map.put("info", info);
		return map;
	}
	
	@SuppressWarnings("static-access")
	public static void update(LocationAlert la){
		BasicDBObject obj = (BasicDBObject) Database.getCol("Alerts", "info").findOne(new BasicDBObject(toMap()));
		
		if(!la.getAlertID().equalsIgnoreCase(alertID)){
			alertID = la.getAlertID();
		}
		if(!la.getCity().equalsIgnoreCase(city)){
			city = la.getCity();
		}
		if(!la.getInfo().equalsIgnoreCase(info)){
			info = la.getInfo();
		}
		if(!la.getLocation().equalsIgnoreCase(location)){
			location = la.getLocation();
		}
		if(!(la.getMunic() == munic)){
			munic = la.getMunic();
		}
		if(!la.getState().equalsIgnoreCase(state)){
			state = la.getState();
		}
		if(!la.getType().equalsIgnoreCase(type)){
			type = la.getType();
		}
		Database.getCol("Alerts", "info").update(obj, new BasicDBObject(toMap()));
	}
}
