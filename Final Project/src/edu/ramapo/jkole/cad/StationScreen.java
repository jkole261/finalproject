/**/
/**
 * StationScreen.java
 * 
 * @author Jason Kole
 * 
 * StationScreen class is a JavaFX application that displays all Station objects within 
 * the system for all town. Within this menu all stations will be able to be 
 * modified as well as added to the dispatch system. 
 */
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONException;

import com.mongodb.BasicDBObject;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class StationScreen extends Application{
	private TableView<Station> table;
	private TabPane tabPane = new TabPane();
	private boolean adminFlag;
	static Stage stage;
	
	public StationScreen(boolean admin) throws Exception{
		try{
			if(!(stage.isShowing())){return;}
			else{stage.show();}
		}
		catch(NullPointerException e2){
			stage = new Stage();
			adminFlag = Main.pro.getAdminlvl() > 3 ? true : false;
			table = new TableView<Station>();
			try {
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
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
		
		MenuItem addStation = new MenuItem("_Add Station");
		addStation.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		MenuItem adminmode = new MenuItem("_Enable Admin");
		adminmode.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
		MenuItem exit = new MenuItem("_Exit Window");
		exit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		
		if(adminFlag) { file.getItems().add(addStation); }
		
		file.getItems().add(adminmode);
		file.getItems().add(exit);
		
		mainMenu.getMenus().add(file);
		
		addStation.setOnAction(actionEvent -> addStation());
		adminmode.setOnAction(actionEvent -> changeAdmin());
		exit.setOnAction(actionEvent -> stage.close());
		
		root.setCenter(vbox);
		root.setBottom(tabPane);
		root.setTop(mainMenu);
		
		Label label = new Label("Stations");
		
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
       
        table.setRowFactory( tv -> {
            TableRow<Station> row = new TableRow<Station>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Station dat = row.getItem();
                    setBottomPane(dat);
                }
            });
            return row ;
        });
      
        table.setItems(getStations());
        table.getColumns().addAll(
        		name, address, municipal, county, state, cont, ccode, mcode, dcode);

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);
 
		stage.setTitle("Stations Menu"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment
        		.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show(); 
	}
	
	private void addStation() {
		// TODO Auto-generated method stub
		Tab addStation = new Tab("Add Station");
		
		VBox stinfo = new VBox();
		stinfo.setSpacing(10);
		stinfo.setPadding(new Insets(5, 10, 20, 10));
		stinfo.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
		
		HBox staName = new HBox();
		
		TextField tfStat = new TextField();
		
		tfStat.setPrefWidth(200);
		
		staName.getChildren().add(new Text("Station: "));
		staName.getChildren().add(tfStat);
		
		HBox staAddr = new HBox();
		
		TextField tfAddr = new TextField();
		TextField tfMunc = new TextField();
		
		tfAddr.setPrefWidth(200);
		staAddr.setSpacing(5);
		staAddr.getChildren().add(new Text("Address:"));
		staAddr.getChildren().add(tfAddr);
		staAddr.getChildren().add(new Text("Municipality: "));
		staAddr.getChildren().add(tfMunc);
		
		HBox staAddr2 = new HBox();
		
		TextField tfCounty = new TextField();
		TextField tfState = new TextField();
		
		staAddr2.setSpacing(5);
		staAddr2.getChildren().add(new Text("County: "));
		staAddr2.getChildren().add(tfCounty);
		staAddr2.getChildren().add(new Text("State: "));
		staAddr2.getChildren().add(tfState);
		
		HBox staCont = new HBox();
		TextField tfCC = new TextField();
		TextField tfMC = new TextField();
		TextField tfDC = new TextField();
		TextField tfContNum = new TextField();
		tfContNum.setPrefWidth(150);
		
		tfCC.setPrefWidth(40);
		tfMC.setPrefWidth(40);
		tfDC.setPrefWidth(40);
		
		staCont.setSpacing(5);
		staCont.getChildren().add(new Text("Contact #:"));
		staCont.getChildren().add(tfContNum);
		staCont.getChildren().add(new Text("Sta Identifier:"));
		staCont.getChildren().add(tfCC);
		staCont.getChildren().add(tfMC);
		staCont.getChildren().add(tfDC);
		
		HBox btMenu = new HBox();
		btMenu.setSpacing(5);
		
		Button save = new Button("Save");
		
		save.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {           		
            	BasicDBObject doc = new BasicDBObject("Name", tfStat.getText().toString())
            		.append("Address", tfAddr.getText().toString())
            		.append("Municipality", tfMunc.getText().toString())
                	.append("County", tfCounty.getText().toString())
                	.append("State", tfState.getText().toString())
	                .append("ContactNum", tfContNum.getText().toString())
	                .append("CountyCode", tfCC.getText().toString())
	                .append("MunicCode", tfMC.getText().toString())
	                .append("District", tfDC.getText().toString());
				Database.add("departmentlocs", "addresses", doc);
                tabPane.getTabs().clear();
                refreshT();
            }
        });
		
		btMenu.getChildren().add(save);
		stinfo.getChildren().addAll(staName, staAddr, staAddr2, staCont, btMenu);
		
		addStation.setContent(stinfo);
		
		tabPane.getTabs().clear();
		tabPane.getTabs().add(addStation);
	}

	private void changeAdmin() {
		if(adminFlag){
			adminFlag = false;	
		}
		else{
			adminFlag = true;
		}
	}

	private void setBottomPane(Station dat) {
		// TODO Auto-generated method stub
		tabPane.getTabs().clear();
		
		Tab station = new Tab("Station Info");
		Tab apparatus = new Tab("Apparatus Info");
		Tab activeap = new Tab("Active Apparatus");
		
		station.setContent(getStaInfo(dat));
		apparatus.setContent(AppMenu.getAppStation(dat));
		try {
			activeap.setContent(getActApp(dat));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tabPane.getTabs().addAll(station, apparatus, activeap);
	}
	
	private Node getStaInfo(Station sta) {
		VBox stinfo = new VBox();
		stinfo.setSpacing(10);
		stinfo.setPadding(new Insets(5, 10, 20, 10));
		stinfo.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
		
		HBox staName = new HBox();
		
		TextField tfStat = new TextField(sta.getName());
		
		tfStat.setPrefWidth(200);
		
		staName.getChildren().add(new Text("Station: "));
		staName.getChildren().add(tfStat);
		
		HBox staAddr = new HBox();
		
		TextField tfAddr = new TextField(sta.getAddress());
		TextField tfMunc = new TextField(sta.getMunicipality());
		
		tfAddr.setPrefWidth(200);
		staAddr.setSpacing(5);
		staAddr.getChildren().add(new Text("Address:"));
		staAddr.getChildren().add(tfAddr);
		staAddr.getChildren().add(new Text("Municipality: "));
		staAddr.getChildren().add(tfMunc);
		
		HBox staAddr2 = new HBox();
		
		TextField tfCounty = new TextField(sta.getCounty());
		TextField tfState = new TextField(sta.getState());
		
		staAddr2.setSpacing(5);
		staAddr2.getChildren().add(new Text("County: "));
		staAddr2.getChildren().add(tfCounty);
		staAddr2.getChildren().add(new Text("State: "));
		staAddr2.getChildren().add(tfState);
		
		HBox staCont = new HBox();
		TextField tfCC = new TextField(sta.getCountycode());
		TextField tfMC = new TextField(sta.getMuniccode());
		TextField tfDC = new TextField(sta.getDistrict());
		TextField tfContNum = new TextField(sta.getContactnum());
		tfContNum.setPrefWidth(150);
		tfCC.setPrefWidth(40);
		tfMC.setPrefWidth(40);
		tfDC.setPrefWidth(40);
		
		staCont.setSpacing(5);
		staCont.getChildren().add(new Text("Contact #:"));
		staCont.getChildren().add(tfContNum);
		staCont.getChildren().add(new Text("Sta Identifier:"));
		staCont.getChildren().add(tfCC);
		staCont.getChildren().add(tfMC);
		staCont.getChildren().add(tfDC);
		
		HBox btMenu = new HBox();
		btMenu.setSpacing(5);
		
		Button update = new Button("Update");
		Button delete = new Button("Delete");
		
		delete.setOnAction(actionEvent -> delete(sta.getOid()));
		
		update.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {           		
            	BasicDBObject doc = Database.find("departmentlocs", "addresses", sta.getOid());
            		doc.put("Name", tfStat.getText());
            		doc.put("Address", tfAddr.getText());
            		doc.put("Municipality", tfMunc.getText());
                	doc.put("County", tfCounty.getText());
                	doc.put("State", tfState.getText());
	                doc.put("ContactNum", tfContNum.getText());
	                doc.put("CountyCode", tfCC.getText());
	                doc.put("MunicCode", tfMC.getText());
	                doc.put("District", tfDC.getText());
                	Database.update("departmentlocs", "addresses", doc, sta.getOid());
                	refreshT();
            }
        });
		
		if(adminFlag){
			btMenu.getChildren().add(update);
			btMenu.getChildren().add(delete);
		}
		stinfo.getChildren().addAll(staName, staAddr, staAddr2, staCont, btMenu);
		
		return stinfo;
	}

	private void delete(String oid) {
		if(Database.remove("departmentlocs", "addresses", oid)){
			System.out.println("Remove Success");
			refreshT();
        }
        else { System.out.println("Remove Error"); }            
	}

	private void refreshT() {
		table.getItems().clear();
		table.setItems(getStations());
	}
	
	@SuppressWarnings("unchecked")
	private Node getActApp(Station sta) throws JSONException {
		VBox vbox = new VBox();
		
		TableView<Apparatus> apptable = new TableView<Apparatus>();
		
        TableColumn<Apparatus, String> appNum = new TableColumn<Apparatus, String>("Unit");
        TableColumn<Apparatus, String> appYr = new TableColumn<Apparatus, String>("App\nYear");
        TableColumn<Apparatus, String> appMa = new TableColumn<Apparatus, String>("App\nMake");
        TableColumn<Apparatus, String> engine = new TableColumn<Apparatus, String>("Eng");
        TableColumn<Apparatus, String> ladder = new TableColumn<Apparatus, String>("Lad");
        TableColumn<Apparatus, String> rescue = new TableColumn<Apparatus, String>("Res");
        TableColumn<Apparatus, String> tank = new TableColumn<Apparatus, String>("Tank");
        TableColumn<Apparatus, String> foam = new TableColumn<Apparatus, String>("Foam");
        TableColumn<Apparatus, String> bls = new TableColumn<Apparatus, String>("BLS");
        TableColumn<Apparatus, String> als = new TableColumn<Apparatus, String>("ALS");
        TableColumn<Apparatus, String> tbls = new TableColumn<Apparatus, String>("Trans\nBLS");
        TableColumn<Apparatus, String> tals = new TableColumn<Apparatus, String>("Trans\nALS");
        TableColumn<Apparatus, String> status = new TableColumn<Apparatus, String>("Status");
       
        appNum.setCellValueFactory(c -> 
        		new SimpleStringProperty(c.getValue().getUnitString()));
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
        status.setCellValueFactory(
        		new PropertyValueFactory<Apparatus, String>("stat"));
        
        apptable.setRowFactory( tv -> {
            TableRow<Apparatus> row = new TableRow<Apparatus>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    //Apparatus dat = row.getItem();
                    //setBottomPane(dat);
                }
            });
            return row ;
        });
      
        apptable.setItems(getActAppar(sta));
        apptable.getColumns().addAll(appNum, appYr, appMa, 
        		engine, ladder, rescue, tank, foam, bls, als, tbls, tals, 
        		status);

		apptable.setPrefHeight(200);
		
		vbox.setSpacing(10);
		vbox.setPadding(new Insets(5, 10, 20, 10));
		vbox.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
			
		vbox.getChildren().addAll(apptable);
		
		return vbox;
	}
	private ObservableList<Apparatus> getActAppar(Station sta) throws JSONException {
		ObservableList<Apparatus> dat = FXCollections.observableArrayList(
				new Apparatus()); 
		Database.setDb(Database.client.getDB("Apparatus"));
		DBCollection coll = Database.db.getCollection("info");
		BasicDBObject obj = new BasicDBObject("UnitCounLoc", sta.getCountycode())
				.append("UnitMuniLoc", sta.getMuniccode())
				.append("UnitDistLoc", sta.getDistrict());
		dat.remove(0);
		List<DBObject> foundDocument = coll.find(obj).toArray();
		for(int i = 0; i < foundDocument.size(); i++){
			 BasicDBObject temp = (BasicDBObject) foundDocument.get(i);
			 Apparatus app = new Apparatus(temp);
			 app.setStat(Status.getStatString(temp.getString("Status")));
			 if(!app.getStat().equalsIgnoreCase("OOS")){
				 dat.add(new Apparatus(temp));
			 }
		}
		return dat;
	}

	private ObservableList<Station> getStations() {
		ObservableList<Station> dat = FXCollections.observableArrayList(
				new Station(
						null, null, null, null, null, 
						null, null, null, null, null)); 
		Database.setDb(Database.client.getDB("departmentlocs"));
		DBCollection coll = Database.db.getCollection("addresses");
		List<DBObject> foundDocument = coll.find().toArray();
		dat.remove(0);
		for(int i = 0; i < foundDocument.size(); i++){
			 BasicDBObject temp = (BasicDBObject) foundDocument.get(i);
			 dat.add(new Station(
					 temp.get("_id").toString(),
					 temp.get("Name").toString(), 
					 temp.get("Address").toString(), 
					 temp.get("Municipality").toString(), 
					 temp.get("County").toString(),
					 temp.get("State").toString(),
					 temp.get("ContactNum").toString(),
					 temp.get("CountyCode").toString(),
					 temp.get("MunicCode").toString(),
					 temp.get("District").toString()				 
					 ));
		}
		return dat;
	}
}
