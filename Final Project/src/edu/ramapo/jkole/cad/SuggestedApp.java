/**/
/**
 * SuggestedApp.java
 * 
 * @author Jason Kole
 * 
 * SuggestedApp is a javaFX application that suggests and 
 * modifies apparatus to respond to an emergency.
 */
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SuggestedApp extends Application {
	private Stage stage;
	private static List<AppList> list;
	private static List<Apparatus> applist;
	private static String type;
	private static String cadid;
	public SuggestedApp(){
		stage = new Stage();
		try {
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<AppList> getNew(AppList curr, List<AppList> apps, String c){
		type = curr.type;
		cadid = c;
		try {
			applist = Dispatch.getUnitsWithType(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new SuggestedApp();
		
		return list;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 400, 300, Color.ANTIQUEWHITE);
    	try {
			scene.getStylesheets().add((new File("lib/cs/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
    	root.setCenter(getCent());
    	
		stage.setTitle("Dispatch Unit Screen"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show();
	}

	@SuppressWarnings("unchecked")
	private Node getCent() throws IOException {
		VBox box = new VBox();
		List<Apparatus> apps = new ArrayList<Apparatus>();
		TableView<Apparatus> table = new TableView<Apparatus>();
		
		TableColumn<Apparatus, String> app = new TableColumn<Apparatus, String>();
		TableColumn<Apparatus, String> location = new TableColumn<Apparatus, String>();
		
		apps = Dispatch.getUnitsWithType(cadid);
		list = removeUnits(apps);
		//GET TOTAL LIST 
		app.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUnitString()));
		location.setCellValueFactory(c -> new SimpleStringProperty(""));
		
		table.getColumns().addAll(app, location);
		
		table.setItems(FXCollections.observableArrayList(apps));	//CHANGE TO LIST ONCE REMOVE DONE

		//REMOVE CURRENT
		
		box.getChildren().add(table);
		
		return box;
	}

	private List<AppList> removeUnits(List<Apparatus> apps) {
		// TODO Auto-generated method stub
		for(int i = 0; i < apps.size(); i++){
			applist.remove(apps.get(i));
		}	
		for(int i = 0; i < applist.size(); i++){
			list.add(new AppList(type, applist.get(i)));
		}
		return list;
	} 
}