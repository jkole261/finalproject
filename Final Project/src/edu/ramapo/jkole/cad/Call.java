package edu.ramapo.jkole.cad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

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
	public void changeDispSeq(Call c, String newseq){
		
	}
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
					curs.curr().get("appNum").toString());
		}
	}
	public void addAgency(Call c, char agen){
		
	}
	public void removeAgency(Call c, char agen){
		
	}
	@Override
	public String toString(){
		return call.toString();
	}
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
	public static String checkAlert(Call call) {
		// TODO Auto-generated method stub
		DBCollection coll = Database.getCol("Calls", "alerts");
		String i = null;
		i = coll.find(new BasicDBObject("addr", call.getCall().get("addr"))).count()+"";
		call.getCall().put("alerts", i);
		return i;
	}
	public static String checkDups(Call call) {
		// TODO Auto-generated method stub
		DBCollection coll = Database.getCol("Calls", "basicInfo");
		String i = null;
		i = coll.find(new BasicDBObject("addr", call.getCall().get("addr"))
				.append("actid", new BasicDBObject("$gte", "0000"))).count()+"";
		call.getCall().put("dups", i);
		return i;
	}
	private String setCADId() {
		// TODO Auto-generated method stub
		String cadID = null;
		DBCollection coll = Database.getCol("Calls", "basicInfo");
		int i = coll.find(new BasicDBObject("cadid", 
				new BasicDBObject("$regex", "^"+Clock.getYr()+".*")
				.append("$options", "i"))).count();
		i++;
		//increment cadID
		cadID = Clock.getYr()+"-"+String.format("%06d", i);
		call.put("cadid", cadID);
		return cadID;
	}
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
	public static void setPaged(String callid) {
		// TODO Auto-generated method stub
		BasicDBObject obj = (BasicDBObject) Database.getCol("Calls", "times")
				.findOne(new BasicDBObject("call", callid));
		if(!(obj.containsField("PAGED"))){
			obj.put("PAGED", Clock.getTime());
		}
		Database.getCol("Calls", "times").save(obj);
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}