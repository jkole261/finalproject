/**/
/**
 * Nature.java
 * 
 * @author Jason Kole
 * 
 * Nature object contains categories that call types and descriptions. there also is
 * a dispatch type to determine what kind of response there will be as well as a 
 * priority for the call. 
 */
/**/
package edu.ramapo.jkole.cad;

public class Nature extends Object{
	private String nature;
	private String disptype;
	private String priority;
	
	public Nature(){
		
	}
	public Nature(String nature, String disp, String pri){
		this.nature = nature.toUpperCase();
		this.disptype = disp;
		this.priority = pri;
	}
	
	public Nature(String string) {
		// TODO Auto-generated constructor stub
		this.nature = string.toUpperCase();
	}

	@Override
	public String toString(){
		return nature;
	}
	public String getNature() {
		return nature;
	}
	public String getDisptype() {
		return disptype;
	}
	public String getPriority() {
		return priority;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public void setDisptype(String disptype) {
		this.disptype = disptype;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int compare(Nature o1, Nature o2) {
		// TODO Auto-generated method stub
		return o1.getNature().compareTo(o2.getNature());
	}
}
