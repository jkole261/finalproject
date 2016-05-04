/**/
/**
 * DispatchSeq.java
 * 
 * @author Jason Kole
 * 
 * the DispatchSeq class handles the sequence of the dispatch type
 * and parses it out into individual parts so an apparatatus with
 * the approiate functionality can respond to the incident.
 */
/**/
package edu.ramapo.jkole.cad;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;

public class DispatchSeq {
	String cadid;
	String[][] sequence;
	Call c;
	
	public DispatchSeq(String callid) {
		// TODO Auto-generated constructor stub
		c = new Call((BasicDBObject) Database.getCol("Calls", "basicInfo")
				.findOne(new BasicDBObject("cadid", callid)));
		sequence = getSequence(c.getCall().get("type"));
	}
	
	public DispatchSeq(String callid, String seq) {
		// TODO Auto-generated constructor stub
		c = new Call((BasicDBObject) Database.getCol("Calls", "basicInfo")
				.findOne(new BasicDBObject("cadid", callid)));
		sequence = getSequence(seq);
	}
	
	public String[][] getApp(){
		return sequence;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.DispatchSeq.getSequence(String type)
	 * SYNOPSIS
	 * 		String type -> the dispatch type that has been selected
	 * DESCRIPTION
	 * 		gets the dispatch sequence of the specified response type
	 * RETURNS
	 * 		String[][] str = a 2D array with the number of apparatus 
	 * 		and each type to be dispatched
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private String[][] getSequence(String type) {
		String[][] str = new String[20][2];
		String[] st;
		FileReader fileReader;
		BufferedReader buffReader;
		String line = "Police Local:: ^PD"; 
		try {
			fileReader = new FileReader("lib/disptypes.dat");
			buffReader = new BufferedReader(fileReader);
			line = buffReader.readLine();
			String sub = line.substring(0, line.indexOf("::"));
			while (!(sub.equalsIgnoreCase(type))){
				line = buffReader.readLine();
				sub = line.substring(0, line.indexOf("::"));
			}
			buffReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		line = line.substring(line.indexOf(":: ")+3);
		st = line.split(",");
		for(int i = 0; i < st.length; i++){
			//check for ^ if so notify only
			st[i] = st[i].trim();
			Pattern p = Pattern.compile("\\^");
			Matcher m = p.matcher(st[i]);
			if(!(m.find())){
				str[i][0] = Integer.parseInt(st[i].replaceAll("[\\D]", ""))+"";
				str[i][1] = st[i].replaceAll("[0-9]", "").trim();
			}
			else{
				str[i][0] = "*";
				str[i][1] = st[i].replaceAll("[0-9]", "").trim().substring(1);
			}
		}
		return str;
	}
}