/**/
/**
 * Status.java
 * 
 * @author Jason Kole
 * 
 * the Status class translates all functions within the program, all 
 * modifications to Apparatus, to a status string and object. the object
 * contains several boolean values to determine if the apparatus is available
 * to be dispatched to a call and its location.
 */
/**/
package edu.ramapo.jkole.cad;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Status {
	private String oid;
	private boolean active; //IN SERVICE OR OOS
	private boolean enrt; 
	private boolean onscene;
	private boolean avail;
	private boolean busy;
	private String timestamp;
	private String comment;
	private String appstring;
	private Apparatus app;

	public Status(Apparatus dat){
		this.app = dat;
	}

	public Status(boolean active, boolean enrt, boolean onscene,
			boolean avail, boolean busy, String timestamp, Apparatus app){
		this.active = active;
		this.enrt = enrt;
		this.onscene = onscene;
		this.avail = avail;
		this.busy = busy;
		this.timestamp = timestamp;
		this.app = app;
	}
	
	
	public Status(boolean active, boolean enrt, boolean onscene, 
			boolean avail, boolean busy, Apparatus app){
		this.active = active;
		this.enrt = enrt;
		this.onscene = onscene;
		this.avail = avail;
		this.busy = busy;
		this.timestamp = getTime();
		this.app = app;
	}
	
	public Status(boolean active, boolean enrt, boolean onscene, 
			boolean busy, boolean avail, String timestamp) {
		this.active = active;
		this.enrt = enrt;
		this.onscene = onscene;
		this.avail = avail;
		this.busy = busy;
		this.timestamp = timestamp;
	}

	public Status(boolean active, boolean enrt, boolean onscene,
			boolean avail, boolean busy, String timestamp, 
			String Comm, Apparatus app){
		this.active = active;
		this.enrt = enrt;
		this.onscene = onscene;
		this.avail = avail;
		this.busy = busy;
		this.timestamp = timestamp;
		this.comment = Comm;
		this.app = app;
	}

	public Status(String oid, boolean active, boolean enrt, boolean onscene,
			boolean avail, boolean busy, String timestamp, 
			String Comm, String app){
		this.oid = oid;
		this.active = active;
		this.enrt = enrt;
		this.onscene = onscene;
		this.avail = avail;
		this.busy = busy;
		this.timestamp = timestamp;
		this.comment = Comm;
		this.appstring = app;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Status.getAppString()
	 * SYNOPSIS
	 * 		
	 * DESCRIPTION
	 * 		returns the apparatus string in the format 'E1326A'
	 * 		these values are AppType, CountyNumber, MunicNumber, AppNumber
	 * RETURNS
	 * 		Action
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public String getAppstring() {
		DBObject obj = Database.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("_id", new ObjectId(appstring)));
		return obj.get("AppType").toString()+obj.get("UnitCount").toString()+
				obj.get("UnitMunic").toString()+obj.get("appNum").toString();
	}
	public String getOid(){
		return oid;
	}
	
	public Status(boolean active, boolean enrt, boolean onscene,
			boolean avail, boolean busy, String timestamp, String Comm) {
		this.active = active;
		this.enrt = enrt;
		this.onscene = onscene;
		this.avail = avail;
		this.busy = busy;
		this.timestamp = timestamp;
		this.setComment(Comm);
	}
	public Status(DBObject curr) {
		oid = curr.get("_id").toString();
		active = Boolean.parseBoolean(curr.get("active").toString()); //IN SERVICE OR OOS
		enrt = Boolean.parseBoolean(curr.get("enrt").toString()); 
		onscene = Boolean.parseBoolean(curr.get("onscene").toString());
		avail = Boolean.parseBoolean(curr.get("avail").toString());
		busy = Boolean.parseBoolean(curr.get("busy").toString());
		timestamp = curr.get("TimeStamp").toString();
		comment = curr.get("Comment").toString();
		app = Apparatus.getApparatus(curr.get("Apparatus").toString());
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Status.updateStatus(Status dat, String comment)
	 * SYNOPSIS
	 * 		Status dat -> new instance of status to be updated
	 * 		String comment ->	comment for status to be updated
	 * DESCRIPTION
	 * 		updates the status dat into the database of Apparatus.status 
	 * 		the new status
	 * RETURNS
	 * 		Action
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	public static boolean updateStatus(Status dat, String comment){
		DBCollection coll = Database.getCol("Apparatus", "Status");
		String timeStamp = new SimpleDateFormat("yyyyMMdd.HHmmssSSS").format(Calendar.getInstance().getTime());
		
		BasicDBObject obj = new BasicDBObject("Apparatus", dat.app.getOid())
			.append("avail", dat.isAvail())
			.append("enrt", dat.isEnrt())
			.append("onscene", dat.isOnscene())
			.append("busy", dat.isBusy())
			.append("active", dat.isActive())
			.append("TimeStamp", timeStamp)
			.append("Comment", comment);
		
		if(dat.app.getUnitString().contains("*")){
			return true;
		}
		
		BasicDBObject app = (BasicDBObject) Database.client.getDB("Apparatus").getCollection("info").findOne(
				new BasicDBObject("_id", new ObjectId(dat.app.getOid())));
		app.put("Status", obj);
		
		Database.client.getDB("Apparatus").getCollection("info").save(app);
		coll.insert(obj);
		
		return true;
	}
	
	public static String getTime(){
		String timeStamp = new SimpleDateFormat("yyyyMMdd.HHmmssSSS").format(Calendar.getInstance().getTime());
		return timeStamp;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public boolean isBusy() {
		return busy;
	}
	
	public Apparatus getApp(){
		return app;
	}
	
	public boolean isActive() {
		return active;
	}

	public boolean isEnrt() {
		return enrt;
	}

	public boolean isOnscene() {
		return onscene;
	}

	public boolean isAvail() {
		return avail;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Status.getStatString(String dat)
	 * SYNOPSIS
	 * 		returns a string of Status
	 * DESCRIPTION
	 * 		converts the status into a readable string
	 * RETURNS
	 * 		String 
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	public static String getStatString(String dat) throws JSONException {
		JSONObject string = new JSONObject(dat);
		if(!Boolean.parseBoolean(string.getString("active"))){
			return "OOS";
		}
		if(Boolean.parseBoolean(string.getString("avail"))){
			return "AVAIL";
		}
		if(Boolean.parseBoolean(string.getString("enrt"))){
			return "ENRT";
		}
		if(Boolean.parseBoolean(string.getString("onscene"))){
			return "ONLOC";
		}
		if(Boolean.parseBoolean(string.getString("busy"))){
			return "BUSY";
		}
		return "ERR";
	}
	@Override
	public String toString(){
		if(!active){
			return "OOS";
		}
		if(avail){
			return "AVAIL";
		}
		if(enrt){
			return "ENRT";
		}
		if(onscene){
			return "ONLOC";
		}
		if(busy){
			return "BUSY";
		}
		return "ERR";
	}
}