/**/
/** Database.java
 * 
 * @author Jason Kole
 * 
 * The database class contains basic functions to access the database stores
 * all the information within the system. this class contains test functions
 * and modification functions for database modification. 
 **/
/**/

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
import com.mongodb.MongoTimeoutException;

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
			e.printStackTrace();
		} catch (MongoTimeoutException e) {
			System.err.println("TEST");
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Database.connectionText()
	 * SYNOPSIS
	 * 		
	 * DESCRIPTION
	 * 		connects to the database and returns all databased within the server
	 * RETURNS
	 * 		System.out.println
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static void connectionTest(){
		List<String> dbs = client.getDatabaseNames();
		System.out.println(Arrays.toString(dbs.toArray())+" - CONNECTED");

	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Database.remove(String datbas, String collect, String id)
	 * SYNOPSIS
	 * 		String datbas ->	database to remove object from
	 * 		String collect -> 	collection to remov object from
	 * 		String id -> id of object to be removed
	 * DESCRIPTION
	 * 		removes object with id from table datbas.collect
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
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
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Database.update(String datbas, String collect, 
	 * 			BasicDBObject obj, String oid)
	 * SYNOPSIS
	 * 		String datbas ->	database to updated object from
	 * 		String collect -> 	collection to updated object from
	 * 		BasicDBObject obj ->	object that will be updated to
	 * 		String oid -> id of object to be updated
	 * DESCRIPTION
	 * 		updated the object with oid in datbas.collect to object obj
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
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
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Database.getCol(String datbas, String collect)
	 * SYNOPSIS
	 * 		String datbas ->	database to remove object from
	 * 		String collect -> 	collection to remov object from
	 * DESCRIPTION
	 * 		returns an instance of DBCollection to allow for other updated to occur
	 * RETURNS
	 * 		DBCollection 
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static DBCollection getCol(String db, String collect) {
		return client.getDB(db).getCollection(collect);		
	}
	public static DBCursor findAll(String db, String collect) {
		DBCollection col = getCol("Calls", "basicInfo");
		DBCursor cur = col.find();
		return cur;
	}
	public static void close() {
		client.close();
	}
}
