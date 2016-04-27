/**/
/** ChoiceMenu.java
 * 
 * @author Jason Kole
 * 
 * The choicemenu class is a javafx pop-out menu that displays all the information
 * in a treeview and on a user click will return the ID number of the element 
 * selected. the choice menu retrieves all its data from .dat files previously
 * save within the system.
 * 
 **/
/**/
package edu.ramapo.jkole.cad;

import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChoiceMenu extends Application{
	TextField textfield;
	TreeView<String> tree;
	String string;
	Stage stage;
	
	public ChoiceMenu(TextField field, String string) {
		this.string = string;
		stage = new Stage();
		textfield = field;
		try{
			start(stage);
		}
		catch(Exception e){e.printStackTrace();}
	}
	private TreeView<String> load(String string) {
		return getTreeList(string);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("NFRIS CHOICE MENU");    
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 300, 250);
		try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		
        tree = load(string);
        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem treeItem = (TreeItem)newValue;
                textfield.setText(treeItem.getValue().toString().split(" -- ")[0]);
                stage.close();
            }
        });
        
        root.setTop(getSearchMenu());
        root.setCenter(tree);
        
        stage.setScene(scene);
        stage.show();
	}

	private Node getSearchMenu() {
		// TODO Auto-generated method stub
		TextField text = new TextField();
		Button bt = new Button("Search");
		VBox box = new VBox();
		box.getChildren().addAll(new Label("Search: "), text, bt);
		
		bt.setOnAction(actionEvent -> search(text.getText()));
		
		return box;
	}
	private Object search(String text) {

		return null;
	}
	@SuppressWarnings("unchecked")
	public static TreeView<String> getTreeList(String str) {
		JSONParser parser = new JSONParser();
		TreeItem<String> root = new TreeItem<String>("");
		root.setExpanded(true);
        try {
 
            Object obj = parser.parse(new FileReader(
                    "lib/"+str+".dat"));
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray objs = (JSONArray) jsonObject.get("Types");
			Iterator<JSONObject> iterator = objs.iterator();
            while (iterator.hasNext()) {
            	JSONObject tobj = iterator.next();
            	TreeItem<String> item = new TreeItem<String>(tobj.get("type").toString());
            	item.getChildren().addAll(getChildren(tobj.get("type").toString(), str));
                root.getChildren().add(item);
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        TreeView<String> tree = new TreeView<String>(root); 
        
		return tree;
	}

	@SuppressWarnings("unchecked")
	public static ObservableList<TreeItem<String>> getChildren(String str, String file) {
		JSONParser parser = new JSONParser();
		ObservableList<TreeItem<String>> oblist = FXCollections.observableArrayList();
		try {
            Object obj = parser.parse(new FileReader(
                    "lib/"+file+".dat"));
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray objs = (JSONArray) jsonObject.get(str);
			Iterator<JSONObject> iterator = objs.iterator();
            while (iterator.hasNext()) {
            	JSONObject tobj = iterator.next();
            	TreeItem<String> item = new TreeItem<String>(tobj.get("code").toString()
            			+" -- "+tobj.get("type").toString());
                oblist.add(item);
            }
        } catch (Exception e) { }
		return oblist;
	}
}