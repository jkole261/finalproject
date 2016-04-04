package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.util.List;

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

public class CallStack extends Application{
	Stage stage;
	TableView<Call> table;
	List<Call> list;
	CallTakerScreen cts;
	
	public CallStack(List<Call> list, CallTakerScreen cts1) {
		stage = new Stage();
		this.list = list;
		cts = cts1;
		try {
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public CallStack(List<Call> list) {
		stage = new Stage();
		this.list = list;
		try {
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 600, 400, Color.ANTIQUEWHITE);
		stage.setTitle("Search Results");
		
		table = new TableView<Call>();	
		VBox mbox = new VBox();
		
		TableColumn<Call, String> cadid = new TableColumn<Call, String>("CadID");
		TableColumn<Call, String> addr = new TableColumn<Call, String>("Address");
		TableColumn<Call, String> nature = new TableColumn<Call, String>("Nature");
		TableColumn<Call, String> stat = new TableColumn<Call, String>("Status");
		TableColumn<Call, String> city = new TableColumn<Call, String>("City");
		TableColumn<Call, String> time = new TableColumn<Call, String>("DateTime");
		
		table.getColumns().addAll(cadid, addr, nature, stat, city, time);

		table.setRowFactory( tv -> {
            TableRow<Call> row = new TableRow<Call>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	try{
                		if(cts.stage.isShowing()){
                			cts.setScreen(row.getItem().toDBObj());
                		}
                		else{
                			new FireCallScreen(row.getItem().getCall().get("cadid"));
                		}
                		stage.close();
                	}
                	catch(NullPointerException e){
                		new FireCallScreen(row.getItem().getCall().get("cadid"));
                		stage.close();
                	}
                }
            });
            return row;
		});
		
	    ObservableList<Call> clist = FXCollections.observableArrayList(list);
	    table.setItems(clist);
	  	
		cadid.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty( param.getValue().getCall().get("cadid").toString() );
            }
        });
		addr.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty( param.getValue().getCall().get("addr").toString() );
            }
        });
		nature.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                String str = param.getValue().getCall().get("nature").toString();
                if(str == null){
                	return new SimpleStringProperty("");
                }
            	return new SimpleStringProperty( str );
            }
        });
		stat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty( param.getValue().getStatus() );
            }
        });
		city.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty( param.getValue().getCall().get("city"));
            }
        });
        time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Call, String>, 
				ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Call, String> param) {
                return new SimpleStringProperty( param.getValue().getCall().get("CallTime"));
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
	}
}