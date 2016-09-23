/**/
/** AppMenu.java
 * 
 * @author Jason Kole
 * 
 * AppMenu class is a JavaFX application that displays all apparatus within the system
 * for all stations. within this menu all apparatus will be able to be modified as well
 * as added to the dispatch system. 
 **/
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.JSONException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AppMenu extends Application {
	protected static TableView<Apparatus> table;
	private TableView<Station> loc; 
	private TabPane tabPane = new TabPane();
	private static TableView<Apparatus> stattable;
	static Stage stage;
	
	public AppMenu() throws Exception{
		try{
			if(stage.isShowing()){
				return;
			}
			else {
				System.out.println("ELSE");
				stage = new Stage();
				table = new TableView<Apparatus>();
				loc = new TableView<Station>();
				loc.setMaxHeight(200);
				try {
					start(stage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch(NullPointerException e2){
			stage = new Stage();
			table = new TableView<Apparatus>();
			loc = new TableView<Station>();
			loc.setMaxHeight(200);
			try {
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.getAppStation()
	 * SYNOPSIS
	 * 		Station dat -> emergency services station that will recieve units
	 * DESCRIPTION
	 * 		generates and populates a table for all apparatus within this station
	 * RETURNS
	 * 		TableView<Apparatus> -> table of all apparatus within the station
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Node getAppStation(Station dat) {
		stattable = new TableView<Apparatus>();
		VBox temp = new VBox();

		temp.setSpacing(10);
		stattable.setMaxHeight(200);
		stattable.getItems().clear();
		temp.setPadding(new Insets(5, 10, 20, 10));
		temp.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
		
        TableColumn appUnit = new TableColumn("Unit");
        TableColumn appYr = new TableColumn("App\nYear");
        TableColumn appMa = new TableColumn("App\nMake");
        TableColumn engine = new TableColumn("Eng");
        TableColumn ladder = new TableColumn("Lad");
        TableColumn rescue = new TableColumn("Res");
        TableColumn tank = new TableColumn("Tank");
        TableColumn foam = new TableColumn("Foam");
        TableColumn bls = new TableColumn("BLS");
        TableColumn als = new TableColumn("ALS");
        TableColumn tbls = new TableColumn("Trans\nBLS");
        TableColumn tals = new TableColumn("Trans\nALS");
        TableColumn wtank = new TableColumn("Water\nTank");
        TableColumn staff = new TableColumn("Crew");
        TableColumn ladsize = new TableColumn("Ladder\nSize");
        TableColumn ftank = new TableColumn("Foam\nTank");
        TableColumn stat = new TableColumn("Status");

        appUnit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Apparatus, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Apparatus, String> param) {
                return new SimpleStringProperty(
                		param.getValue().getAppType()+
                		param.getValue().getCounNum()+
                		param.getValue().getMuniNum()+
                		param.getValue().getAppNum());
            }
        });
        appYr.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("unitYear"));
        appMa.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("unitMake"));
        engine.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("engine"));
        ladder.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("ladder"));
        rescue.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("rescue"));
        tank.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("tanker"));
        foam.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("foam"));
        bls.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("bls"));
        als.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("als"));
        tbls.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("tbls"));
        tals.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("tals"));
        staff.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("crew"));
        wtank.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("wtank"));
        ladsize.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("ladSize"));
        ftank.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("ftank"));
        stat.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("stat"));
        
        stattable.setRowFactory( tv -> {
            TableRow<Apparatus> row = new TableRow<Apparatus>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                   try{
                	   new FireCallScreen(row.getItem().getCurCall());
                   }
                   catch(NullPointerException e){}
                }
            });
            return row ;
        });
      	
		ObservableList<Apparatus> oblist = FXCollections.observableArrayList(
				new Apparatus()); 
		Database.setDb(Database.client.getDB("Apparatus"));
		DBCollection coll = Database.db.getCollection("info");
		
		BasicDBObject tobj = new BasicDBObject("UnitCounLoc", dat.getCountycode());
			tobj.append("UnitMuniLoc", dat.getMuniccode());
			tobj.append("UnitDistLoc", dat.getDistrict());
			
		List<DBObject> foundDocument = coll.find(tobj).toArray();
		oblist.remove(0);
		
		for(int i = 0; i < foundDocument.size(); i++){
			BasicDBObject obj = (BasicDBObject) foundDocument.get(i);
			Apparatus app = new Apparatus(obj);
			try {
				app.setStat(Status.getStatString(obj.getString("Status")));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			oblist.add(app);
		}
	    stattable.setItems( oblist );
	    stattable.getColumns().addAll(
	    		appUnit, appYr, appMa, 
	    		engine, ladder, rescue, tank, foam, bls, als, tbls, tals, 
	    		staff, wtank, ladsize, ftank, stat);
	    
	    temp.getChildren().add(stattable);
	    
	    return temp;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.start(Stage stage)
	 * SYNOPSIS
	 * 		Stage stage -> the main stage for this class
	 * DESCRIPTION
	 * 		generates a GUI for users to see and edit all apparatus within the menu
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage stage) throws Exception {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1000, 600, Color.ANTIQUEWHITE);
		try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		MenuBar mainMenu = new MenuBar();  
		final VBox vbox = new VBox();
		
		Menu file = new Menu("_File");
		
		MenuItem addApparatus = new MenuItem("_Add Apparatus");
		addApparatus.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		MenuItem adminmode = new MenuItem("_Enable Admin");
		adminmode.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
		MenuItem exit = new MenuItem("_Exit Window");
		exit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		
		if(Main.isAdmin()) { file.getItems().add(addApparatus); }
		
		file.getItems().add(adminmode);
		file.getItems().add(exit);
		
		mainMenu.getMenus().add(file);
		
		addApparatus.setOnAction(actionEvent -> addApparatus());
	//	adminmode.setOnAction(actionEvent -> changeAdmin());
		exit.setOnAction(actionEvent -> stage.close());
		
		root.setCenter(vbox);
		root.setBottom(tabPane);
		root.setTop(mainMenu);
		
		Label label = new Label("Apparatus");
		
		TableColumn name = new TableColumn("Station");
        TableColumn address = new TableColumn("Address");
        TableColumn municipal = new TableColumn("Municipality");
        TableColumn county = new TableColumn("County");
        TableColumn state = new TableColumn("ST");
        TableColumn cont = new TableColumn("Contact #");
        TableColumn ccode = new TableColumn("C #");
        TableColumn mcode = new TableColumn("M #");
        TableColumn dcode = new TableColumn("D #");
        TableColumn id = new TableColumn("id");
       
        name.setCellValueFactory(
                new PropertyValueFactory<Station, String>("Name"));
        address.setCellValueFactory(
                new PropertyValueFactory<Station, String>("Address"));
        municipal.setCellValueFactory(
                new PropertyValueFactory<Station, String>("Municipality"));
        county.setCellValueFactory(
        		new PropertyValueFactory<Station, String>("county"));
        state.setCellValueFactory(
        		new PropertyValueFactory<Station, String>("State"));
        cont.setCellValueFactory(
        		new PropertyValueFactory<Station, String>("contactnum"));
        ccode.setCellValueFactory(
        		new PropertyValueFactory<Station, String>("countycode"));
        mcode.setCellValueFactory(
        		new PropertyValueFactory<Station, String>("municcode"));
        dcode.setCellValueFactory(
        		new PropertyValueFactory<Station, String>("District"));
        id.setCellValueFactory(
        		new PropertyValueFactory<Station, String>("oid")); 
      
        loc.getColumns().addAll(
        		name, address, municipal, county, state, cont, ccode, mcode, dcode);
        
        TableColumn appUnit = new TableColumn("Unit");
        TableColumn appYr = new TableColumn("App\nYear");
        TableColumn appMa = new TableColumn("App\nMake");
        TableColumn engine = new TableColumn("Eng");
        TableColumn ladder = new TableColumn("Lad");
        TableColumn rescue = new TableColumn("Res");
        TableColumn tank = new TableColumn("Tank");
        TableColumn foam = new TableColumn("Foam");
        TableColumn bls = new TableColumn("BLS");
        TableColumn als = new TableColumn("ALS");
        TableColumn tbls = new TableColumn("Trans\nBLS");
        TableColumn tals = new TableColumn("Trans\nALS");
        TableColumn wtank = new TableColumn("Water\nTank");
        TableColumn staff = new TableColumn("Crew");
        TableColumn ladsize = new TableColumn("Ladder\nSize");
        TableColumn ftank = new TableColumn("Foam\nTank");
        TableColumn stat = new TableColumn("Status");
        
        appUnit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Apparatus, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Apparatus, String> param) {
                return new SimpleStringProperty(
                		param.getValue().getAppType()+
                		param.getValue().getCounNum()+
                		param.getValue().getMuniNum()+
                		param.getValue().getAppNum());
            }
        });
        appYr.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("unitYear"));
        appMa.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("unitMake"));
        engine.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("engine"));
        ladder.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("ladder"));
        rescue.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("rescue"));
        tank.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("tanker"));
        foam.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("foam"));
        bls.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("bls"));
        als.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("als"));
        tbls.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("tbls"));
        tals.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("tals"));
        staff.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("crew"));
        wtank.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("wtank"));
        ladsize.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("ladSize"));
        ftank.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("ftank"));
        stat.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("stat"));
        
        table.setRowFactory( tv -> {
            TableRow<Apparatus> row = new TableRow<Apparatus>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	setBottomPane(row.getItem());
                }
            });
            return row ;
        });
      
        table.setItems(getApparatus());
        table.getColumns().addAll(
        		appUnit, appYr, appMa, 
        		engine, ladder, rescue, tank, foam, bls, als, tbls, tals, 
        		staff, wtank, ladsize, ftank, stat);

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);
 
		stage.setTitle("Apparatus Menu"); 
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
	 * 		edu.ramapo.jkole.cad.AppMenu.setBottomPane(Apparatus dat)
	 * SYNOPSIS
	 * 		Apparatus dat -> apparatus to get information for
	 * DESCRIPTION
	 * 		set the bottom portion of the stage to a tabbed menu with information 
	 * 		on the apparatus, location, and status log.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private void setBottomPane(Apparatus dat) {
		// TODO Auto-generated method stub
		tabPane.getTabs().clear();
		
		Tab appinf = new Tab("Apparatus Info");
		Tab apploc = new Tab("Apparatus Location");
		Tab appStat = new Tab("Apparatus Status");

		appinf.setContent(getAppInfo(dat));
		apploc.setContent(getAppLoc(dat));
		appStat.setContent(getStatus(dat));
		
		tabPane.getTabs().addAll(appinf, apploc, appStat );	
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.getStatus(Apparatus dat)
	 * SYNOPSIS
	 * 		Apparatus dat -> apparatus to recieve status log of
	 * DESCRIPTION
	 * 		this function creates and displays a table with all status history
	 * 		of the apparatus dat. the user also has the ability to change status
	 * 		in this menu with a button click. 
	 * RETURNS
	 * 		VBox vbox -> visual box containing buttons and tableview
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	@SuppressWarnings("unchecked")
	private Node getStatus(Apparatus dat) {
		// TODO Auto-generated method stub
		VBox vbox = new VBox();
		TableView<Status> stattable= new TableView<Status>();
		stattable.setPrefSize(400, 100);
		
		TableColumn<Status, String> App = new TableColumn<Status, String>("Apparatus");
		TableColumn<Status, String> avail = new TableColumn<Status, String>("Avail");
		TableColumn<Status, String> enrt = new TableColumn<Status, String>("Enrt");
		TableColumn<Status, String> onscene = new TableColumn<Status, String>("Onscene");
		TableColumn<Status, String> busy = new TableColumn<Status, String>("Busy");
		TableColumn<Status, String> active = new TableColumn<Status, String>("Active");
		TableColumn<Status, String> time = new TableColumn<Status, String>("Time Stamp");
		TableColumn<Status, String> comment = new TableColumn<Status, String>("Comment");
		
		App.setCellValueFactory(
               new PropertyValueFactory<Status, String>("app"));
		avail.setCellValueFactory(
				new PropertyValueFactory<Status, String>("avail"));
		enrt.setCellValueFactory(
				new PropertyValueFactory<Status, String>("enrt"));
		onscene.setCellValueFactory(
				new PropertyValueFactory<Status, String>("onscene"));
		busy.setCellValueFactory(
	            new PropertyValueFactory<Status, String>("busy"));
		active.setCellValueFactory(
				new PropertyValueFactory<Status, String>("active"));
		time.setCellValueFactory(
	            new PropertyValueFactory<Status, String>("timestamp"));
		comment.setCellValueFactory(
				new PropertyValueFactory<Status, String>("comment"));
		
		stattable.getColumns().addAll(App, avail, enrt, onscene, busy, active, time, comment);
		
		ObservableList<Status> statlist = getAppStatus(dat);
		stattable.setItems(statlist);
	
		vbox.setSpacing(10);
		vbox.setPadding(new Insets(5, 10, 20, 10));
		vbox.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
						BorderStrokeStyle.SOLID, 
					new CornerRadii(3), 
					new BorderWidths(2))));
		
		HBox btmenu = new HBox();
		
		ToggleGroup tbg = new ToggleGroup();
				
		ToggleButton availb = new ToggleButton("Available");
		availb.setUserData(new String("a"));
		availb.setToggleGroup(tbg);
		
		ToggleButton enrtb = new ToggleButton("Enroute");
		enrtb.setUserData(new String("e"));
		enrtb.setToggleGroup(tbg);
		
		ToggleButton onscb = new ToggleButton("On Scene");
		onscb.setUserData(new String("o"));
		onscb.setToggleGroup(tbg);
		
		ToggleButton busyb = new ToggleButton("Busy");
		busyb.setUserData(new String("b"));
		busyb.setToggleGroup(tbg);
		
		Button updatestat = new Button("Update");
		
		updatestat.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {     
            	if(tbg.getSelectedToggle() == null){
            		Status.updateStatus(new Status(false, false, false, false, false, dat), "Manual Status Change");
            	}
            	else{
            		if (tbg.getSelectedToggle().getUserData().toString().equalsIgnoreCase("e"))
            			Status.updateStatus(new Status(true, true, false, false, false, dat), "Manual Status Change");
            		else if (tbg.getSelectedToggle().getUserData().toString().equalsIgnoreCase("o"))
            			Status.updateStatus(new Status(true, false, true, false, false, dat), "Manual Status Change");
            		else if (tbg.getSelectedToggle().getUserData().toString().equalsIgnoreCase("a"))
            			Status.updateStatus(new Status(true, false, false, true, false, dat), "Manual Status Change");
            		else if (tbg.getSelectedToggle().getUserData().toString().equalsIgnoreCase("b"))
            			Status.updateStatus(new Status(true, false, false, false, true, dat), "Manual Status Change");
            	}
            	refresh();
            	refreshT();
            }

			private void refresh() {
				// TODO Auto-generated method stub
				stattable.getItems().clear();
				stattable.setItems(getAppStatus(dat));
			}
		});
		
		btmenu.getChildren().addAll( availb, enrtb, onscb, busyb);
		btmenu.setSpacing(8);
		vbox.getChildren().addAll(new Text("Status Menu"), stattable, btmenu, updatestat);
		return vbox;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.getAppStatus(Apparatus dat)
	 * SYNOPSIS
	 * 		Apparatus dat -> apparatus to recieve status log of
	 * DESCRIPTION
	 * 		creates a list status updates for specified apparatus
	 * RETURNS
	 * 		ObservableList<Status> -> table of all status within the apparatus
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private ObservableList<Status> getAppStatus(Apparatus dat) {
		ObservableList<Status> obl = FXCollections.observableArrayList(
				new Status(false, false, false, false, false, dat));
		Database.setDb(Database.client.getDB("Apparatus"));
		DBCollection coll = Database.db.getCollection("Status");
		List<DBObject> foundDocument = coll.find(new BasicDBObject("Apparatus", dat.getOid())).toArray();
		obl.remove(0);
		for(int i = 0; i < foundDocument.size(); i++){
			BasicDBObject temp = (BasicDBObject) foundDocument.get(i);			
			Status tstat = new Status(
					Boolean.parseBoolean(temp.getString("active")),
					Boolean.parseBoolean(temp.getString("enrt")),
					Boolean.parseBoolean(temp.getString("onscene")),
					Boolean.parseBoolean(temp.getString("avail")),
					Boolean.parseBoolean(temp.getString("busy")), 
					temp.get("TimeStamp").toString(),
					temp.getString("Comment"),
					dat);		
			obl.add(tstat);
		}
		return obl;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.getAppLoc(Apparatus app)
	 * SYNOPSIS
	 * 		Apparatus dat -> the apparatus to recieve location and update location
	 * DESCRIPTION
	 * 		generates a GUI for the user to change the station where this apparatus 
	 * 		is located. this generates 3 search boxes that the user can see all stations
	 * 		within a certain search criteria. 
	 * RETURNS
	 * 		VBox aploc -> the visual of the discription of this function
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private Node getAppLoc(Apparatus app) {
		VBox aploc = new VBox();
		aploc.setSpacing(10);
		aploc.setPadding(new Insets(5, 10, 20, 10));
		aploc.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
		HBox curloc = new HBox();
		curloc.getChildren().add(new Text(getCurLoc(app)));
		
		HBox query = new HBox();
		query.setSpacing(8);
		TextField tfCount = new TextField();
		tfCount.setPrefWidth(50);
		TextField tfMunic = new TextField();
		tfMunic.setPrefWidth(50);
		TextField tfDist = new TextField();
		tfDist.setPrefWidth(50);
		Button findloc = new Button("Search");
		
		findloc.setOnAction(e -> { 
            	loc.getItems().clear();
            	System.out.println("FIND LOCATION\n"+app.getUnitString());
            	DBCollection coll = Database.getCol("departmentlocs", "addresses");
            	BasicDBObject obj = new BasicDBObject("CountyCode", tfCount.getText());
            		if(!tfMunic.getText().equalsIgnoreCase(""))	{obj.append("MunicCode", tfMunic.getText());}
            		if(!tfDist.getText().equalsIgnoreCase(""))	{obj.append("District", tfDist.getText());}
            	ObservableList<Station> oblist = FXCollections.observableArrayList(new Station()); 
            	List<DBObject> foundDocument = coll.find(obj).toArray();
        		oblist.remove(0);
        		
        		for(int i = 0; i < foundDocument.size(); i++){
        			 BasicDBObject doc = (BasicDBObject) foundDocument.get(i);
        			 oblist.add(new Station(
        					 doc.get("_id").toString(),
        					 doc.get("Name").toString(), 
        					 doc.get("Address").toString(), 
        					 doc.get("Municipality").toString(), 
        					 doc.get("County").toString(),
        					 doc.get("State").toString(),
        					 doc.get("ContactNum").toString(),
        					 doc.get("CountyCode").toString(),
        					 doc.get("MunicCode").toString(),
        					 doc.get("District").toString()				 
        					 ));
        		}
        		loc.setItems(oblist);
                loc.setRowFactory( tv -> {
                    TableRow<Station> row = new TableRow<Station>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {  
                        	System.out.println("A "+app.getUnitString());
                        	changeLoc(app, row.getItem().getCountycode(), 
                            		row.getItem().getMuniccode(),
                            		row.getItem().getDistrict());
                            loc.getItems().clear();
                    }});
                    return row ;
                });
        });
		
		query.getChildren().addAll(tfCount, tfMunic, tfDist, findloc);
		aploc.getChildren().addAll(curloc, query, loc);
		
		return aploc;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.changeLoc(Apparatus dat, String county, String munic, String dist)
	 * SYNOPSIS
	 * 		Apparatus dat -> apparatus that will be updated
	 * 		String county -> county code of new location
	 * 		String munic -> municipal code of new location
	 * 		String dist -> district code of new location
	 * DESCRIPTION
	 * 		changes the values within the apparatus database of the location to county, munic, and dist.
	 * 		this changes where the apparatus is located within the system. 
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	protected void changeLoc(Apparatus dat, String c, String m, String d) {
		System.out.println(dat.getOid());
		DBCollection coll = Database.getCol("Apparatus", "info");
		BasicDBObject obj = (BasicDBObject) coll.findOne(new BasicDBObject("_id", new ObjectId(dat.getOid())));
			obj.put("UnitCounLoc", c);
			obj.put("UnitMuniLoc", m);
			obj.put("UnitDistLoc", d);
		Database.update("Apparatus", "info", obj, dat.getOid());
		obj = new BasicDBObject();
		refreshT();
	}
	private String getCurLoc(Apparatus dat) {
		MongoClient temp = Database.client; 
		DB db = temp.getDB("departmentlocs");
		DBCollection coll = db.getCollection("addresses");
		BasicDBObject obj = new BasicDBObject("CountyCode", dat.getUnitLocCoun())
			.append("MunicCode", dat.getUnitLocMuni())
			.append("District", dat.getUnitLocDist());
		DBObject doc = coll.findOne(obj);
		return doc.get("Name").toString();
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.getAppInfo(Apparatus dat)
	 * SYNOPSIS
	 * 		Apparatus dat -> apparatus that will be updated
	 * DESCRIPTION
	 * 		creates a GUI for the user to update information of the apparatus
	 * 		once fired it autofills all information about the apparatus that
	 * 		was previously filled.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private Node getAppInfo(Apparatus dat) {
		VBox apinfo = new VBox();
		apinfo.setSpacing(10);
		apinfo.setPadding(new Insets(5, 10, 20, 10));
		apinfo.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
		
		HBox staName = new HBox();
		
		TextField unit = new TextField();
		
		unit.setPrefWidth(50);
		
		staName.getChildren().add(new Text("Unit Number: "));
		staName.getChildren().addAll(unit);
		
		unit.setText(dat.getAppType()+dat.getCounNum()+dat.getMuniNum()+dat.getAppNum());
		
		HBox appInf = new HBox();
		
		TextField tfAppYr = new TextField(dat.getUnitYear());
		TextField tfAppMa = new TextField(dat.getUnitMake());
		
		appInf.setSpacing(5);
		appInf.getChildren().add(new Text("Apparatus Year:"));
		appInf.getChildren().add(tfAppYr);
		appInf.getChildren().add(new Text("Apparatus Make: "));
		appInf.getChildren().add(tfAppMa);
		
		HBox appType = new HBox();
		HBox emsType = new HBox();
		
		CheckBox engine = new CheckBox();
		CheckBox ladder = new CheckBox();
		CheckBox rescue = new CheckBox();
		CheckBox tank = new CheckBox();
		CheckBox foam = new CheckBox();
		CheckBox bls = new CheckBox();
		CheckBox als = new CheckBox();
		CheckBox tbls = new CheckBox();
		CheckBox tals = new CheckBox();
		CheckBox supv = new CheckBox();
		
		engine.setSelected(dat.isEngine());
		ladder.setSelected(dat.isLadder());
		rescue.setSelected(dat.isRescue());
		tank.setSelected(dat.isTanker());
		foam.setSelected(dat.isFoam());
		bls.setSelected(dat.isBls());
		als.setSelected(dat.isAls());
		tbls.setSelected(dat.isTbls());
		tals.setSelected(dat.isTals());
		supv.setSelected(dat.isSupv());
		
		appType.setSpacing(5);
		appType.getChildren().addAll(new Text("Engine: "), engine);
		appType.getChildren().addAll(new Text("Ladder: "), ladder);
		appType.getChildren().addAll(new Text("Rescue: "), rescue);
		appType.getChildren().addAll(new Text("Tanker: "), tank);
		appType.getChildren().addAll(new Text("Foam: "), foam);
		appType.getChildren().addAll(new Text("Chief: "), supv);
		
		emsType.setSpacing(5);
		emsType.getChildren().addAll(new Text("BLS Non_Trans: "), bls);
		emsType.getChildren().addAll(new Text("ALS Non_Trans: "), als);
		emsType.getChildren().addAll(new Text("BLS Transp: "), tbls);
		emsType.getChildren().addAll(new Text("ALS Transp: "), tals);
		
		HBox staCont = new HBox();
		TextField tfWTank = new TextField(dat.getWtank());
		TextField tfStaff = new TextField(dat.getCrew());
		TextField tfFTank = new TextField(dat.getFtank());
		TextField tfLad = new TextField(dat.getLadSize());
		
		tfWTank.setPrefWidth(40);
		tfStaff.setPrefWidth(40);
		tfFTank.setPrefWidth(40);
		tfLad.setPrefWidth(40);
		
		staCont.setSpacing(5);
		staCont.getChildren().add(new Text("Water Tank:"));
		staCont.getChildren().add(tfWTank);
		staCont.getChildren().add(new Text("Foam Tank:"));
		staCont.getChildren().add(tfFTank);
		staCont.getChildren().add(new Text("Crew: "));
		staCont.getChildren().add(tfStaff);
		staCont.getChildren().add(new Text("Ladder Size: "));
		staCont.getChildren().add(tfLad);
		
		HBox btMenu = new HBox();
		btMenu.setSpacing(5);
		
		Button save = new Button("Save");
		Button del = new Button("Delete");
		
		del.setOnAction(actionEvent -> delete(dat.getOid()));
		
		save.setOnAction(new EventHandler<ActionEvent>() {  
			@Override public void handle(ActionEvent e) {   				
            	BasicDBObject doc = Database.find("Apparatus", "info", dat.getOid());	
            	doc.put("AppType", unit.getText().toString().substring(0, 1));
            	doc.put("UnitCount", unit.getText().toString().substring(1, 3));
        		doc.put("UnitMunic", unit.getText().toString().substring(3, 5));
        		try{
        			doc.put("appNum", unit.getText().toString().substring(5, 6));
        		}
        		catch(Exception e1){
        			doc.put("appNum", "");
        		}
            	doc.put("AppYr", tfAppYr.getText().toString());
            	doc.put("AppMake", tfAppMa.getText().toString());
        		doc.put("Engine", engine.isSelected());
        		doc.put("Ladder", ladder.isSelected());
        		doc.put("Rescue", rescue.isSelected());
        		doc.put("Tank", tank.isSelected());
        		doc.put("Foam", foam.isSelected());
        		doc.put("Bls", bls.isSelected());
        		doc.put("Als", als.isSelected());
        		doc.put("tbls", tbls.isSelected());
        		doc.put("tals", tals.isSelected());
        		doc.put("supv", supv.isSelected());
        		doc.put("Crew", tfStaff.getText().toString());
        		doc.put("WTank", tfWTank.getText().toString());
        		doc.put("LadSize", tfLad.getText().toString());
        		doc.put("FTank", tfFTank.getText().toString());
        		if(unit.getText().toString().substring(0, 1).equalsIgnoreCase("B")){
        			doc.put("Brush", true);
        		}
        		else{
        			doc.put("Brush", true);
        		}
        		
                Database.update("Apparatus", "info", doc, dat.getOid());
                refreshT();
            }
        });
		
		if(Main.isAdmin()){
		btMenu.getChildren().addAll(save, del);
		}
		
		apinfo.getChildren().addAll(staName, appInf, appType, emsType, staCont, btMenu);
		
		return apinfo;
	}
	private void delete(String oid) {
		if(Database.remove("Apparatus", "info", oid)){
			System.out.println("Remove Success");
			refreshT();
        }
        else { System.out.println("Remove Error"); }            
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.addApparatus()
	 * SYNOPSIS
	 * 		 
	 * DESCRIPTION
	 * 		creates a GUI for the user to add an apparatus into the database
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private void addApparatus() {
		// TODO Auto-generated method stub
		Tab addStation = new Tab("Add Apparatus");
		
		VBox stinfo = new VBox();
		stinfo.setSpacing(10);
		stinfo.setPadding(new Insets(5, 10, 20, 10));
		stinfo.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
		
		HBox staName = new HBox();
	
		TextField tfUnit = new TextField();
		

		tfUnit.setPrefWidth(50);
		
		staName.getChildren().add(new Text("Unit: "));
		staName.getChildren().addAll(tfUnit);
		
		HBox appInf = new HBox();
		
		TextField tfAppYr = new TextField();
		TextField tfAppMa = new TextField();
		
		appInf.setSpacing(5);
		appInf.getChildren().add(new Text("Apparatus Year:"));
		appInf.getChildren().add(tfAppYr);
		appInf.getChildren().add(new Text("Apparatus Make: "));
		appInf.getChildren().add(tfAppMa);
		
		HBox appType = new HBox();
		HBox emsType = new HBox();
		
		CheckBox engine = new CheckBox();
		CheckBox ladder = new CheckBox();
		CheckBox rescue = new CheckBox();
		CheckBox tank = new CheckBox();
		CheckBox foam = new CheckBox();
		CheckBox bls = new CheckBox();
		CheckBox als = new CheckBox();
		CheckBox tbls = new CheckBox();
		CheckBox tals = new CheckBox();
		CheckBox supv = new CheckBox();
		
		appType.setSpacing(5);
		appType.getChildren().addAll(new Text("Engine: "), engine);
		appType.getChildren().addAll(new Text("Ladder: "), ladder);
		appType.getChildren().addAll(new Text("Rescue: "), rescue);
		appType.getChildren().addAll(new Text("Tanker: "), tank);
		appType.getChildren().addAll(new Text("Foam: "), foam);
		appType.getChildren().addAll(new Text("Chief"), supv);
		
		emsType.setSpacing(5);
		emsType.getChildren().addAll(new Text("BLS Non_Trans: "), bls);
		emsType.getChildren().addAll(new Text("ALS Non_Trans: "), als);
		emsType.getChildren().addAll(new Text("BLS Transp: "), tbls);
		emsType.getChildren().addAll(new Text("ALS Transp: "), tals);
		
		HBox staCont = new HBox();
		TextField tfWTank = new TextField();
		TextField tfStaff = new TextField();
		TextField tfFTank = new TextField();
		TextField tfLad = new TextField();
		
		tfWTank.setPrefWidth(40);
		tfStaff.setPrefWidth(40);
		tfFTank.setPrefWidth(40);
		tfLad.setPrefWidth(40);
		
		staCont.setSpacing(5);
		staCont.getChildren().add(new Text("Water Tank:"));
		staCont.getChildren().add(tfWTank);
		staCont.getChildren().add(new Text("Foam Tank:"));
		staCont.getChildren().add(tfFTank);
		staCont.getChildren().add(new Text("Staffing: "));
		staCont.getChildren().add(tfStaff);
		staCont.getChildren().add(new Text("Ladder Size: "));
		staCont.getChildren().add(tfLad);
		
		HBox btMenu = new HBox();
		btMenu.setSpacing(5);
		
		Button save = new Button("Save");
		
		save.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {  
            	BasicDBObject doc = 
          new BasicDBObject("AppType", tfUnit.getText().toString().substring(0, 1))
            		.append("UnitCount", tfUnit.getText().toString().substring(1, 3))
            		.append("UnitMunic", tfUnit.getText().toString().substring(3, 5))
                	.append("AppYr", tfAppYr.getText().toString())
                	.append("AppMake", tfAppMa.getText().toString())
	                .append("UnitCounLoc", "13")
	                .append("UnitMuniLoc", "93")
	                .append("UnitDistLoc", "1")
            		.append("Engine", engine.isSelected())
            		.append("Ladder", ladder.isSelected())
            		.append("Rescue", rescue.isSelected())
            		.append("Tank", tank.isSelected())
            		.append("Foam", foam.isSelected())
            		.append("Bls", bls.isSelected())
            		.append("Als", als.isSelected())
            		.append("tbls", tbls.isSelected())
            		.append("tals", tals.isSelected())
            		.append("supv", supv.isSelected())
            		.append("Crew", tfStaff.getText().toString())
            		.append("WTank", tfWTank.getText().toString())
            		.append("LadSize", tfLad.getText().toString())
            		.append("FTank", tfFTank.getText().toString())
            		.append("Status", "{}");
            	try{
            		doc.append("appNum", tfUnit.getText().toString().substring(5, 6));
            	}
            	catch(Exception e1){
            		doc.append("appNum", "");
            	}
            	if(tfUnit.getText().toString().substring(0, 1).equalsIgnoreCase("b")){
            		doc.append("Brush", true);
            	}
            	else{
            		doc.append("Brush", false);
            	}
            	Status stat = new Status(new Apparatus((BasicDBObject) doc));
            	Database.add("Apparatus", "info", doc);
            	doc = (BasicDBObject) Database.client.getDB("Apparatus").getCollection("info").findOne(doc);
            	//DBObject of status
            	BasicDBObject statobj = new BasicDBObject("Apparatus", doc.getString("_id"))
           			.append("avail", stat.isAvail())
           			.append("enrt", stat.isEnrt())
           			.append("onscene", stat.isOnscene())
           			.append("busy", stat.isBusy())
           			.append("active", stat.isActive())
           			.append("TimeStamp", Status.getTime())
            		.append("Comment", "New Apparatus");
            	
            	//add object to apparatus
            	doc.put("Status", statobj);
            	Database.client.getDB("Apparatus").getCollection("info").save(doc);
            	Database.add("Apparatus", "Status", statobj);
            	
                tabPane.getTabs().clear();
                refreshT();
            }
        });
		
		btMenu.getChildren().add(save);
		stinfo.getChildren().addAll(staName, appInf, appType, emsType, staCont, btMenu);
		
		addStation.setContent(stinfo);
		
		tabPane.getTabs().clear();
		tabPane.getTabs().add(addStation);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.refreshT()
	 * SYNOPSIS
	 * 		TableView table
	 * DESCRIPTION
	 * 		removes all values within the table and then calls a 
	 * 		new instance of the function to fill data into it
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	protected void refreshT() {
		// TODO Auto-generated method stub
		table.getItems().clear();
		table.setItems(getApparatus());
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.AppMenu.getApparatus()
	 * SYNOPSIS
	 * 		
	 * DESCRIPTION
	 * 		calls the database and recieves all apparatus within the system
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private ObservableList<Apparatus> getApparatus() {
		ObservableList<Apparatus> dat = FXCollections.observableArrayList(
				new Apparatus()); 
		Database.setDb(Database.client.getDB("Apparatus"));
		DBCollection coll = Database.db.getCollection("info");
		List<DBObject> foundDocument = coll.find().toArray();
		dat.remove(0);
		for(int i = 0; i < foundDocument.size(); i++){
			BasicDBObject temp = (BasicDBObject) foundDocument.get(i);
			Apparatus app = new Apparatus(temp);
			try {
				app.setStat(Status.getStatString(temp.getString("Status")));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dat.add(new Apparatus(temp));
		}
		return dat;
	}
}