/**/
/**
 * PrefMenu.java
 * 
 * @author Jason Kole
 * 
 * the PrefMenu will be accessed through the mainmenu and be 
 * able to change CSS formatting throughout the entire program.
 * the preferences are stored in a CSS file according to the 
 * user.
 * 
 * NOT COMPLETED FOR THIS RELEASE
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

public class PrefMenu extends Application{
	static Stage stage;
	
	public PrefMenu(){
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
    	Scene scene = new Scene(root, 600, 600, Color.ANTIQUEWHITE);
    	try { scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css")
    			.toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		stage.setTitle("Preferences Menu"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show();
	}
}
