/**/
/** ActCallMenu.java
 * 
 * @author Jason Kole
 * 
 * The ActCallMenu or Active Call Menu, is a javaFX application that when started
 * will display all current calls that are in the system and occuring at the current
 * time. This screen has the ability to go into another screen, (FireCallScreen) for more details.
 **/
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ActCallMenu extends Application{
	static TableView<Call> table;
	public static Stage stage;
	
	public ActCallMenu(){	
		try{
			if((stage.isShowing())){return;}
			else if(!(stage.isShowing())){
				getCalls();
				stage.show();
			}
		}
		catch(NullPointerException e2){
			stage = new Stage();
			try {
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActCallMenu.start()
	 * SYNOPSIS
	 * 		this.ActCallMenu	->	this JavaFX Application 
	 * DESCRIPTION
	 * 		starts and creates the visual application of the ActiveCallMenu within this
	 * 		menu there is a thread that runs and looks for new information. within this 
	 * 		function the thread every 10 seconds looks for new calls that have been added
	 * 		since the last refresh.
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void start(Stage stage) throws Exception {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 600, 400, Color.ANTIQUEWHITE);
		try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		
		stage.setTitle("Active Calls");
		
		table = new TableView<Call>();	
		VBox mbox = new VBox();
		
		TableColumn<Call, String> actid = new TableColumn<Call, String>("ActID");
		TableColumn<Call, String> loc = new TableColumn<Call, String>("Address");
		TableColumn<Call, String> type = new TableColumn<Call, String>("Nature");
		TableColumn<Call, String> stat = new TableColumn<Call, String>("Status");
		TableColumn<Call, String> city = new TableColumn<Call, String>("City");
		
		table.getColumns().addAll(actid, loc, type, stat, city);

		table.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
			@Override
			  public Boolean call(ResizeFeatures param) {
			     return true;
			  }
			});
		
		table.setRowFactory( tv -> {
            TableRow<Call> row = new TableRow<Call>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	new FireCallScreen(row.getItem());
                }
            });
            return row;
		});
		
		new Thread(new Runnable(){ 
			public void run(){ 
				do { 
					try{
						int a = table.getSelectionModel().getSelectedIndex();
						table.setItems(check(table.getItems()));
						table.getSelectionModel().select(a);
						Thread.sleep(10000);
					} catch(IllegalStateException | InterruptedException e){
						System.err.println("ERROR ON ACTIVE CALL THREAD");
					} 
				} 
				while(stage.isShowing());
			} 
		}).start();
			
		actid.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty(param.getValue().getCall().get("actid"));
            }
        });
		loc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty(param.getValue().getCall().get("addr").split(",")[0]);
            }
        });
		type.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty(param.getValue().getCall().get("nature"));
            }
        });
		city.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty(param.getValue().getCall().get("city"));
            }
        });
		stat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty(param.getValue().getStatus());
            }
        });
		
		MenuItem clear = new MenuItem("Clear Call");
		clear.setMnemonicParsing(true);
		
		clear.setOnAction(actionEvent -> {
			Call.clearCall(table.getSelectionModel().getSelectedItem());
		});
		
		table.setContextMenu(new ContextMenu(clear));
		
		mbox.getChildren().add(table);
		root.setCenter(mbox);
	    stage.setScene(scene);
	    stage.setMaxWidth(GraphicsEnvironment
       		.getLocalGraphicsEnvironment()	
       		.getMaximumWindowBounds().width);
	    stage.sizeToScene(); 
	    stage.show();
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActCallMenu.check()
	 * SYNOPSIS
	 * 		ObservableList<Call> calls	->	current list of Calls
	 * DESCRIPTION
	 * 		every call of this function checks the size of calls against the
	 * 		size of the database. if the sizes are different then it will set
	 * 		the current ObservableList to what is currently in the database.
	 * 		if the list is the same size then the function returns null else 
	 * 		will return the new list.
	 * RETURNS
	 * 		ObservableList<Call> calls; null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	protected ObservableList<Call> check(ObservableList<Call> calls) {
		if(calls.isEmpty()){
			calls = getCalls();
		}
		else{
			ObservableList<Call> cs = getCalls();
			if(calls.size() == cs.size()){
				int i = 0;
				for(Call c : calls){
					if(c.getCall().equals(cs.get(i).getCall())){		
						c.setStatus(Call.getStatus(c.getCall().get("cadid")));
						calls.set(i, getCall(i));
					}
					i++;
				}
				return calls;	
			}
			else{
				calls = getCalls();
			}
		}
		return calls;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActCallMenu.check()
	 * SYNOPSIS
	 * 		ObservableList<Call> calls	->	current list of Calls
	 * DESCRIPTION
	 * 		every call of this function checks the size of calls against the
	 * 		size of the database. if the sizes are different then it will set
	 * 		the current ObservableList to what is currently in the database.
	 * 		if the list is the same size then the function returns null else 
	 * 		will return the new list.
	 * RETURNS
	 * 		ObservableList<Call> calls; null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private Call getCall(int j) {
		DBCollection coll = Database.getCol("Calls", "basicInfo");
		List<DBObject> foundDocument = coll.find(
				new BasicDBObject("actid", new BasicDBObject("$gte", "0000"))).toArray();		
		Call temp = new Call((BasicDBObject) foundDocument.get(j));
		temp.setStatus(Call.getStatus(temp.getCall().get("cadid")));
		return temp;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActCallMenu.getSelectedCall()
	 * SYNOPSIS
	 * 		TableView<Call> table	->	table that user will be able to 
	 * 										select call from.
	 * DESCRIPTION
	 * 		gets the selected item from the table at the time of 
	 * 		function call.
	 * RETURNS
	 * 		Call table.getSelectionModel().getSelectedItem();
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public static Call getSelectedCall(){
		return table.getSelectionModel().getSelectedItem();
	}
	
	@Override
	public void stop(){
	    System.out.println("Stage is closing");
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.ActCallMenu.getCalls()
	 * SYNOPSIS
	 * 		ObservableList<Call> date	->	list that will contain all 
	 * 											current calls.
	 * 		DBCollection Coll			->	collection that gets all information 
	 * 											from the database.
	 * DESCRIPTION
	 * 		On function calls this searches the database of current calls for
	 * 		calls that hace an activeid number of greater then 0, which 
	 * 		means they are active.
	 * RETURNS
	 * 		ObservableList<Call> dat	->	observable list containing all calls
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private ObservableList<Call> getCalls() {
		ObservableList<Call> dat = FXCollections.observableArrayList(); 
		DBCollection coll = Database.getCol("Calls", "basicInfo");
		List<DBObject> foundDocument = coll.find(new BasicDBObject("actid", new BasicDBObject("$gte", "0000"))).toArray();
		for(int i = 0; i < foundDocument.size(); i++){
			Call temp = new Call((BasicDBObject) foundDocument.get(i));
			temp.setStatus(Call.getStatus(temp.getCall().get("cadid")));
			dat.add(temp);
		}
		return dat;
	}
}