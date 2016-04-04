package edu.ramapo.jkole.cad;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.json.JSONException;

import com.mongodb.BasicDBObject;

public class Apparatus {
	private final String counNum;
	private final String muniNum;
	private String appNum;
	private final String unitYear;
	private final String unitMake;
	private  String unitLocCoun;
	private  String unitLocMuni;
	private  String unitLocDist;
	private boolean engine;
	private final boolean ladder;
	private final boolean rescue;
	private final boolean tanker;
	private final boolean foam;
	private final boolean bls;
	private final boolean brush;
	private final boolean als;
	private final boolean tbls;
	private final boolean tals;
	private boolean supv;
	private final String crew;
	private final String wtank;
	private final String ladSize;
	private final String ftank;
	private String stat;
	
	public Apparatus(
			String oid, String apptype, String counNum, String muniNum, String appNum,
			String unitYear, String unitMake, 
			String unitLocCoun, String unitLocMuni, String unitLocDist, 
			boolean engine, boolean ladder, boolean rescue, boolean tanker, boolean foam, 
			boolean bls, boolean als, boolean tbls, boolean tals,
			String crew, String wtank, String ladSize, String ftank) {
		super();
		this.setOid(oid);
		this.counNum = counNum;
		this.muniNum = muniNum;
		this.appNum = appNum;
		this.appType = apptype;
		this.unitYear = unitYear;
		this.unitMake = unitMake;
		this.unitLocCoun = unitLocCoun;
		this.unitLocMuni = unitLocMuni;
		this.unitLocDist = unitLocDist;
		this.engine = engine;
		this.ladder = ladder;
		this.rescue = rescue;
		this.tanker = tanker;
		if(appType.equalsIgnoreCase("b")){
			this.brush = true;
		}
		else{
			this.brush = false;
		}
		this.foam = foam;
		this.bls = bls;
		this.als = als;
		this.tbls = tbls;
		this.tals = tals;
		this.crew = crew;
		this.wtank = wtank;
		this.ladSize = ladSize;
		this.ftank = ftank;
		this.supv = false;
		stat = null;
	}

	public Apparatus(BasicDBObject obj){
		this.supv = false;
		this.appType = null;
		this.oid = obj.getString("_id");
		this.counNum = obj.get("UnitCount").toString();
		this.muniNum = obj.get("UnitMunic").toString();
		this.unitYear = obj.get("AppYr").toString();
		this.unitMake = obj.get("AppMake").toString();
		this.unitLocCoun = obj.get("UnitCounLoc").toString();
		this.unitLocMuni = obj.get("UnitMuniLoc").toString();
		this.unitLocDist = obj.get("UnitDistLoc").toString();
		this.engine = Boolean.parseBoolean(obj.get("Engine").toString());
		this.ladder = Boolean.parseBoolean(obj.get("Ladder").toString());
		this.rescue = Boolean.parseBoolean(obj.get("Rescue").toString());
		this.tanker = Boolean.parseBoolean(obj.get("Tank").toString());
		this.foam = Boolean.parseBoolean(obj.get("Foam").toString());
		this.brush = Boolean.parseBoolean(obj.getString("Brush"));
		this.bls = Boolean.parseBoolean(obj.get("Bls").toString());
		this.als = Boolean.parseBoolean(obj.get("Als").toString());
		this.tbls = Boolean.parseBoolean(obj.get("tbls").toString());
		this.tals = Boolean.parseBoolean(obj.get("tals").toString());
		try{
		this.supv = Boolean.parseBoolean(obj.get("supv").toString());
		}
		catch(NullPointerException e){
		}
		try{
			this.appNum = obj.get("appNum").toString();
			this.appType = obj.getString("AppType");
		}
		catch(Exception e){
			this.appNum = "";
			if(engine && rescue){appType = "S";}
			else if(engine && ladder){appType = "L";}
			else if(engine){appType = "E";}
			else if(ladder){appType = "L";}
			else if(rescue){appType = "R";}
			else if(foam){appType = "F";}
			else if(brush){appType = "B";}
			else if(tanker){appType = "T";}
			else if(tbls){appType = "A";}
			else if(tals){appType = "M";}
			else if(supv){appType = "C";}
		}
		this.crew = obj.get("Crew").toString();
		this.wtank = obj.get("WTank").toString();
		this.ladSize = obj.get("LadSize").toString();
		this.ftank = obj.get("FTank").toString();
		try {
			this.stat = Status.getStatString(obj.get("Status").toString());
		} catch (JSONException e) {
			this.stat = null;
		}
	}
	
	public Apparatus(
			String oid, String unitnum,
			String unitYear, String unitMake, 
			String unitLocCoun, String unitLocMuni, String unitLocDist, 
			boolean engine, boolean ladder, boolean rescue, boolean tanker, boolean foam, 
			boolean bls, boolean als, boolean tbls, boolean tals,
			String crew, String wtank, String ladSize, String ftank, boolean supv) {
		super();
		this.setOid(oid);
		this.appType = unitnum.substring(0, 1);
		this.counNum = unitnum.substring(1, 3);
		this.muniNum = unitnum.substring(3, 5);
		try{
			this.appNum = unitnum.substring(5, 6);
		}
		catch(Exception e){
			this.appNum = "";
		}
		if(this.appType.equalsIgnoreCase("b")){
			this.brush = true;
		}
		else{
			this.brush = false;
		}
		this.unitYear = unitYear;
		this.unitMake = unitMake;
		this.unitLocCoun = unitLocCoun;
		this.unitLocMuni = unitLocMuni;
		this.unitLocDist = unitLocDist;
		this.engine = engine;
		this.ladder = ladder;
		this.rescue = rescue;
		this.tanker = tanker;
		this.foam = foam;
		this.bls = bls;
		this.als = als;
		this.tbls = tbls;
		this.tals = tals;
		this.crew = crew;
		this.wtank = wtank;
		this.ladSize = ladSize;
		this.ftank = ftank;
		this.supv = supv;
		stat = null;
	}

	public Apparatus() {
		// TODO Auto-generated constructor stub
		super();
		this.setOid(oid);
		this.appType = "*";
		this.counNum = "*";
		this.muniNum = "*";
		this.appNum = "*";
		this.unitYear = "";
		this.unitMake = "";
		this.unitLocCoun = "";
		this.unitLocMuni = "";
		this.unitLocDist = "";
		this.engine = false;
		this.ladder = false;
		this.rescue = false;
		this.tanker = false;
		this.foam = false;
		this.bls = false;
		this.als = false;
		this.tbls = false;
		this.tals = false;
		this.brush = false;
		this.crew = "";
		this.wtank = "";
		this.ladSize = "";
		this.ftank = "";
		this.supv = false;
		stat = null;
	}

	public Apparatus(String string) {
		// TODO Auto-generated constructor stub
		if(string.substring(0, 2).equals("PD")){
			this.appType = "*";
			this.counNum = "*";
			this.muniNum = "*";
		}
		else{
			this.appType = string.substring(0, 1);
			this.counNum = string.substring(1, 3);
			this.muniNum = string.substring(3, 5);
			try{
				this.appNum = string.substring(5, 6);
			}
			catch(Exception e){
				this.appNum = "";
			}
		}
		this.unitYear = "";
		this.unitMake = "";
		this.unitLocCoun = "";
		this.unitLocMuni = "";
		this.unitLocDist = "";
		this.engine = false;
		this.ladder = false;
		this.rescue = false;
		this.tanker = false;
		this.foam = false;
		this.bls = false;
		this.als = false;
		this.tbls = false;
		this.tals = false;
		this.brush = false;
		this.crew = "";
		this.wtank = "";
		this.ladSize = "";
		this.ftank = "";
		this.supv = false;
		stat = null;
	}

	public String getCrew() {
		return crew;
	}

	public String getWtank() {
		return wtank;
	}

	public String getFtank() {
		return ftank;
	}

	private String oid;
	private String appType;
	public String getAppType() {
		return appType;
	}
	public static Apparatus getApparatus(String oid) {
		Apparatus app = new Apparatus((BasicDBObject)
				Database.getCol("Apparatus", "info")
					    .findOne(new BasicDBObject("_id", new ObjectId(oid))));
		return app;
	}
	public static Apparatus findApp(String app){
		app = app.toUpperCase();
		BasicDBObject obj = new BasicDBObject("AppType", app.substring(0,1))
				.append("UnitCount", app.substring(1, 3))
				.append("UnitMunic", app.substring(3, 5));
		try {obj.append("appNum", app.substring(5, 6));} 
		catch (Exception e) {obj.append("appNum", "");}
		System.out.println(obj);
		Apparatus a = new Apparatus((BasicDBObject) Database.getCol("Apparatus", "info").findOne(obj));
		
		return a;
	}
	
	public void setSupv(boolean supv) {
		this.supv = supv;
	}

	public String getCounNum() {
		return counNum;
	}

	public String getAppNum() {
		return appNum;
	}
	
	public String getMuniNum() {
		return muniNum;
	}

	public String getUnitYear() {
		return unitYear;
	}

	public String getUnitMake() {
		return unitMake;
	}

	public String getUnitLocCoun() {
		return unitLocCoun;
	}

	public String getUnitLocMuni() {
		return unitLocMuni;
	}

	public String getUnitLocDist() {
		return unitLocDist;
	}

	public boolean isEngine() {
		return engine;
	}
	public boolean isBrush(){
		return brush;
	}

	public boolean isLadder() {
		return ladder;
	}

	public boolean isRescue() {
		return rescue;
	}

	public boolean isTanker() {
		return tanker;
	}

	public boolean isFoam() {
		return foam;
	}

	public boolean isBls() {
		return bls;
	}

	public boolean isAls() {
		return als;
	}

	public boolean isTbls() {
		return tbls;
	}

	public boolean isTals() {
		return tals;
	}

	public String getLadSize() {
		return ladSize;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getStat() {
		return stat;
	}
	public void setStat(String status){
		this.stat = status;
	}

	public boolean isSupv() {
		return supv;
	}
	
	public String getUnitString(){
		return appType+counNum+muniNum+appNum;
	}

	public static void changeLocation(String appUnit, String newLoc) {
		// TODO Auto-generated method stub
		BasicDBObject obj = new BasicDBObject("AppType", appUnit.substring(0,1))
				.append("UnitCount", appUnit.substring(1, 3))
				.append("UnitMunic", appUnit.substring(3, 5));
		try {obj.append("appNum", appUnit.substring(5, 6));} 
		catch (Exception e) {obj.append("appNum", "");}
		obj = (BasicDBObject) Database.getCol("Apparatus", "info")
				.findOne(obj);
		String[] str = newLoc.split("-");
		obj.put("UnitCounLoc", str[0]);
		obj.put("UnitMuniLoc", str[1]);
		obj.put("UnitDistLoc", str[2]);
		Database.getCol("Apparatus", "info").save(obj);
	}

	public void setEngine(boolean b) {
		// TODO Auto-generated method stub
		this.engine = b;
	}
	
	@Override
	public String toString() {
		return "Apparatus [oid=" + oid + ", apptype="+ appType + ", counNum=" + counNum + ", muniNum=" + muniNum + ", appNum="+appNum+ ", unitYear=" + unitYear + ", unitMake=" + unitMake + ", unitLocCoun="
				+ unitLocCoun + ", unitLocMuni=" + unitLocMuni + ", unitLocDist=" + unitLocDist + "]";
	}

	public String getCurCall() {
		// TODO Auto-generated method stub
		if(getStat().equalsIgnoreCase("ENRT") || 
				getStat().equalsIgnoreCase("ONLOC") || 
				getStat().equalsIgnoreCase("busy")){
			BasicDBObject obj = (BasicDBObject) Database.getCol("Apparatus", "info")
					.findOne(new BasicDBObject("AppType",this.appType)
							.append("UnitCount", this.counNum)
							.append("UnitMunic", this.muniNum)
							.append("appNum", this.appNum));
			String s = obj.getString("Status");
			
			Pattern p = Pattern.compile("\\d{2}-\\d{6}");
			Matcher m = p.matcher(s);
			
			Pattern p1 = Pattern.compile("\\bRADIOLOG.*.+?(?=\\bOPR)", Pattern.CASE_INSENSITIVE);
			Matcher m1 = p1.matcher(s);
			
			if(m.find()){
				return m.group();
			}
			else if(m1.find()){
				return m1.group();
			}
		}
		return "";
	}
}
