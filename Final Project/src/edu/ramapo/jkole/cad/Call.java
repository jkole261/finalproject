/**/
/** Call.java
 * 
 * @author Jason Kole
 * 
 * The Call object contains all information that is crutial to an emergency call.
 * within this class also contains the ability to modify one or many calls at one 
 * time. currently there is no safety on the calls to prevent certain modifications.
 **/
/**/
package edu.ramapo.jkole.cad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import edu.ramapo.jkole.cad.locAlert.LocationAlert;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Call {
	private HashMap<String, String> call = new HashMap<String, String>();
	private String status;

	public Call(){
		
	}
	@SuppressWarnings("unchecked")
	public Call(BasicDBObject obj){
		//DIRECT FROM DB
		this.call = (HashMap<String, String>) obj.toMap();
	}
	@SuppressWarnings("unchecked")
	public Call(String cadid){
		this.call = (HashMap<String, String>) Database.getCol("Calls", "basicInfo")
				.findOne(new BasicDBObject("cadid", cadid)).toMap();
	}
	public Call(HashMap<String, String> hashmap){
		this.call = hashmap;
	}
	public HashMap<String, String> getCall() {
		return call;
	}
	public void setCall(HashMap<String, String> call) {
		this.call = call;
	}
	public HashMap<String, String> getDispSeq(Call c){
		//Type, fire protocol
		return null;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.initDisp(String callid)
	 * SYNOPSIS
	 * 		String callid -> callid for call to be dispatched
	 * DESCRIPTION
	 * 		manually starts the process of dispatching units in the 
	 * 		event that the call was placed into pending calls and not
	 * 		dispatched initially. 
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void initDisp(String callid){
		DBCollection coll = Database.getCol("Calls", "status");
		BasicDBObject obj = new BasicDBObject("CallId", callid);
		obj.append("Status", "RCVD");
		coll.insert(obj);
		Dispatch.recUnits(callid);
	}
	public void upgrade(String cadid, String type){
		Dispatch.recUnits(cadid, type);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.clearCall(Call c)
	 * SYNOPSIS
	 * 		Call c -> call that will have its status changed to clear
	 * DESCRIPTION
	 * 		updates all the tables that reference this call to available.
	 * 		sets the clear time to the current time, and changed the status to clear.
	 * 		this also clears every apparatus from the call but going through the list 
	 * 		one by one of all apparatus assigned to the call.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void clearCall(Call c){
		BasicDBObject obj = (BasicDBObject) Database
				.getCol("Calls", "basicInfo")
				.findOne(new BasicDBObject("cadid", c.getCall().get("cadid").toString()));
		obj.put("actid", "");
		Database.update("Calls", "basicInfo", obj, obj.getString("_id"));
		obj = (BasicDBObject) Database
				.getCol("Calls", "status")
				.findOne(new BasicDBObject("CallId", obj.get("cadid").toString()));
		obj.put("Status", "CLEAR");
		Database.update("Calls", "status", obj, obj.getString("_id"));
		
		BasicDBObject obj1 = (BasicDBObject) Database.getCol("Calls", "times")
				.findOne(new BasicDBObject("call", c.getCall().get("cadid").toString()));
		if(!(obj1.containsField("CLEAR"))){
			obj1.put("CLEAR", Clock.getTime());
		}
		Database.getCol("Calls", "times").save(obj1);
		
		//CLEAR ALL APPARATUS FROM CALL
		DBCursor curs = Database.getCol("Apparatus", "info")
				.find(new BasicDBObject("Status.Comment", 
						Pattern.compile(c.getCall().get("cadid").toString(),
								Pattern.CASE_INSENSITIVE)));
		while(curs.hasNext()){
			curs.next();
			clearApp(
					curs.curr().get("AppType").toString(), 
					curs.curr().get("UnitCount").toString(),
					curs.curr().get("UnitMunic").toString(),
					curs.curr().get("appNum").toString(),
					c);
		}
	}
	@Override
	public String toString(){
		return call.toString();
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.addCall(Call c)
	 * SYNOPSIS
	 * 		Call c -> call to be added
	 * DESCRIPTION
	 * 		calls functions that autofill certain aspects of the call such as the cadid number
	 * 		active id number, dublicate number, number of alerts, and call times
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void addCall(Call c) {
		// TODO Auto-generated method stub
		BasicDBObject doc = new BasicDBObject(c.getCall());
		//set cadid
		doc.put("cadid", c.setCADId());
		//set active
		doc.put("actid", c.setActive());
		//CHECK FOR DUPLICATE CALLS
		doc.put("Dups", checkDups(c));
		//Check Alerts on addr
		doc.put("Alerts", checkAlert(c));
		//add to DB
		Database.add("Calls", "basicInfo", doc);
		Database.add("Calls", "times", 
				new BasicDBObject("call", c.getCall().get("cadid").toString()));
		initDisp(doc.getString("cadid"));
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.checkAlert(Call call)
	 * SYNOPSIS
	 * 		Call call -> Call to be check if address contains alerts
	 * DESCRIPTION
	 * 		searches the database of Alerts to see if the call address matches anything in that table.
	 * 		if so returns the number of alerts and displays popup menus with alerts. 
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static String checkAlert(Call call) {
		// TODO Auto-generated method stub
		DBCollection coll = Database.getCol("Alerts", "info");
		DBCursor curs = coll.find(new BasicDBObject("addr", call.getCall().get("addr")));
		String i =curs.size()+"";
		if(((int) Double.parseDouble(i)) > 0){
			sendAlert(curs);
		}
		call.getCall().put("alerts", i);
		return i;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.sendAlert(DBCursor curs)
	 * SYNOPSIS
	 * 		DBCursor curs -> object of alert
	 * DESCRIPTION
	 * 		displays an alert menu with the alert so the dispatcher 
	 * 		can see what it is
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private static void sendAlert(DBCursor curs) {
		while(curs.hasNext()){
			SoundPlayer.playSound();
			System.out.println("ALERT");
			curs.next();
			LocationAlert temp = new LocationAlert();
			temp.setType(curs.curr().get("type").toString());
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Location Alert");
			alert.setHeaderText(temp.getTypeString());
			alert.setContentText(curs.curr().get("info").toString());
			alert.showAndWait();
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.checkDups(Call call)
	 * SYNOPSIS
	 * 		Call call -> call to be checked
	 * DESCRIPTION
	 * 		looks through the database to see if there are duplicate calls 
	 * 		in the system according to address and nature.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static String checkDups(Call call) {
		// TODO Auto-generated method stub
		DBCollection coll = Database.getCol("Calls", "basicInfo");
		String i = null;
		i = coll.find(new BasicDBObject("addr", call.getCall().get("addr"))
				.append("actid", new BasicDBObject("$gte", "0000"))).count()+"";
		call.getCall().put("dups", i);
		return i;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.setCADId()
	 * SYNOPSIS
	 * 		String callid -> callid for call to be dispatched
	 * DESCRIPTION
	 * 		generates the callid based on number of calls in the 
	 * 		system for the given year
	 * RETURNS
	 * 		String cadid -> id of call
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private String setCADId() {
		// TODO Auto-generated method stub
		String cadID = null;
		DBCollection coll = Database.getCol("Calls", "basicInfo");
		int i = coll.find(new BasicDBObject("cadid", 
				new BasicDBObject("$regex", "^"+Clock.getYr()+".*")
				.append("$options", "i"))).count();
		i++;
		cadID = Clock.getYr()+"-"+String.format("%06d", i);
		call.put("cadid", cadID);
		return cadID;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.setEnrt(Call call)
	 * SYNOPSIS
	 * 		Call call -> call to be updated
	 * DESCRIPTION
	 * 		sets the status of the call to enrt as well as updates 
	 * 		the Calls.times database to have the enrt time of the 
	 * 		current time.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void setEnrt(Call call){
		//Update callTimes
		Database.getCol("Calls", "status").findAndModify(
				new BasicDBObject("CallId", call.getCall().get("cadid").toString()), 
				new BasicDBObject("CallId", call.getCall().get("cadid").toString())
					.append("Status", "ENRT"));
		//add to Calls times check if exists
		BasicDBObject obj1 = (BasicDBObject) Database.getCol("Calls", "times")
				.findOne(new BasicDBObject("call", call.getCall().get("cadid").toString()));
		if(!(obj1.containsField("ENRT"))){
			obj1.put("ENRT", Clock.getTime());
		}
		Database.getCol("Calls", "times").save(obj1);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.setActive()
	 * SYNOPSIS
	 * 		
	 * DESCRIPTION
	 * 		sets the activeid of the call so that it will be seen as active
	 * RETURNS
	 * 		String actid
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private String setActive() {
		int i = 0;
		DBCollection coll = Database.getCol("Calls", "basicInfo");
		DBCursor curs = coll.find(new BasicDBObject("cadid", 
				new BasicDBObject("$regex", "^"+Clock.getYr()+".*")
				.append("$options", "i"))
				.append("actid", new BasicDBObject("$gt", "0000")));
		while(curs.hasNext()){
			curs.next();
		}
		try{
			i = (int)Double.parseDouble(curs.curr().get("actid").toString());
		}
		catch(Exception e){
			i = 0;
		}
		i++;
		//increment cadID
		call.put("actid", String.format("%04d", i));
		return String.format("%04d", i);
	}
	public static String getStatus(String cadid) {
		DBCollection coll = Database.getCol("Calls", "status");
		try{
			return coll.findOne(new BasicDBObject("CallId", cadid)).get("Status").toString();
		}
		catch(Exception e){
			return "";
		}
	}
	public BasicDBObject toDBObj() {
		// TODO Auto-generated method stub
		return new BasicDBObject(this.call);
	}

	public static void clearAll() {
		// TODO Auto-generated method stub
		DBCursor curs = Database
				.getCol("Calls", "basicInfo")
				.find(new BasicDBObject("actid", new BasicDBObject("$gte", "0000")));
		do{
			curs.next();
			BasicDBObject obj = (BasicDBObject) curs.curr();
			obj.put("actid", "");
			Database.update("Calls", "basicInfo", obj, obj.getString("_id"));
			obj = (BasicDBObject) Database
					.getCol("Calls", "status")
					.findOne(new BasicDBObject("CallId", obj.get("cadid").toString()));
			obj.put("Status", "CLEAR");
			Database.update("Calls", "status", obj, obj.getString("_id"));
			clearApp(obj.getString("CallId"));
		}
		while(curs.hasNext());
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.clearApp(String String)
	 * SYNOPSIS
	 * 		String string -> callid
	 * DESCRIPTION
	 * 		finds all apparatus with the callid number of string in the status comments.
	 * 		then clears each apparatus within the call. 
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private static void clearApp(String string) {
		// TODO Auto-generated method stub
		DBCursor curs = Database.getCol("Apparatus", "Status")
				.find(new BasicDBObject("Comment", 
						Pattern.compile(string,
								Pattern.CASE_INSENSITIVE)));
		if(curs.hasNext()){
			clearApp(
					curs.curr().get("AppType").toString(), 
					curs.curr().get("UnitCount").toString(),
					curs.curr().get("UnitMunic").toString(),
					curs.curr().get("appNum").toString());
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.clearApp(String atype, String uc, String um, String an)
	 * SYNOPSIS
	 * 		String atype -> Apparatus type
	 * 		String uc ->	Unit County Code
	 * 		String um ->	Unit Municipality Code
	 * 		String an ->	Apparatus Number
	 * DESCRIPTION
	 * 		finds the object containing atype, uc, um, and an in the Apparatus database
	 * 		and then updates the status to available.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private static void clearApp(String atype, String uc, String um, String an) {
		// TODO Auto-generated method stub
		Apparatus app = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", atype)
						.append("UnitCount", uc)
						.append("UnitMunic", um)
						.append("appNum", an)));
		Status.updateStatus(new Status(true, false, false, true, false, app),
				"CALL CLEARED:"+
						ActCallMenu.table.getSelectionModel().getSelectedItem().getCall().get("cadid")+
						"|OPR:"+Login.getUser());	
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.clearApp(String atype, String uc, String um, String an, Call c)
	 * SYNOPSIS
	 * 		String atype -> Apparatus type
	 * 		String uc ->	Unit County Code
	 * 		String um ->	Unit Municipality Code
	 * 		String an ->	Apparatus Number
	 * 		Call c ->		Call to be cleared from
	 * DESCRIPTION
	 * 		finds the object containing atype, uc, um, and an in the Apparatus database
	 * 		and then updates the status to available.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private static void clearApp(String atype, String uc, String um, String an, Call c) {
		Apparatus app = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", atype)
						.append("UnitCount", uc)
						.append("UnitMunic", um)
						.append("appNum", an)));
		Status.updateStatus(new Status(true, false, false, true, false, app),
				"CALL CLEARED:"+
						c.getCall().get("cadid")+
						"|OPR:"+Login.getUser());	
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.setPaged(String callid)
	 * SYNOPSIS
	 * 		String callid	-> cad number for call to be updated
	 * DESCRIPTION
	 * 		finds the call with the callid and updates the call.times 
	 * 		table and adds the paged time of the call.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void setPaged(String callid) {
		// TODO Auto-generated method stub
		BasicDBObject obj = (BasicDBObject) Database.getCol("Calls", "times")
				.findOne(new BasicDBObject("call", callid));
		if(!(obj.containsField("PAGED"))){
			obj.put("PAGED", Clock.getTime());
		}
		Database.getCol("Calls", "times").save(obj);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.setArvd(String callid)
	 * SYNOPSIS
	 * 		String callid	-> cad number for call to be updated
	 * DESCRIPTION
	 * 		finds the call with the callid and updates the call.times 
	 * 		table and adds the arvd time of the call.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void setArvd(Call call) {
		// TODO Auto-generated method stub
		Database.getCol("Calls", "status").findAndModify(
				new BasicDBObject("CallId", call.getCall().get("cadid").toString()), 
				new BasicDBObject("CallId", call.getCall().get("cadid").toString())
					.append("Status", "ARVD"));
		BasicDBObject obj = (BasicDBObject) Database.getCol("Calls", "times")
				.findOne(new BasicDBObject("call", call.getCall().get("cadid")));
		if(!(obj.containsField("ARVD"))){
			obj.put("ARVD", Clock.getTime());
		}
		Database.getCol("Calls", "times").save(obj);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.setArvd(String callid)
	 * SYNOPSIS
	 * 		String callid	-> cad number for call to be updated
	 * DESCRIPTION
	 * 		finds the call with the callid and updates the call.times 
	 * 		table and adds the ctrld time of the call.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void setCtrld(Call call) {
		Database.getCol("Calls", "status").findAndModify(
				new BasicDBObject("CallId", call.getCall().get("cadid").toString()), 
				new BasicDBObject("CallId", call.getCall().get("cadid").toString())
					.append("Status", "CTRLD"));
		BasicDBObject obj = (BasicDBObject) Database.getCol("Calls", "times")
				.findOne(new BasicDBObject("call", call.getCall().get("cadid")));
		if(!(obj.containsField("CTRLD"))){
			obj.put("CTRLD", Clock.getTime());
		}
		Database.getCol("Calls", "times").save(obj);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.addComment(Call selectedCall, String[] str)
	 * SYNOPSIS
	 * 		Call selectedCall -> call selected in the activecallmenu
	 * 		String[] str -> comments for call
	 * DESCRIPTION
	 * 		adds a comment to the calls.comments table with the value of the 
	 * 		string array str.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void addComment(Call selectedCall, String[] str) {
		// TODO Auto-generated method stub
		try{
			BasicDBObject obj = (BasicDBObject) Database.getCol("Calls", "comments")
				.findOne(new BasicDBObject("Call", selectedCall.getCall().get("cadid").toString()));
			if(!(obj.equals(null))){
				String s = "";
				for(int i = 1; i < str.length; i++){
					s += str[i]+" ";
				}
				s += "\n**"+Login.getUser()+" "+Clock.getTime()+"**";
				obj.put("comments", obj.getString("comments")+"\n\n"+s);
				Database.getCol("Calls", "comments").save(obj);
			}
		}
		catch(NullPointerException e){
			BasicDBObject obj = new BasicDBObject("Call", selectedCall.getCall().get("cadid"));
			String s = "";
			for(int i = 1; i < str.length; i++){
				s += str[i]+" ";
			}
			s += "\n**"+Login.getUser()+" "+Clock.getTime()+"**";
			obj.append("comments", s);
			Database.getCol("Calls", "comments").insert(obj);
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.getDispComments()
	 * SYNOPSIS
	 * 		 
	 * DESCRIPTION
	 * 		finds and gets comments from call.comments table for 
	 * 		this instance of call.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public String getDispComments() {
		// TODO Auto-generated method stub
		try{
			return Database.getCol("Calls", "comments")
					.findOne(new BasicDBObject("Call", 
							this.getCall().get("cadid"))).get("comments").toString();
		}
		catch(NullPointerException e){
			return "";
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.getAppFromCall(String callid)
	 * SYNOPSIS
	 * 		String callid	-> cad number for call to get Apparatus from
	 * DESCRIPTION
	 * 		finds all apparatus that are assigned to call with cadid of callid
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static List<Apparatus> getAppFromCall(String callid) {
		DBCursor curs = Database.getCol("Apparatus", "info")
				.find(new BasicDBObject("Status.Comment", 
						Pattern.compile(callid,
								Pattern.CASE_INSENSITIVE)));
		List<Apparatus> applist = new ArrayList<Apparatus>();
		while(curs.hasNext()){
			curs.next();
			applist.add(new Apparatus((BasicDBObject)curs.curr()));
		}
		return applist;
	}
}