/**/
/**
 * MunicMenu.java
 * 
 * @author Jason Kole
 * 
 * MunicMenu class is a JavaFX application that displays all Municipalities within the system
 * for all town. within this menu all municipalities will be able to be modified as well
 * as added to the dispatch system. 
 */
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import com.mongodb.BasicDBObject;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

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

public class MunicMenu {

	private TableView<Municipality> table;
	private TabPane tabPane = new TabPane();
	private boolean adminFlag;
	static Stage stage;
	
	public MunicMenu() throws Exception{	
		try{
			if(!(stage.isShowing())){return;}
			else{stage.show();}
		}
		catch(NullPointerException e2){
			stage = new Stage();
			adminFlag = Main.isAdmin();
			table = new TableView<Municipality>();
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
		
		MenuItem addStation = new MenuItem("_Add City");
		addStation.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		MenuItem exit = new MenuItem("_Exit Window");
		exit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		
		if(Main.pro.getAdminlvl() == 5) { file.getItems().add(addStation); }
		
		file.getItems().add(exit);
		
		mainMenu.getMenus().add(file);
		
		addStation.setOnAction(actionEvent -> addMunic());
		exit.setOnAction(actionEvent -> stage.close());
		
		root.setCenter(vbox);
		root.setBottom(tabPane);
		root.setTop(mainMenu);
		
		Label label = new Label("Municipalities");
		
        TableColumn address = new TableColumn("Address");
        TableColumn municipal = new TableColumn("Municipality");
        TableColumn county = new TableColumn("County");
        TableColumn state = new TableColumn("ST");
        TableColumn cont = new TableColumn("Contact #");
        TableColumn ccode = new TableColumn("C #");
        TableColumn mcode = new TableColumn("M #");
        TableColumn oid = new TableColumn("id");
       
        address.setCellValueFactory(
                new PropertyValueFactory<Municipality, String>("Address"));
        municipal.setCellValueFactory(
                new PropertyValueFactory<Municipality, String>("Municipality"));
        county.setCellValueFactory(
        		new PropertyValueFactory<Municipality, String>("county"));
        state.setCellValueFactory(
        		new PropertyValueFactory<Municipality, String>("State"));
        cont.setCellValueFactory(
        		new PropertyValueFactory<Municipality, String>("contactnum"));
        ccode.setCellValueFactory(
        		new PropertyValueFactory<Municipality, String>("countycode"));
        mcode.setCellValueFactory(
        		new PropertyValueFactory<Municipality, String>("municcode"));
        oid.setCellValueFactory(
        		new PropertyValueFactory<Municipality, String>("oid")); 
       
        table.setRowFactory( tv -> {
            TableRow<Municipality> row = new TableRow<Municipality>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	Municipality dat = row.getItem();
                    setBottomPane(dat);
                }
            });
            return row ;
        });
      
        table.setItems(getMunicipalities());
        table.getColumns().addAll(
        		municipal, address, county, state, cont, ccode, mcode);

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);
 
		stage.setTitle("Municipality Menu"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment
        		.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show(); 
	}
	
	private void addMunic() {
		// TODO Auto-generated method stub
		Tab addMunic = new Tab("Add Municipality");
		
		VBox stinfo = new VBox();
		stinfo.setSpacing(10);
		stinfo.setPadding(new Insets(5, 10, 20, 10));
		stinfo.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
		
		HBox staAddr = new HBox();
		
		TextField tfAddr = new TextField();
		TextField tfMunc = new TextField();
		
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
		TextField tfContNum = new TextField();
		
		tfCC.setPrefWidth(40);
		tfMC.setPrefWidth(40);
		
		staCont.setSpacing(5);
		staCont.getChildren().add(new Text("Contact #:"));
		staCont.getChildren().add(tfContNum);
		staCont.getChildren().add(new Text("Sta Identifier:"));
		staCont.getChildren().add(tfCC);
		staCont.getChildren().add(tfMC);
		
		HBox btMenu = new HBox();
		btMenu.setSpacing(5);
		
		Button save = new Button("Save");
		
		save.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {           		
            	BasicDBObject doc = new BasicDBObject("Address", tfAddr.getText().toString())
            		.append("Municipality", tfMunc.getText().toString())
                	.append("County", tfCounty.getText().toString())
                	.append("State", tfState.getText().toString())
	                .append("ContactNum", tfContNum.getText().toString())
	                .append("CountyCode", tfCC.getText().toString())
	                .append("MunicCode", tfMC.getText().toString());
				Database.add("municipalities", "addresses", doc);
                tabPane.getTabs().clear();
                refreshT();
            }
        });
		
		btMenu.getChildren().add(save);
		stinfo.getChildren().addAll(staAddr, staAddr2, staCont, btMenu);
		
		addMunic.setContent(stinfo);
		
		tabPane.getTabs().clear();
		tabPane.getTabs().add(addMunic);
	}

	@SuppressWarnings("unused")
	private void changeAdmin() {
		if(adminFlag){
			adminFlag = false;	
		}
		else{
			adminFlag = true;
		}
	}

	private void setBottomPane(Municipality dat) {
		// TODO Auto-generated method stub
		tabPane.getTabs().clear();
		
		Tab munic = new Tab("Municipality Info");
		
		munic.setContent(getMunInfo(dat));
		
		tabPane.getTabs().addAll(munic);
	}
	
	private Node getMunInfo(Municipality munic) {
		VBox stinfo = new VBox();
		stinfo.setSpacing(10);
		stinfo.setPadding(new Insets(5, 10, 20, 10));
		stinfo.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));

		HBox staAddr = new HBox();
		
		TextField tfAddr = new TextField(munic.getAddress());
		TextField tfMunc = new TextField(munic.getMunicipality());
		
		staAddr.setSpacing(5);
		staAddr.getChildren().add(new Text("Address:"));
		staAddr.getChildren().add(tfAddr);
		staAddr.getChildren().add(new Text("Municipality: "));
		staAddr.getChildren().add(tfMunc);
		
		HBox staAddr2 = new HBox();
		
		TextField tfCounty = new TextField(munic.getCounty());
		TextField tfState = new TextField(munic.getState());
		
		staAddr2.setSpacing(5);
		staAddr2.getChildren().add(new Text("County: "));
		staAddr2.getChildren().add(tfCounty);
		staAddr2.getChildren().add(new Text("State: "));
		staAddr2.getChildren().add(tfState);
		
		HBox staCont = new HBox();
		TextField tfCC = new TextField(munic.getCountycode());
		TextField tfMC = new TextField(munic.getMuniccode());
		TextField tfContNum = new TextField(munic.getContactnum());
		
		tfCC.setPrefWidth(40);
		tfMC.setPrefWidth(40);
		
		staCont.setSpacing(5);
		staCont.getChildren().add(new Text("Contact #:"));
		staCont.getChildren().add(tfContNum);
		staCont.getChildren().add(new Text("Sta Identifier:"));
		staCont.getChildren().add(tfCC);
		staCont.getChildren().add(tfMC);
		
		HBox btMenu = new HBox();
		btMenu.setSpacing(5);
		
		Button update = new Button("Update");
		Button delete = new Button("Delete");
		
		update.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {           		
            	BasicDBObject doc = Database.find("municipalities", "addresses", munic.getOid());
            		doc.put("Municipality", tfMunc.getText());	
            		doc.put("Address", tfAddr.getText());
                	doc.put("County", tfCounty.getText());
                	doc.put("State", tfState.getText());
	                doc.put("ContactNum", tfContNum.getText());
	                doc.put("CountyCode", tfCC.getText());
	                doc.put("MunicCode", tfMC.getText());
                	Database.update("municipalities", "addresses", doc, munic.getOid());
                	refreshT();
            }
        });
		
		btMenu.getChildren().addAll(update, delete);
		
		delete.setOnAction(actionEvent -> delete(munic.getOid()));
		stinfo.getChildren().addAll(staAddr, staAddr2, staCont, btMenu);
		
		return stinfo;
	}

	private void delete(String oid) {
		if(Database.remove("municipalities", "addresses", oid)){
			System.out.println("Remove Success");
			refreshT();
        }
        else { System.out.println("Remove Error"); }            
	}

	private void refreshT() {
		table.getItems().clear();
		table.setItems(getMunicipalities());
	}
	
	private ObservableList<Municipality> getMunicipalities() {
		ObservableList<Municipality> dat = FXCollections.observableArrayList(
				new Municipality()); 
		Database.setDb(Database.client.getDB("municipalities"));
		DBCollection coll = Database.db.getCollection("addresses");
		List<DBObject> foundDocument = coll.find().toArray();
		dat.remove(0);
		for(int i = 0; i < foundDocument.size(); i++){
			 BasicDBObject temp = (BasicDBObject) foundDocument.get(i);
			 dat.add(new Municipality(
					 temp.get("_id").toString(),
					 temp.get("Municipality").toString(), 
					 temp.get("Address").toString(), 
					 temp.get("County").toString(),
					 temp.get("State").toString(),
					 temp.get("ContactNum").toString(),
					 temp.get("CountyCode").toString(),
					 temp.get("MunicCode").toString()				 
						 ));
			}
			return dat;
		}
}