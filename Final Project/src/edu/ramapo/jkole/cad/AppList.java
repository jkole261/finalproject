/**/
/** AppList.java
 * 
 * @author Jason Kole
 * 
 * AppList object the stores the apparatus and the purpose it is being dispatched for.
 **/
/**/
package edu.ramapo.jkole.cad;

public class AppList {

	public Apparatus app;
	public Apparatus getApp() {
		return app;
	}
	public void setApp(Apparatus app) {
		this.app = app;
	}
	public final String type;
	
	public AppList(String string, Apparatus selectUnit) {
		// TODO Auto-generated constructor stub
		this.app = selectUnit;
		this.type = string;
	}
	@Override
	public String toString(){	
		return type+": "+app.getAppType()+app.getCounNum()+app.getMuniNum()+app.getAppNum();
	}
}
