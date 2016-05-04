/**/
/**
 * StationAlert.java
 * 
 * @author Jason Kole
 * 
 * StationAlert is recieved from the server and displayed 
 * within a dialog box on the console. WARNING this currently
 * is not working. 
 */
/**/

package edu.ramapo.jkole.cad;

public class StationAlert {
	 
	public static void checkAlert(Apparatus apps) {
		String loc = apps.getUnitLocCoun()+"-"+apps.getUnitLocMuni()+"-"+apps.getUnitLocDist();
		System.out.println(apps.getUnitString()+" as "+loc+" CHECKED against "+Main.pro.getAgency());
		if(loc.equalsIgnoreCase(Main.pro.getAgency())){
			System.out.println("ALERT");
		}
	}
	
}
