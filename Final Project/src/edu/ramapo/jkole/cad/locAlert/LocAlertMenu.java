/**/
/**
 * LocAlertMenu.java
 * 
 * @author Jason Kole
 * 
 * LocAlertMenu is a javaFX application that displays all 
 * LocationAlerts within the dispatch system. from this 
 * screen you can add alerts and they will be automattically 
 * formated into the correct address format.
 */
/**/
package edu.ramapo.jkole.cad.locAlert;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.ramapo.jkole.cad.Call;
import edu.ramapo.jkole.cad.ChoiceMenu;
import edu.ramapo.jkole.cad.Database;
import edu.ramapo.jkole.cad.Main;
import edu.ramapo.jkole.cad.Municipality;
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
import javafx.scene.input.MouseEvent;
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

public class LocAlertMenu extends Application{
	Stage stage;
	TableView<LocationAlert> table;
	private TabPane tabPane = new TabPane();
	
	public LocAlertMenu(){
		try{
			if((stage.isShowing())){return;}
			else if(!(stage.isShowing())){
				stage.show();
			}
		}
		catch(NullPointerException e2){
			stage = new Stage();
			table = new TableView<LocationAlert>();
			try {
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public void start(Stage primaryStage) throws Exception {
		if(Main.pro.getAdminlvl() < 4){return;}
		
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
		
		MenuItem addAlert = new MenuItem("_Add Alert");
		addAlert.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
		MenuItem adminmode = new MenuItem("_Enable Admin");
		MenuItem exit = new MenuItem("_Exit Window");
		exit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		
		file.getItems().addAll(addAlert, adminmode, exit);
		
		mainMenu.getMenus().add(file);
		
		addAlert.setOnAction(actionEvent -> addAlert());
		adminmode.setOnAction(actionEvent -> {});
		exit.setOnAction(actionEvent -> stage.close());
		
		root.setCenter(vbox);
		root.setBottom(tabPane);
		root.setTop(mainMenu);
		
		Label label = new Label("Location Alerts");
		
		TableColumn<LocationAlert, String> location = new TableColumn<LocationAlert, String>("Location");
		TableColumn<LocationAlert, String> city = new TableColumn<LocationAlert, String>("City");
		TableColumn<LocationAlert, String> type = new TableColumn<LocationAlert, String>("Type");
		TableColumn<LocationAlert, String> info = new TableColumn<LocationAlert, String>("Info");
      
        table.setRowFactory( tv -> {
            TableRow<LocationAlert> row = new TableRow<LocationAlert>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    LocationAlert dat = row.getItem();
             //       setBottomPane(dat);
                }
            });
            return row ;
        });
      
        table.setItems(getAlerts());
        
        location.setCellValueFactory(
                new PropertyValueFactory<LocationAlert, String>("location"));
        city.setCellValueFactory(new PropertyValueFactory<LocationAlert, String>("munic"));
        type.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LocationAlert, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LocationAlert, String> param) {
                return new SimpleStringProperty(param.getValue().getTypeString());
            }
        });
        info.setCellValueFactory(new PropertyValueFactory<LocationAlert, String>("info"));
        
        table.getColumns().addAll(
        		location, city, type, info);
        
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);
 
		stage.setTitle("Location Alert Menu"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment
        		.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show(); 
	}
	private void setBottomPane(LocationAlert dat) {
		tabPane.getTabs().clear();
		
		Tab alert = new Tab("Location Alert Info");
		
		alert.setContent(getStaInfo(dat));
		
		tabPane.getTabs().add(alert);
	}
	private Node getStaInfo(LocationAlert dat) {
		
		return null;
	}
	@SuppressWarnings("unchecked")
	private ObservableList<LocationAlert> getAlerts() {
		ObservableList<LocationAlert> dat = FXCollections.observableArrayList(
				new LocationAlert()); 
		Database.setDb(Database.client.getDB("Alerts"));
		DBCollection coll = Database.db.getCollection("info");
		List<DBObject> foundDocument = coll.find().toArray();
		dat.remove(0);
		for(int i = 0; i < foundDocument.size(); i++){
			 BasicDBObject temp = (BasicDBObject) foundDocument.get(i);
			 dat.add(new LocationAlert((HashMap<String, String>)temp.toMap()));
		}
		return dat;
	}
	private void addAlert() {
		Tab addStation = new Tab("Add Alert");
		
		VBox stinfo = new VBox();
		stinfo.setSpacing(10);
		stinfo.setPadding(new Insets(5, 10, 20, 10));
		stinfo.setBorder(new Border(
				new BorderStroke(Paint.valueOf("GRAY"), 
					BorderStrokeStyle.SOLID, 
				new CornerRadii(3), 
				new BorderWidths(2))));
		
		HBox alertAddr = new HBox();
		TextField address = new TextField();
		address.setPrefWidth(300);
		
		alertAddr.getChildren().addAll(new Text("Address: "), address);
		
		HBox alertInfo = new HBox();
		
		TextField type = new TextField();
		TextField info = new TextField();
		type.setPrefWidth(100);
		info.setPrefWidth(200);
		
		alertInfo.setSpacing(5);
		alertInfo.getChildren().add(new Text("Type:"));
		alertInfo.getChildren().add(type);
		
		type.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent event) {
    			new ChoiceMenu(type, "alertType");
    		}
    	});
		
		alertInfo.getChildren().add(new Text("Info: "));
		alertInfo.getChildren().add(info);
			
		HBox btMenu = new HBox();
		btMenu.setSpacing(5);
		
		Button save = new Button("Save");
		
		save.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) { 
            	address.setText(validateAddress(address.getText()));
            	LocationAlert alert = new LocationAlert();
            	alert.setLocation(address.getText());
            	alert.setMunic(Municipality.getCodeFromDB("Municipality", 
            					address.getText().split(",")[1].trim()));
            	alert.setCity(address.getText().split(",")[1].trim());
            	alert.setType(type.getText());
            	alert.setInfo(info.getText());
            	BasicDBObject doc = new BasicDBObject(alert.toMap());
				Database.add("Alerts", "info", doc);
                tabPane.getTabs().clear();
                refreshT();
            }
        });
		
		btMenu.getChildren().add(save);
		
		stinfo.getChildren().addAll(alertAddr, alertInfo, btMenu);
		
		addStation.setContent(stinfo);
		
		tabPane.getTabs().clear();
		tabPane.getTabs().add(addStation);
	}
	protected String validateAddress(String text) {
		try {
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(text+", NJ")
					.setLanguage("en").getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			return geocoderResponse.getResults().get(0).getFormattedAddress();
		} catch (IOException e) {
			System.err.print(e);
		}
		return "";
	}
	protected void refreshT() {
		table.getItems().clear();
		table.setItems(getAlerts());
		
	}
}