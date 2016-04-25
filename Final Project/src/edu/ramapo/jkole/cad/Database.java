package edu.ramapo.jkole.cad;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Database {
	public static MongoClient client;
	public static DB db;
	
	public static DB getDb() {
		return db;
	}
	public static void setDb(DB db) {
		Database.db = db;
	}
	public static void Connect(){
		try {
			client = new MongoClient("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void connectionTest(String DBName, String Collection){
		List<String> dbs = client.getDatabaseNames();
		System.out.println(Arrays.toString(dbs.toArray())+" - CONNECTED");

	}
	public static boolean remove(String datbas, String collect, String id) {
		DB db = client.getDB(datbas);
		DBCollection col = db.getCollection(collect);
		BasicDBObject doc = new BasicDBObject()
			.append("_id", new ObjectId(id));
		if(col.findAndRemove(doc) != null){
			return true;
		}
		else return false;
	}
	public static void update(String datbas, String collect, BasicDBObject obj, String oid) {
		DBCollection col = getCol(datbas, collect);	
		DBObject temp = col.findOne(new ObjectId(oid));
		col.findAndModify(temp, obj);
	}
	public static BasicDBObject find(String datbas, String collect, String oid) {
		DB db = client.getDB(datbas);
		DBCollection col = db.getCollection(collect);
		DBObject obj = col.findOne(new ObjectId(oid));
		return (BasicDBObject) obj;
	}
	public static void add(String datbas, String collect, BasicDBObject doc) {
		DB db = client.getDB(datbas);
		DBCollection col = db.getCollection(collect);
		col.insert(doc);
	}
	public static String get(String what, String key, String text, String dbase, String collect) {
		DB db = client.getDB(dbase);
		DBCollection col = db.getCollection(collect);
		BasicDBObject obj = new BasicDBObject(key, text);
		String temp = col.findOne(obj).get(what).toString();
		return temp;
	}
	public static DBCollection getCol(String db, String collect) {
		// TODO Auto-generated method stub
		return client.getDB(db).getCollection(collect);		
	}
	public static DBCursor findAll(String db, String collect) {
		// TODO Auto-generated method stub
		DBCollection col = getCol("Calls", "basicInfo");
		DBCursor cur = col.find();
		return cur;
	}
	public static void close() {
		// TODO Auto-generated method stub
		client.close();
	}
}
