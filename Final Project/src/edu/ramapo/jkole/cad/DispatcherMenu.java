package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DispatcherMenu extends Application {
	static Stage stage;
	
	public DispatcherMenu(){
		stage = new Stage();
		try{
			start(stage);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 1000, 700, Color.ANTIQUEWHITE);
    	
		stage.setTitle("Dispatcher Screen"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show();
	}
	
}
