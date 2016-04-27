/**/
/**
 * UsageLog.java
 * 
 * @author Jason Kole
 * 
 * UsageLog is a javaFX application that can be accessed 
 * from the firecallscreen or the calltakerscreen and 
 * will show the status updates for all units that are 
 * apart of the current call. 
 */
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UsageLog extends Application{
	Stage stage;
	String cadid;
	TableView<Status> table;
	
	public UsageLog(String cadid){
		stage = new Stage();
		this.cadid = cadid;
		table = new TableView<Status>();
		try {
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 600, 550, Color.ANTIQUEWHITE);
    	try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
    	TableColumn<Status, String> unit = new TableColumn<Status, String>();
    	TableColumn<Status, String> stat = new TableColumn<Status, String>();
    	TableColumn<Status, String> time = new TableColumn<Status, String>();
    	TableColumn<Status, String> usr = new TableColumn<Status, String>();
    	TableColumn<Status, String> com = new TableColumn<Status, String>();
    	
    	unit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Status, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Status, String> param) {
                return new SimpleStringProperty(param.getValue().getApp().getUnitString());
            }
        }); 	
    	stat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Status, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Status, String> param) {
                return new SimpleStringProperty(param.getValue().toString());
            }
        }); 	
    	time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Status, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Status, String> param) {
                return new SimpleStringProperty(param.getValue().getTimestamp());
            }
        });
    	usr.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Status, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Status, String> param) {
                return new SimpleStringProperty(param.getValue().getComment()
                		.substring(param.getValue().getComment().indexOf("OPR:")+4));
            }
        });
    	com.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Status, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Status, String> param) {
                return new SimpleStringProperty(param.getValue().getComment()
                		.substring(0, param.getValue().getComment().indexOf(':')));
            }
        });
    	
    	table.getColumns().addAll(unit, stat, time, usr, com);

	    table.setItems(check(table.getItems()));
    	
    	MenuBar menu = new MenuBar();
    	
    	Menu file = new Menu("_File");
    	Menu edit = new Menu("_Edit");
    	Menu cad = new Menu("_CAD");
    	Menu help = new Menu("_Help");
    	
    	menu.getMenus().addAll(file, edit, cad, help);
    	
    	root.setCenter(table);
    	
		stage.setTitle("Usage Log For Incident "+cadid); 
	    stage.setScene(scene);
	    stage.setMaxWidth(GraphicsEnvironment
	    		.getLocalGraphicsEnvironment().getMaximumWindowBounds().width);
	    stage.sizeToScene(); 
	    stage.show(); 	
	}

	protected ObservableList<Status> check(ObservableList<Status> items) {
		return getApps();
	}

	private ObservableList<Status> getApps() {
		try{
			ObservableList<Status> list = FXCollections.observableArrayList();
			DBCollection coll = Database.getCol("Apparatus", "Status");
			BasicDBObject obj = new BasicDBObject();
			DBCursor curs;
			obj = new BasicDBObject("Comment", 
					Pattern.compile(cadid,
							Pattern.CASE_INSENSITIVE));
			curs = coll.find(obj);
			do{	
				curs.next();
				list.add(new Status(curs.curr()));
			}
			while(curs.hasNext());
			return list;
		}
		catch(NoSuchElementException e){
			return FXCollections.observableArrayList();
		}
	}
}