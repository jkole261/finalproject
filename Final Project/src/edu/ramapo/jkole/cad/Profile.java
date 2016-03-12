package edu.ramapo.jkole.cad;

public class Profile {
	private static String user;
	private static String agency = "13-12-1";
	private static int securlvl;
	private static int adminlvl;
	private static int editlvl;
	private static int querylvl;
	
	public Profile(){
		user = null;
		agency = "13-26-1";
		securlvl = 0;
		adminlvl = 0;
		editlvl = 0;
		querylvl = 0;
	}
	
	public Profile(String user, String agency, int securlvl, 
			int adminlvl, int editlvl, int querylvl){
		Profile.user = user;
		Profile.agency = agency;
		Profile.securlvl = securlvl;
		Profile.adminlvl = adminlvl;
		Profile.editlvl = editlvl;
		Profile.querylvl = querylvl;
	}
	
	public String getUser() {
		return user;
	}
	public static void setUser(String user) {
		Profile.user = user;
	}
	public String getAgency() {
		return agency;
	}
	public static void setAgency(String agency) {
		Profile.agency = agency;
	}
	public static int getSecurlvl() {
		return securlvl;
	}
	public static void setSecurlvl(int securlvl) {
		Profile.securlvl = securlvl;
	}
	public static int getAdminlvl() {
		return adminlvl;
	}
	public static void setAdminlvl(int adminlvl) {
		Profile.adminlvl = adminlvl;
	}
	public static int getEditlvl() {
		return editlvl;
	}
	public static void setEditlvl(int editlvl) {
		Profile.editlvl = editlvl;
	}
	public static int getQuerylvl() {
		return querylvl;
	}
	public static void setQuerylvl(int querylvl) {
		Profile.querylvl = querylvl;
	}
	public static String getTitle() {
		return title;
	}
	public static void setTitle(String title) {
		Profile.title = title;
	}
	private static String title;
}
