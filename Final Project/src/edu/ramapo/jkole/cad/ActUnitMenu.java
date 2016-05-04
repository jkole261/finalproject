/**/
/** ActUnitMenu.java
 * 
 * @author Jason Kole
 * 
 * The ActUnitMenu, or Active Units Menu is a javaFX application that when started
 * will display all current apparatus that are in the system and their locations
 * at the current time. unit locations will be able to be changed within this screen.
 **/
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ActUnitMenu extends Application{
	public static Stage stage;
	TableView<Apparatus> table;
	
	public ActUnitMenu(){
		try{
			if(stage.isShowing()){return;}
			else if(!(stage.isShowing())){
				stage.show();
			}
		}
		catch(NullPointerException e2){
			try {
				stage = new Stage();
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActUnitMenu.start()
	 * SYNOPSIS
	 * 		this.ActUnitMenu	->	this JavaFX Application 
	 * DESCRIPTION
	 * 		starts and creates the visual application of the ActiveUnitMenu within this
	 * 		menu there is a thread that runs and looks for new information. within this 
	 * 		function the thread every 10 seconds looks for new apparatus that have been 
	 * 		added or had a status update since the last refresh.
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 600, 400, Color.ANTIQUEWHITE);
		stage.setTitle("Active Units");
		try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		table = new TableView<Apparatus>();	
		VBox mbox = new VBox();
		
		TableColumn<Apparatus, String> unit = new TableColumn<Apparatus, String>("Unit");
		TableColumn<Apparatus, String> loc = new TableColumn<Apparatus, String>("Location");
		TableColumn<Apparatus, String> stat = new TableColumn<Apparatus, String>("Status");
		TableColumn<Apparatus, String> curcall = new TableColumn<Apparatus, String>("Current Call");
		TableColumn<Apparatus, String> timeelap = new TableColumn<Apparatus, String>("Time Elapsed");
		
		table.getColumns().addAll(unit, loc, stat, curcall, timeelap);
		 

		table.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
			@Override
			  public Boolean call(ResizeFeatures param) {
			     return true;
			  }
			});
		
		MenuItem showCall = new MenuItem("_Show Call");
		showCall.setMnemonicParsing(true);
		MenuItem changeLoc = new MenuItem("_Change Loc");
		changeLoc.setMnemonicParsing(true);
		MenuItem rLog = new MenuItem("_Radio Log");
		rLog.setMnemonicParsing(true);
		MenuItem appMenu = new MenuItem("View Apparatus");
		appMenu.setMnemonicParsing(true);
		Menu status = new Menu("_Set Status");
		MenuItem enrt = new MenuItem("_ENRT");
		enrt.setMnemonicParsing(true);
		MenuItem onloc = new MenuItem("_ONLOC");
		onloc.setMnemonicParsing(true);
		MenuItem avail = new MenuItem("_AVAIL");
		avail.setMnemonicParsing(true);
		MenuItem oos = new MenuItem("_OOS");
		oos.setMnemonicParsing(true);
		MenuItem busy = new MenuItem("_BUSY");
		busy.setMnemonicParsing(true);
		
		status.getItems().addAll(enrt, onloc, avail, oos, busy);
		
		showCall.setOnAction(actionEvent -> {
		        Apparatus item = table.getSelectionModel().getSelectedItem();
		        if (item != null){ 
		            new FireCallScreen(item.getCurCall());
		        }
		});
		
		appMenu.setOnAction(actionEvent -> {
			try {
				AppMenu apm = new AppMenu();
				apm.table.getSelectionModel().select(table.getSelectionModel().getSelectedItem());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		changeLoc.setOnAction(actionEvent -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Change Unit Location");
			dialog.setHeaderText("Enter new Location for\n"+table.getSelectionModel().getSelectedItem().getUnitString());
			dialog.setContentText("Station:");

			Optional<String> result = dialog.showAndWait();
			result.ifPresent(com -> {
				Apparatus.changeLocation(table
						.getSelectionModel().getSelectedItem().getUnitString(), com);
			});	
		});
		
		rLog.setOnAction(actionEvent -> CallTakerScreen.createRadioLog(
				table.getSelectionModel().getSelectedItem().getUnitString()));
		
		enrt.setOnAction(actionEvent -> {
			if(!(table.getSelectionModel().getSelectedItem().getCurCall().equalsIgnoreCase(""))){
				Pattern p = Pattern.compile("\\d{2}-\\d{6}");
				Matcher m = p.matcher(table.getSelectionModel().getSelectedItem().getCurCall());
				if(m.find()){
					ApparatusDispatch.enrtApp(m.group(), table.getSelectionModel().getSelectedItem().getUnitString());
				}
			}
		});
		
		onloc.setOnAction(actionEvent -> {
			if(!(table.getSelectionModel().getSelectedItem().getCurCall().equalsIgnoreCase(""))){
				Pattern p = Pattern.compile("\\d{2}-\\d{6}");
				Matcher m = p.matcher(table.getSelectionModel().getSelectedItem().getCurCall());
				if(m.find()){
					ApparatusDispatch.arvdApp(m.group(), table.getSelectionModel().getSelectedItem().getUnitString());
				}
			}
		});
		
		avail.setOnAction(actionEvent -> {
			ApparatusDispatch.setAvail(table.getSelectionModel().getSelectedItem().getUnitString());
		});
		
		oos.setOnAction(actionEvent -> ApparatusDispatch
				.setOss(table.getSelectionModel().getSelectedItem().getUnitString()));
		
		busy.setOnAction(actionEvent -> CallTakerScreen.createRadioLog(
				table.getSelectionModel().getSelectedItem().getUnitString()));
		
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
		
		table.setContextMenu(new ContextMenu(showCall, appMenu, changeLoc, rLog, status));
		
		new Thread(new Runnable() { 
			public void run() { 
				do { 
					try{
						int a = table.getSelectionModel().getSelectedIndex();
						table.setItems(check(table.getItems()));
						table.getSelectionModel().select(a);
						Thread.sleep(8000);
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
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActUnitMenu.getElapsed(Apparatus app)
	 * SYNOPSIS
	 * 		Apparatus app	-> gets string of time of last update
	 * DESCRIPTION
	 * 		searches the database for the apparatus app and gets the 
	 * 		string of the timestamp from the last status update. this 
	 * 		is then sent to getElapsedTime(String string) which parses 
	 * 		it into time format.
	 * RETURNS
	 * 		getElapsedTime(String string)	->	parses string into time 
	 * 			format then gets elapsed time starting at string.
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	protected String getElapsed(Apparatus app) {
		// TODO Auto-generated method stub
		BasicDBObject obj = (BasicDBObject) Database.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("_id", new ObjectId(app.getOid())));
		String s = obj.getString("Status");
		s = s.substring(s.indexOf("TimeStamp")+14, s.indexOf("TimeStamp")+32);
		return getElapsedTime(s);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActUnitMenu.getElapsedTime(String string)
	 * SYNOPSIS
	 * 		String string	->	time of last update in string format
	 * DESCRIPTION
	 * 		parses the string into year, month, day, hours, minutes, and seconds
	 * 		subtracts time since the current time and returns and elapsed time
	 * RETURNS
	 * 		String currentTime	->	current time in string format
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
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
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActUnitMenu.check()
	 * SYNOPSIS
	 * 		ObservableList<Apparatus> items	->	current list of Apparatus
	 * DESCRIPTION
	 * 		every call of this function checks the size of items against the
	 * 		size of the database. if the sizes are different then it will set
	 * 		the current ObservableList to what is currently in the database.
	 * 		if the list is the same size then the function returns null else 
	 * 		will return the new list.
	 * RETURNS
	 * 		ObservableList<Apparatus> items; null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
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
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActUnitMenu.getApps(int i)
	 * SYNOPSIS
	 * 		int i	->	gets the i'th apparatus in the database collection
	 * DESCRIPTION
	 * 		gets the collection of all apparatus within the database and retrieves 
	 * 		the number i apparatus.
	 * RETURNS
	 * 		Apparatus temp	->	number i apparatus
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private Apparatus getApps(int i) {
		DBCollection coll = Database.getCol("Apparatus", "info");
		List<DBObject> foundDocument = coll.find(new BasicDBObject("Status.active", true)).toArray();
		Apparatus temp = new Apparatus((BasicDBObject) foundDocument.get(i));
		return temp;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActUnitMenu.getApps()
	 * SYNOPSIS
	 * 		ObservableList<Apparatus> dat	->	list that will contain all 
	 * 												current Apparatus.
	 * 		DBCollection Coll				->	collection that gets all information 
	 * 												from the database.
	 * DESCRIPTION
	 * 		On function calls this searches the database of apparatus and adds them to dat
	 * 		means they are active.
	 * RETURNS
	 * 		ObservableList<Apparatus> dat	->	observable list containing all Apparatus
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
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