/**/
/**
 * Profile.java
 * 
 * @author Jason Kole
 * 
 * the profile object stores all information for the specific users, including
 * username, agency, and security levels within the programs. as of now these
 * cannot be changed within the program. 
 */
/**/
package edu.ramapo.jkole.cad;

public class Profile {
	private  String user;
	private  String agency;
	private  int securlvl;
	private  int adminlvl;
	private  int editlvl;
	private  int querylvl;
	
	public Profile(){ 
		user = null;
		agency = null;
		securlvl = 0;
		adminlvl = 0;
		editlvl = 0;
		querylvl = 0;
	}
	
	public Profile(String user, String agency, int securlvl, 
			int adminlvl, int editlvl, int querylvl){
		this.user = user;
		this.agency = agency;
		this.securlvl = securlvl;
		this.adminlvl = adminlvl;
		this.editlvl = editlvl;
		this.querylvl = querylvl;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public int getSecurlvl() {
		return securlvl;
	}
	public void setSecurlvl(int securlvl) {
		this.securlvl = securlvl;
	}
	public int getAdminlvl() {
		return adminlvl;
	}
	public void setAdminlvl(int adminlvl) {
		this.adminlvl = adminlvl;
	}
	public int getEditlvl() {
		return editlvl;
	}
	public void setEditlvl(int editlvl) {
		this.editlvl = editlvl;
	}
	public int getQuerylvl() {
		return querylvl;
	}
	public void setQuerylvl(int querylvl) {
		this.querylvl = querylvl;
	}
}
