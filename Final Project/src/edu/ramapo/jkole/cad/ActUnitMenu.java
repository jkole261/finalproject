package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ActUnitMenu extends Application{
	Stage stage;
	TableView<Apparatus> table;
	
	public ActUnitMenu(){
		
		try {
			stage = new Stage();
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 600, 400, Color.ANTIQUEWHITE);
		stage.setTitle("Active Units");
		
		table = new TableView<Apparatus>();	
		VBox mbox = new VBox();
		
		TableColumn<Apparatus, String> unit = new TableColumn<Apparatus, String>("Unit");
		TableColumn<Apparatus, String> loc = new TableColumn<Apparatus, String>("Location");
		TableColumn<Apparatus, String> stat = new TableColumn<Apparatus, String>("Status");
		TableColumn<Apparatus, String> curcall = new TableColumn<Apparatus, String>("Current Call");
		TableColumn<Apparatus, String> timeelap = new TableColumn<Apparatus, String>("Time Elapsed");
		
		table.getColumns().addAll(unit, loc, stat, curcall, timeelap);

		table.setRowFactory( tv -> {
            TableRow<Apparatus> row = new TableRow<Apparatus>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	try{
                		new FireCallScreen(row.getItem().getCurCall());
                	}
                	catch(NullPointerException e){
                		System.out.println("no call");
                	}
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
	        	  int a = table.getSelectionModel().getSelectedIndex();
	        	  table.setItems(check(table.getItems()));
	        	  table.getSelectionModel().select(a);
	        	  Thread.sleep(10000);
	          }  
	          catch(IllegalStateException | InterruptedException e){
	        	  System.err.println("ERROR ON ACTIVE UNIT THREAD");
	          } 
	        } 
	        while(stage.isShowing());
	      }
	    }).start();
			
		unit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Apparatus, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Apparatus, String> param) {
                return new SimpleStringProperty(param.getValue().getUnitString());
            }
        });
		loc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Apparatus, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Apparatus, String> param) {
                return new SimpleStringProperty(param.getValue().getUnitLocCoun()+"-"+param.getValue().getUnitLocMuni()+"-"+param.getValue().getUnitLocDist());
            }
        });
		stat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Apparatus, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Apparatus, String> param) {
                return new SimpleStringProperty( param.getValue().getStat());
            }
        });
		curcall.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Apparatus, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Apparatus, String> param) {
                return new SimpleStringProperty( param.getValue().getCurCall() );
            }
        });
		timeelap.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Apparatus, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Apparatus, String> param) {
                return new SimpleStringProperty( getElapsed(param.getValue() ));
            }
        });
		
		mbox.getChildren().add(table);
		root.setCenter(mbox);
	    stage.setScene(scene);
	    stage.setMaxWidth(GraphicsEnvironment
       		.getLocalGraphicsEnvironment()	
       		.getMaximumWindowBounds().width);
	    stage.sizeToScene(); 
	    stage.show();
	}
	protected String getElapsed(Apparatus app) {
		// TODO Auto-generated method stub
		BasicDBObject obj = (BasicDBObject) Database.getCol("Apparatus", "info").findOne(new BasicDBObject("_id", new ObjectId(app.getOid())));
		String s = obj.getString("Status");
		s = s.substring(s.indexOf("TimeStamp")+14, s.indexOf("TimeStamp")+32);
			
		return getElapsedTime(s);
	}
	
	private String getElapsedTime(String string) {
		// TODO Auto-generated method stub
		int yr = 0, day = 0, month = 0, hr = 0, min = 0, sec = 0;
		yr = Integer.parseInt(string.substring(0, 4));
		month = Integer.parseInt(string.substring(4, 6));
		day = Integer.parseInt(string.substring(6, 8));
		hr = Integer.parseInt(string.substring(9, 11));
		min = Integer.parseInt(string.substring(11, 13));
		sec = Integer.parseInt(string.substring(13, 15));
		
		month--;
		
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(yr, month, day, hr, min, sec);
		Date d = cal.getTime();
		Date d1 = cal2.getTime();
	
		hr =  (int)((d1.getTime()-d.getTime())/ (1000*60*60));
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
	
	private ObservableList<Apparatus> check(ObservableList<Apparatus> items) {
		if(items.isEmpty()){
			items = getApps();
		}
		else{
			ObservableList<Apparatus> as = getApps();
			if(items.size() == as.size()){
				int i = 0;
				for(Apparatus a : items){
					if(!( a.equals(as.get(i)) )){ //if app is unchanged		
						items.set(i, getApps(i));
					}
					i++;
				}
				return items;	
			}
			else{
				items = getApps();
			}
		}
		return items;
	}
	private Apparatus getApps(int i) {
		DBCollection coll = Database.getCol("Apparatus", "info");
		List<DBObject> foundDocument = coll.find(new BasicDBObject("Status.active", true)).toArray();
		Apparatus temp = new Apparatus((BasicDBObject) foundDocument.get(i));
		return temp;
	}

	private ObservableList<Apparatus> getApps() {
		ObservableList<Apparatus> apps = FXCollections.observableArrayList(); 
		DBCollection coll = Database.getCol("Apparatus", "info");
		List<DBObject> foundDocument = coll.find(new BasicDBObject("Status.active", true)).toArray();
		for(int i = 0; i < foundDocument.size(); i++){
			Apparatus temp = new Apparatus((BasicDBObject) foundDocument.get(i));
			apps.add(temp);
		}
		return apps;
	} 
}