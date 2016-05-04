/**/
/**
 * DispatchMenu.java
 * 
 * @author Jason Kole
 * 
 * the DispatchMenu is a javaFX application that contains quick counts 
 * of all pending calls and time since last 911 dispatch call recieved.
 * This screen is meant to be aid to dispatchers but give no crutial 
 * information
 */
/**/
package edu.ramapo.jkole.cad;
 
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DispatcherMenu extends Application {
	static Stage stage;
	
	public DispatcherMenu(){
		try{
			if(!stage.isShowing()){
				stage.show();
			}
			else{ }
		}
		catch(Exception e){
			stage = new Stage();
			try {
				start(stage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 1000, 700, Color.ANTIQUEWHITE);
    	try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		stage.setTitle("Dispatcher Screen"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show();
	}
}