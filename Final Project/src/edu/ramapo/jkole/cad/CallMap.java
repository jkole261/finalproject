/**/
/** CallMap.java
 * 
 * @author Jason Kole
 * 
 * The callmap is a webview of a external html document. this is to provide the dispatcher an
 * overhead view of the area to assist the units on locaiton with further information that may 
 * not be available to the specific person on the ground.
 **/
/**/
package edu.ramapo.jkole.cad;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class CallMap extends Application {
    static Stage stage;
    static WebEngine webEngine;
    static WebView webView;
    
	public CallMap(){
    	stage = new Stage();
    	start(stage);
    }
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallMap.start()
	 * SYNOPSIS
	 * 		
	 * DESCRIPTION
	 * 		creates a GUI to display the file googlemap.html that is 
	 * 		interactive to the users similar to googlemaps.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
    @Override public void start(Stage stage) {
        // create web engine and view
    	webView = new WebView();
    	webEngine = webView.getEngine();
    	webEngine.load(getClass().getResource("/googlemap.html").toString());
        // create map type buttons
        final ToggleGroup mapTypeGroup = new ToggleGroup();
        final ToggleButton road = new ToggleButton("Road");
        road.setSelected(true);
        road.setToggleGroup(mapTypeGroup);
        final ToggleButton satellite = new ToggleButton("Satellite");
        satellite.setToggleGroup(mapTypeGroup);
        final ToggleButton hybrid = new ToggleButton("Hybrid");
        hybrid.setToggleGroup(mapTypeGroup);
        final ToggleButton terrain = new ToggleButton("Terrain");
        terrain.setToggleGroup(mapTypeGroup);
        mapTypeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle toggle1) {
                if (road.isSelected()) {
                    webEngine.executeScript("document.setMapTypeRoad()");
                } else if (satellite.isSelected()) {
                    webEngine.executeScript("document.setMapTypeSatellite()");
                } else if (hybrid.isSelected()) {
                    webEngine.executeScript("document.setMapTypeHybrid()");
                } else if (terrain.isSelected()) {
                    webEngine.executeScript("document.setMapTypeTerrain()");
                }
            }
        });
        // add search
        final TextField searchBox = new TextField();
        final Button search = new Button("Search");
        search.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		webEngine.executeScript("document.goToLocation(\'"+searchBox.getText()+"\')");;
        	}
        });
    
        Button zoomIn = new Button("Zoom In");
        zoomIn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) { webEngine.executeScript("document.zoomIn()"); }
        });
        Button zoomOut = new Button("Zoom Out");
        zoomOut.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) { webEngine.executeScript("document.zoomOut()"); }
        });
        // create toolbar
        ToolBar toolBar = new ToolBar();
        toolBar.getStyleClass().add("map-toolbar");
        toolBar.getItems().addAll(
                road, satellite, hybrid, terrain,
                createSpacer(),
                createSpacer(),
                new Label("Location:"), searchBox, search, zoomIn, zoomOut);
        // create root
        BorderPane root = new BorderPane();
        root.getStyleClass().add("map");
        root.setCenter(webView);
        root.setTop(toolBar);
        // create scene
        stage.setTitle("Web Map");
        Scene scene = new Scene(root,1000,700, Color.web("#666970"));
        stage.setScene(scene);
        // show stage
        stage.show();
    }
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.Call.createSpacer()
	 * SYNOPSIS
	 * 		 
	 * DESCRIPTION
	 * 		creates a spacer within the GUI
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
}