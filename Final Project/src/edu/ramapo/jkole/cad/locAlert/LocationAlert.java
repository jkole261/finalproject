/**/
/**
 * LocationAlert.java
 * 
 * @author Jason Kole
 * 
 * LocationAlert objects store a location and an alert 
 * type. once the object is pulled from the database a 
 * notification appears once the call is dipatched. 
 */
/**/
package edu.ramapo.jkole.cad.locAlert;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.BasicDBObject;

import edu.ramapo.jkole.cad.ChoiceMenu;
import edu.ramapo.jkole.cad.Database;
import javafx.scene.control.TreeItem;

public class LocationAlert {
	private  ObjectId alertID;
	private  String location;
	private  String city;
	private  String munic;
	private  String type;
	private  String info;
	
	public LocationAlert(){
		
	}
	
	public LocationAlert(BasicDBObject obj){
		new LocationAlert((HashMap<String, String>) obj.toMap());
	}
	
	public LocationAlert(HashMap<String, String> map){
		location = map.get("addr"); //formatted address
		city = map.get("city");
		munic = map.get("munic");
		type = map.get("type");
		info = map.get("info");
	}

	public ObjectId getAlertID() {
		return alertID;
	}

	public void setAlertID(ObjectId alertID) {
		this.alertID = alertID;
	}

	public String getLocation() {
		return location;
	}

	public String getTypeString(){
		JSONParser parser = new JSONParser(); 
        try {
            Object obj = parser.parse(new FileReader(
            		"lib/alertType.dat"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray objs = (JSONArray) jsonObject.get("Types");
			Iterator<JSONObject> iterator = objs.iterator();
            while (iterator.hasNext()) {
            	JSONObject tobj = iterator.next();         	
            	Object obj1 = parser.parse(new FileReader(
                        "lib/alertType.dat"));
                JSONObject jobject = (JSONObject) obj1;
                JSONArray obja = (JSONArray) jobject.get(tobj.get("type").toString());
    			Iterator<JSONObject> iterator2 = obja.iterator();
                while (iterator2.hasNext()) {
                	JSONObject jobj = iterator2.next();
                	if(jobj.get("code").toString().equalsIgnoreCase(this.type)){
                		return jobj.get("type").toString();
                	}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return "";
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMunic() {
		return munic;
	}

	public void setMunic(String munic) {
		this.munic = munic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public HashMap<String, String> toMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("addr", location);
		map.put("city", city);
		map.put("munic", munic+"");
		map.put("type", type);
		map.put("info", info);
		return map;
	}
	
	@SuppressWarnings("static-access")
	public void update(LocationAlert la){
		BasicDBObject obj = (BasicDBObject) Database.getCol("Alerts", "info").findOne(new BasicDBObject(toMap()));
		
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
		if(!la.getType().equalsIgnoreCase(type)){
			type = la.getType();
		}
		Database.getCol("Alerts", "info").update(obj, new BasicDBObject(toMap()));
	}
}