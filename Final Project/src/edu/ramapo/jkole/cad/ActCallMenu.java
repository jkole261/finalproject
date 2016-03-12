package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import edu.ramapo.jkole.alerting.AlertCheck;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ActCallMenu extends Application{
	static TableView<Call> table;
	static Stage stage;
	
	public ActCallMenu(){	
		try{
			if((stage.isShowing())){return;}
			else if(!(stage.isShowing())){
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
	
	@SuppressWarnings("unchecked")
	public void start(Stage stage) throws Exception {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 600, 400, Color.ANTIQUEWHITE);
		stage.setTitle("Active Calls");
		
		table = new TableView<Call>();	
		VBox mbox = new VBox();
		
		TableColumn<Call, String> actid = new TableColumn<Call, String>("ActID");
		TableColumn<Call, String> loc = new TableColumn<Call, String>("Address");
		TableColumn<Call, String> type = new TableColumn<Call, String>("Nature");
		TableColumn<Call, String> stat = new TableColumn<Call, String>("Status");
		TableColumn<Call, String> city = new TableColumn<Call, String>("City");
		
		table.getColumns().addAll(actid, loc, type, stat, city);

		table.setRowFactory( tv -> {
            TableRow<Call> row = new TableRow<Call>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	new FireCallScreen(row.getItem());
                }
            });
            return row;
		});
		
		new Thread(new Runnable() 
	    { 
	      public void run() 
	      { 
	        do { 
	          try{
	        	  int a = table.getSelectionModel().getSelectedIndex();
	        	  table.setItems(check(table.getItems()));
	        	  table.getSelectionModel().select(a);
	        	  Thread.sleep(10000);
	          }  
	          catch(IllegalStateException | InterruptedException e){
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
		
		mbox.getChildren().add(table);
		root.setCenter(mbox);
	    stage.setScene(scene);
	    stage.setMaxWidth(GraphicsEnvironment
       		.getLocalGraphicsEnvironment()	
       		.getMaximumWindowBounds().width);
	    stage.sizeToScene(); 
	    stage.show();
	    
	    AlertCheck chk = new AlertCheck(Main.pro.getAgency());
	}
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

	private Call getCall(int j) {
		DBCollection coll = Database.getCol("Calls", "basicInfo");
		List<DBObject> foundDocument = coll.find(
				new BasicDBObject("actid", new BasicDBObject("$gte", "0000"))).toArray();		
		Call temp = new Call((BasicDBObject) foundDocument.get(j));
		temp.setStatus(Call.getStatus(temp.getCall().get("cadid")));
		return temp;
	}

	public static Call getSelectedCall(){
		return table.getSelectionModel().getSelectedItem();
	}
	
	@Override
	public void stop(){
	    System.out.println("Stage is closing");
	}
	
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