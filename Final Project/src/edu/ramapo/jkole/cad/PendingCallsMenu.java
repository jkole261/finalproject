/**/
/**
 * PendingCallsMenu.java
 * 
 * @author Jason Kole
 * 
 * the PendingCallMenu is a javaFX appliation that displays all 
 * calls that still have to be dispatched. once a call is dispatched
 * it will no longer be displayed within this table. thread runs
 * every 10 seconds to retrieve new calls.
 */
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PendingCallsMenu extends Application {
	static Stage stage;
	static TableView<Call> table;
	
	public PendingCallsMenu(){
		try{
			if(!(stage.isShowing())){return;}
			else{
				stage.show();
			}
		}
		catch(NullPointerException e2){
			stage = new Stage();
			table = new TableView<Call>();
			try {
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	@Override
	public void start(Stage stage) throws Exception {
		BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 500, 400, Color.ANTIQUEWHITE);
    	try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
    	MenuBar menu = new MenuBar();
    	
    	Menu file = new Menu("_File");
    	Menu edit = new Menu("_Edit");
    	Menu cad = new Menu("_CAD");
    	Menu help = new Menu("_Help");
    	
    	menu.getMenus().addAll(file, edit, cad, help);
    	
    	root.setTop(menu);
    	root.setCenter(getCenter());
    	
		stage.setTitle("Pending Calls"); 
	    stage.setScene(scene);
	    stage.setMaxWidth(GraphicsEnvironment
	    		.getLocalGraphicsEnvironment().getMaximumWindowBounds().width);
	    stage.sizeToScene(); 
	    stage.show(); 	
	}

	@SuppressWarnings("unchecked")
	private Node getCenter() {
		VBox vbox = new VBox();
		
		TableColumn<Call, String> actid = new TableColumn<Call, String>("ACT ID");
		TableColumn<Call, String> addr = new TableColumn<Call, String>("ADDRESS");
		TableColumn<Call, String> city = new TableColumn<Call, String>("CITY");
		TableColumn<Call, String> nature = new TableColumn<Call, String>("NATURE");
		TableColumn<Call, String> pri = new TableColumn<Call, String>("PRIORITY");
		TableColumn<Call, String> disptime = new TableColumn<Call, String>("DISPATCH TIME");
		TableColumn<Call, String> elaptime = new TableColumn<Call, String>("ELAPSED TIME");
		
		actid.setCellValueFactory(c -> new SimpleStringProperty(
				c.getValue().getCall().get("actid").toString()));
		addr.setCellValueFactory(c -> new SimpleStringProperty(
				c.getValue().getCall().get("addr").toString().split(",")[0].trim()));
		city.setCellValueFactory(c -> new SimpleStringProperty(
				c.getValue().getCall().get("city").toString()));
		nature.setCellValueFactory(c -> new SimpleStringProperty(
				c.getValue().getCall().get("nature").toString()));
		pri.setCellValueFactory(c -> new SimpleStringProperty(
				c.getValue().getCall().get("pri").toString()));
		disptime.setCellValueFactory(c -> new SimpleStringProperty(
				c.getValue().getCall().get("CallTime").toString().split(" ")[1].trim()));
		elaptime.setCellValueFactory(c -> new SimpleStringProperty(
				getElapsedTime(c.getValue().getCall().get("CallTime").toString())));
		
		table.getColumns().addAll(actid, addr, city, nature, pri, disptime, elaptime);
		
		table.setRowFactory( tv -> {
            TableRow<Call> row = new TableRow<Call>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	try{
                		new FireCallScreen(row.getItem().getCall().get("cadid"));
                	}
                	catch(NullPointerException e){ }
                }
            });
            return row;
		});
		
		new Thread(new Runnable() 
	    { 
	      public void run() 
	      { 
	        do { 
	          try{
	        	  table.getItems().clear();
	        	  table.setItems(getCalls());
	        	  Thread.sleep(10000);
	          } // 10 second pause 
	          catch(Exception e){
	        	System.err.print(e.getStackTrace());
	          } 
	        } 
	        while(stage.isShowing());
	      }
	    }).start();
		
		vbox.getChildren().add(table);	
		return vbox;
	}

	private String getElapsedTime(String string) {
		// TODO Auto-generated method stub
		int yr = 0, day = 0, month = 0, hr = 0, min = 0, sec = 0;
		yr = Integer.parseInt(string.substring(0, 4));
		month = Integer.parseInt(string.substring(5, 7));
		day = Integer.parseInt(string.substring(8, 10));
		hr = Integer.parseInt(string.substring(11, 13));
		min = Integer.parseInt(string.substring(14, 16));
		sec = Integer.parseInt(string.substring(17,19));
		
		//Java Calendar 0-11. decrement by one to convert
		month--;
		
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(yr, month, day, hr, min, sec);
		Date d = cal.getTime();
		Date d1 = cal2.getTime();

		hr = (int)((d1.getTime()-d.getTime())/(1000*60*60));
		min = (int)((d1.getTime()-d.getTime()) - (hr*60*60*1000))/(1000*60);
		sec = (int)((d1.getTime()-d.getTime()) - ((hr*60*60*1000) + (min*60*1000)))/(1000);		
		
		String s = "";
		if(hr > 0){
			s = "HR: "+hr+" ";
		}
		if(min > 0){
			s += "MIN: "+min+" ";
		}
		if(sec > 0){
			s += "SEC: "+sec;
		}
		return s;
	}

	private ObservableList<Call> getCalls() {
		ObservableList<Call> dat = FXCollections.observableArrayList(); 
		DBCollection coll = Database.getCol("Calls", "status");
		DBCollection callcol = Database.getCol("Calls", "basicInfo");
		List<DBObject> foundDocument = coll.find(new BasicDBObject("Status", "RCVD")).toArray();
		for(int i = 0; i < foundDocument.size(); i++){
			Call temp = new Call((BasicDBObject) callcol.findOne(
				new BasicDBObject("cadid", foundDocument.get(i).get("CallId").toString())));
			dat.add(temp);
		}
		System.out.println(dat.size());
		return dat;
	}
	
}
