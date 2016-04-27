/**/
/**
 * Station.java
 * 
 * @author Jason Kole
 * 
 * the Station object is a specific police department, fire department,
 * or first aid squad within a municipality each station contains 
 * apparatus. 
 */
/**/
package edu.ramapo.jkole.cad;

import com.mongodb.DBObject;

public class Station {
	private  String oid ;
	private  String name ;
	private  String address;
	private  String municipality;
	private  String county;
	private  String state;
	private  String contactnum;
	private  String countycode;
	private  String municcode;
	private  String district;
	
	 Station(String id, String name, String address, String municipality, String county,
			 String state, String contactnum, String countycode, String municcode, String district) {
	        this.oid = 			id;
		 	this.name = 		name;
	        this.address = 		address;
	        this.municipality = municipality;
	        this.county = 		county;
	        this.state = 		state;
	        this.contactnum = 	contactnum;
	        this.countycode = 	countycode;
	        this.municcode = 	municcode;
	        this.district = 	district;
	    }

	public Station(DBObject dbObject) {
		// TODO Auto-generated constructor stub
		this.oid = 			dbObject.get("_id").toString();
	 	this.name = 		dbObject.get("Name").toString();
        this.address = 		dbObject.get("Address").toString();
        this.municipality = dbObject.get("Municipality").toString();
        this.county = 		dbObject.get("County").toString();
        this.state = 		dbObject.get("State").toString();
        this.contactnum = 	dbObject.get("ContactNum").toString();
        this.countycode = 	dbObject.get("CountyCode").toString();
        this.municcode = 	dbObject.get("MunicCode").toString();
        this.district = 	dbObject.get("District").toString();
	}

	public Station() {
		
	}

	@Override
	public String toString() {
		return "Station [oid=" + oid + ", name=" + name + ", address=" + address + ", municipality=" + municipality
				+ ", county=" + county + ", state=" + state + ", contactnum=" + contactnum + ", countycode="
				+ countycode + ", municcode=" + municcode + ", district=" + district + "]";
	}

	public String getCounty() {
		return county;
	}

	public String getState() {
		return state;
	}

	public String getContactnum() {
		return contactnum;
	}

	public String getCountycode() {
		return countycode;
	}

	public String getMuniccode() {
		return municcode;
	}

	public String getDistrict() {
		return district;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getMunicipality() {
		return municipality;
	}
	public void setName(String Name) {
		name = (Name);
	}
	public void setMunicipality(String Munic){
		municipality = (Munic);
	}
	public void setAddress(String Address){
		address = Address;
	}
	public void setState(String State){
		state = (State);
	}

	public String getOid() {
		return oid;
	}
}
