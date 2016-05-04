/**/
/**
 * MainMenu.java
 * 
 * @author Jason Kole
 * 
 * the MainMenu is the first javaFX application once the program is started if the login
 * screen is bypassed. this holds a command line and also buttons for all important menus
 * that will be accessed often within the system.
 */
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;

import edu.ramapo.jkole.cad.locAlert.LocAlertMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainMenu extends Application {
	
	TextField cmdline;
	static Stage stage;
	
    @Override
	public void stop() throws Exception {
    	Database.close();
		super.stop();
	}
    
	public void start(Stage stage) {
		stage = new Stage();
        BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 700, 100, Color.ANTIQUEWHITE);
    	try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
    	
        VBox topContainer = new VBox();  
        MenuBar mainMenu = new MenuBar();  
        ToolBar toolBar = new ToolBar();
        
        StackPane stack = new StackPane();
        
        stack.getChildren().add(Clock.getClock());
        stack.setAlignment(Pos.CENTER_RIGHT);
        
        root.setRight(stack);
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolBar);
        
        root.setTop(topContainer);
        
        root.setCenter(cmdLine());
        
        Menu file = new Menu("_File");
        Menu disp = new Menu("_Dispatch");
        Menu sear = new Menu("_Search");
        Menu admi = new Menu("_Admin");
        Menu help = new Menu("_Help");    
       
        //File Menu
        MenuItem exit = new MenuItem("Exit");
        MenuItem prop = new MenuItem("Properties");
        exit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        
        //DispatchMenu
        MenuItem stations = new MenuItem("Edit Stations");
        MenuItem apperat = new MenuItem("Edit Apparatus");
        
        //Search Menu
        
        //Admin Menu
        MenuItem statusmenu = new MenuItem("Status Menu");
        MenuItem municmenu = new MenuItem("Municipality Menu");
        MenuItem localert = new MenuItem("Location Alerts");
        admi.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
       
        //Help Menu
        MenuItem connTest = new MenuItem("Database Test");
        
        file.getItems().addAll(prop, exit);
        
        disp.getItems().addAll(stations, apperat);
        
        admi.getItems().addAll(municmenu, statusmenu, localert);
        
        help.getItems().addAll(connTest);
        
        prop.setOnAction(actionEvent -> new PrefMenu());
        
        exit.setOnAction(actionEvent -> Platform.exit());
        
        municmenu.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		try {
					new MunicMenu();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
        
        statusmenu.setOnAction(new EventHandler<ActionEvent>(){
        	@Override
        	public void handle(ActionEvent e){
        		new StatusMenu();
        	}
        });
        
        stations.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                try {
					new StationScreen(Main.isAdmin());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        });
        
        apperat.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					new AppMenu();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
        });
        
        localert.setOnAction(actionEvent -> new LocAlertMenu());
        
        connTest.setOnAction(actionEvent -> Database
        		.connectionTest());
       
        if(Main.isAdmin()){
        	
        	mainMenu.getMenus().addAll(file, disp, sear, admi, help);
        }
        if(!Main.isAdmin()){
        	mainMenu.getMenus().addAll(file, disp, sear, help);
        }
        Button dispscreen = new Button("Disp");	
        dispscreen.setTooltip(new Tooltip("Dispatch Screen"));
        dispscreen.setOnAction(actionEvent -> new DispatcherMenu());
        
        Button callTaker = new Button("Call Taker");				
        callTaker.setTooltip(new Tooltip("Calltaker Screen"));
        callTaker.setOnAction(actionEvent -> new CallTakerScreen());
        
        Button mapping = new Button("Map");				
        mapping.setTooltip(new Tooltip("Map"));
        mapping.setOnAction(actionEvent -> getMap());
        
        Button activeCalls = new Button("Act Calls");	
        activeCalls.setTooltip(new Tooltip("Active Calls"));
        activeCalls.setOnAction(actionEvent -> new ActCallMenu());
        
        Button activeUnits = new Button("Act Units");
        activeUnits.setTooltip(new Tooltip("Active Units"));
        activeUnits.setOnAction(actionEvent -> new ActUnitMenu());
        
        Button pendingCalls = new Button("Pend Call");	
        pendingCalls.setTooltip(new Tooltip("Pending Calls"));
        pendingCalls.setOnAction(actionEvent -> new PendingCallsMenu());
                 
        toolBar.getItems().addAll(callTaker, dispscreen, mapping, activeCalls,
        		activeUnits, pendingCalls);
              
    	cmdline.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke){
                if (ke.getCode().equals(KeyCode.ENTER)){
                	try{
                		if(ActCallMenu.stage.isShowing()){
                			Call c = ActCallMenu.getSelectedCall();
                			update(c, cmdline.getText().toUpperCase());
                		}   
                		else if(FireCallScreen.stage.isShowing()){
	                		Call c = FireCallScreen.c;
	                		update(c, cmdline.getText().toUpperCase());
	               		}
	                	else{
	                		execute(cmdline.getText().toUpperCase());
	                	}
	                	cmdline.clear();
                	}
                	catch(NullPointerException e){
                		execute(cmdline.getText().toUpperCase());
                		cmdline.clear();
                	}
                }
            }
        });
        
    	cmdline.setFocusTraversable(true);
    	
        stage.setTitle("Computer Aided Dispatch 2.0"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show(); 	
       
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		    	System.out.println("SYSTEM EXIT");
		    	Database.close();
		    	try{
		    		Main.chk.close();
		    		Main.client.close();
		    	}
		    	catch(NullPointerException e){ }
		    	Platform.exit();
		    }
		});
    }
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.MainMenu.update(Call selectedCall, String text)
	 * SYNOPSIS
	 * 		Call selectedCall -> call selected from activecallmenu
	 * 		String text -> text in command line
	 * DESCRIPTION
	 * 		calls the modify function in the CmdLine class
	 * RETURNS
	 * 		Action
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	protected void update(Call selectedCall, String text) {
		String[] str = parseText(text);
		CmdLine.modify(selectedCall, str[0], str);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.MainMenu.execute(String text)
	 * SYNOPSIS
	 * 		String text -> text in command line
	 * DESCRIPTION
	 * 		calls the execute function in the CmdLine class
	 * RETURNS
	 * 		Action
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	protected void execute(String text){
		String[] str = parseText(text);
		CmdLine.execute(str);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.MainMenu.parseText(String text)
	 * SYNOPSIS
	 * 		String text -> text in command line
	 * DESCRIPTION
	 * 		splits the text in command line to an string array
	 * RETURNS
	 * 		String[] text
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	private String[] parseText(String text) {
		return text.split(" ");
	}
	private Node cmdLine() {
		VBox box = new VBox();
		
		cmdline = new TextField();
		
		box.getChildren().add(cmdline);
		box.setSpacing(10);
		box.setPadding(new Insets(10, 10, 10, 10));
		
		return box;
	}
	
	private void getMap() {
		new CallMap();
	}
	public static void openMenu(String[] args){
		try {
			Application.launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	public static void showPopup(String message) {
		System.out.println(message);
	}
}