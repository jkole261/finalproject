package edu.ramapo.jkole.cad;

public class Municipality {
	
	private String id;
	private String municipality; 
	private String address;
	private String county;
	private String state;
	private String contactnum;
	private String countycode;
	private String municcode;
	
	public Municipality(String oid, String munic, String address, String county,
			String state, String contactnum, String countycode, String municcode) {
		this.id = oid;
		this.municipality = munic;
		this.address = address;
		this.county = county;
		this.state = state;
		this.contactnum = contactnum;
		this.countycode = countycode;
		this.municcode = municcode;
	}
	
	public Municipality() {
		// TODO Auto-generated constructor stub
	}
	
	public String getAddress() {
		return address;
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

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setContactnum(String contactnum) {
		this.contactnum = contactnum;
	}

	public void setCountycode(String countycode) {
		this.countycode = countycode;
	}

	public void setMuniccode(String municcode) {
		this.municcode = municcode;
	}

	public void setOid(String oid) {
		this.id = oid;
	}

	public String getOid() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public static String getCodeFromDB(String key, String text) {
		// TODO Auto-generated method stub
		return Database.get("MunicCode", key, text,"municipalities", "addresses");
	}

}
