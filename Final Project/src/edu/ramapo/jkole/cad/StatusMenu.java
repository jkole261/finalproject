package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class StatusMenu extends Application {
	TableView<Status> table;
	static Stage stage;
	
	public StatusMenu(){
		try{
			if(!(stage.isShowing())){return;}
		}
		catch(NullPointerException e2){
			try {
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1000, 600, Color.ANTIQUEWHITE);
		
		MenuBar mainmenu = new MenuBar();
		
		Menu file = new Menu("_File");
		MenuItem pref = new MenuItem("Preferences");
		MenuItem print = new MenuItem("Print");
		MenuItem exit = new MenuItem("Exit");
		
		print.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		exit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		
		file.getItems().addAll(print, pref, exit);
		
		mainmenu.getMenus().addAll(file);
		
		print.setOnAction(actionEvent -> print(stage));
		exit.setOnAction(actionEvent -> stage.close());
		
		VBox vbox = new VBox();
		
		vbox.getChildren().add(getStatusTable());
		
		root.setCenter(vbox);
		root.setTop(mainmenu);
		stage.setTitle("Status Menu"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment
        		.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show(); 
	}

	private void print(Stage stage) {
		PrinterJob printerJob = PrinterJob.createPrinterJob();
		   if(printerJob.showPrintDialog(stage.getOwner()) && printerJob.printPage(table))
		       printerJob.endJob();
	}

	@SuppressWarnings("unchecked")
	private Node getStatusTable() {
		VBox temp = new VBox();
		temp.setSpacing(5);
        temp.setPadding(new Insets(10, 10, 10, 10));
		table = new TableView<Status>();
		
		TableColumn<Status, String> AppUnit = new TableColumn<Status, String>("Unit");
		TableColumn<Status, String> avail = new TableColumn<Status, String>("Avail");
		TableColumn<Status, String> enrt = new TableColumn<Status, String>("Enrt");
		TableColumn<Status, String> onsce = new TableColumn<Status, String>("Onloc");
		TableColumn<Status, String> busy = new TableColumn<Status, String>("Busy");
		TableColumn<Status, String> act = new TableColumn<Status, String>("Active");
		TableColumn<Status, String> time = new TableColumn<Status, String>("TimeStamp");
		TableColumn<Status, String> comm = new TableColumn<Status, String>("Comment");
		
		AppUnit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Status, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Status, String> param) {
                return new SimpleStringProperty(param.getValue().getAppstring());
            }
        });
		avail.setCellValueFactory(
				new PropertyValueFactory<Status, String>("avail"));
		enrt.setCellValueFactory(
				new PropertyValueFactory<Status, String>("enrt"));
		onsce.setCellValueFactory(
				new PropertyValueFactory<Status, String>("onscene"));
		busy.setCellValueFactory(
				new PropertyValueFactory<Status, String>("busy"));
		act.setCellValueFactory(
				new PropertyValueFactory<Status, String>("active"));
		time.setCellValueFactory(c ->
				new SimpleStringProperty(getFormatedTime(c.getValue().getTimestamp())));
		comm.setCellValueFactory(
				new PropertyValueFactory<Status, String>("comment"));
		
		 table.setRowFactory( tv -> {
	            TableRow<Status> row = new TableRow<Status>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 3 && (! row.isEmpty()) ) {
	                    Database.remove("Apparatus", "Status", row.getItem().getOid());
	                    refresh();
	                }
	            });
	            return row ;
	        });
		
		table.setItems(getStatus());
		table.getColumns().addAll(AppUnit, avail, enrt, onsce, busy, act, time, comm);
		temp.getChildren().add(table);
		
		return temp;
	}

	private String getFormatedTime(String timestamp) {
		return timestamp.substring(0, 4)+"/"+timestamp.substring(4, 6)+"/"+
				timestamp.substring(6, 8)+" "+timestamp.substring(9, 11)+":"+
				timestamp.substring(11, 13)+":"+timestamp.substring(13, 15)+
				"."+timestamp.substring(15);
	}
	private void refresh(){
		table.getItems().clear();
		table.setItems(getStatus());
	}
	private ObservableList<Status> getStatus() {
		ObservableList<Status> obl = FXCollections.observableArrayList();
		List<DBObject> foundDocument = Database.client.getDB("Apparatus").getCollection("Status").find().toArray();
		for(int i = 0; i < foundDocument.size(); i++){
			BasicDBObject temp = (BasicDBObject) foundDocument.get(i);			
			Status tstat = new Status(
					temp.getString("_id"),
					Boolean.parseBoolean(temp.getString("active")),
					Boolean.parseBoolean(temp.getString("enrt")),
					Boolean.parseBoolean(temp.getString("onscene")),
					Boolean.parseBoolean(temp.getString("avail")),
					Boolean.parseBoolean(temp.getString("busy")), 
					temp.get("TimeStamp").toString(),
					temp.getString("Comment"),
					temp.getString("Apparatus"));		
			obl.add(tstat);
		}
		return obl;
	}
}