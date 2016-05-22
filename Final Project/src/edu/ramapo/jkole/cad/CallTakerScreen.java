/****/
/** CallTakerScreen.java
 * 
 * @author Jason Kole
 * 
 * The calltakerscreen is a javaFX application that is one of the main focuses
 * of the dispatch system. within this screen the user will be able to input data
 * into the database as well as upgrade existing calls within the system. WARNING 
 * currently there is no protection against editing an old call.
 **/
/****/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoTimeoutException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class CallTakerScreen extends Application {
	public static Stage stage;
	HashMap<String, String> callinfo;
	TextField cadid;TextField actid;
	ComboBox<Nature> nature;
	TextField type;TextField pri;
	TextField addr;
	TextField city;
	TextField addr2;
	TextField pzone;TextField fzone;TextField ezone;
	TextField det;TextField alarm;
	TextArea dir;
	TextField contact;TextField contNumber;
	TextField address;
	TextField info;
	TextField lp;TextField st;
	TextField calls;TextField dups;TextField names;TextField alerts;
	TextField rechw;
	TextField date1;TextField date2;
	TextField recby;
	String ntext;
	List<DBObject> actCall;
	int index;
	
	public CallTakerScreen(){
		try{
			if((stage.isShowing())){return;}
			else if(!(stage.isShowing())){
				clearScreen();
				stage.show();
			}
		}
		catch(NullPointerException e2){
			getCalls();
			cadid = new TextField();
			actid = new TextField();
			nature  = new ComboBox<Nature>();
			type = new TextField();
			pri = new TextField();
			addr = new TextField();
			city = new TextField();
			addr2 = new TextField();
			pzone = new TextField();
			fzone = new TextField();
			ezone = new TextField();
			det = new TextField();
			alarm = new TextField();
			dir = new TextArea();
			contact = new TextField();
			contNumber = new TextField();
			address = new TextField();
			info = new TextField();
			lp = new TextField();
			st = new TextField();
			calls = new TextField();
			dups = new TextField();
			names = new TextField();
			alerts = new TextField();
			rechw = new TextField();
			date1 = new TextField();
			recby = new TextField();
			date2 = new TextField();
			stage = new Stage();
			try {
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void getCalls() {
		try{
			actCall = Database.getCol("Calls", "basicInfo").find().toArray();
			index = actCall.size();
		} catch (MongoTimeoutException e){
			System.err.println(e.getMessage());
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallTakerScreen.start()
	 * SYNOPSIS
	 * 		
	 * DESCRIPTION
	 * 		creates a GUI for dispatchers to input information and 
	 * 		create new calls.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	@Override
	public void start(Stage stage) throws Exception {
		BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 1000, 700, Color.ANTIQUEWHITE);
    	try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} 	
        VBox top = new VBox();  
        MenuBar mainMenu = getMenus();  
        ToolBar toolBar = getToolBar();
        ToolBar toolBar2 = getToolBar2();
        
        top.getChildren().addAll(mainMenu, toolBar, toolBar2);
		root.setTop(top);
		
		root.setCenter(getCenter());
		
		stage.setTitle("Call Taker Screen"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show();
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallTakerScreen.getCenter()
	 * SYNOPSIS
	 * 		
	 * DESCRIPTION
	 * 		adds information to the main stage (GUI)
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private Node getCenter() throws IOException {
		VBox callinfo = new VBox();
		callinfo.setPadding(new Insets(15, 15, 5, 15));
		
		pzone.setMaxWidth(75);
		fzone.setMaxWidth(75);
		ezone.setMaxWidth(75);
		
		alerts.setFocusTraversable(false);
		type.setFocusTraversable(false);
		pri.setFocusTraversable(false);
		city.setFocusTraversable(false);
		pzone.setFocusTraversable(false);
		fzone.setFocusTraversable(false);
		ezone.setFocusTraversable(false);
		det.setFocusTraversable(false);
		alarm.setFocusTraversable(false);
		dir.setFocusTraversable(false);
		calls.setFocusTraversable(false);
		dups.setFocusTraversable(false);
		names.setFocusTraversable(false);
		
		HBox hb1 = new HBox();
		cadid.setPromptText("##-######");
		cadid.setFocusTraversable(false);
		hb1.getChildren().addAll(new Label("CAD Number:"), cadid);
		
		HBox hb2 = new HBox();
		hb2.setSpacing(5);
		type.setPrefWidth(75);
		pri.setPrefWidth(75);
		actid.setPromptText("###");
		actid.setDisable(true);
		hb2.getChildren().addAll(new Label("Active ID"), actid, 
				new Label("Nature"), nature, new Label("Type"), 
				type, new Label("Priority"), pri);
		
		HBox hb3 = new HBox();
		addr.setPrefWidth(450);
		hb3.setSpacing(5);
		hb3.getChildren().addAll(new Label("Address"), addr, new Label("City"), city);
		
		HBox hb4 = new HBox();
		addr2.setPrefWidth(600);
		hb4.getChildren().add(addr2);	
		
		HBox hb5 = new HBox();
		hb5.setSpacing(10);
		hb5.getChildren().addAll(new Label("Zones"), new Label("P: "), pzone, 
				new Label("F: "), fzone, new Label("E: "), ezone,
				new Label("Determinate"), det, new Label("Alarm"), alarm);
		
		HBox hb6 =  new HBox();
		dir.setMaxHeight(75);
		hb6.getChildren().addAll(new Label("Directions"), dir);
		
		HBox hb7 = new HBox();
		hb7.getChildren().addAll(new Label("Contact"), contact, 
				new Label("Tel"), contNumber);
		
		HBox hb8 = new HBox();
		hb8.getChildren().addAll(new Label("Address"), address);
		
		HBox hb9 = new HBox();
		info.setPrefWidth(650);
		hb9.getChildren().addAll(new Label("Info"), info);
		
		HBox hb10 = new HBox();
		hb10.getChildren().addAll(new Label("License Plate"), lp, 
				new Label("State"), st);
		
		HBox hb11 = new HBox();
		hb11.getChildren().addAll(new Label("Calls"), calls, 
				new Label("Dupl"), dups, new Label("Names"), names, 
				new Label("Alerts"), alerts);
		
		HBox hb12 = new HBox();
		hb12.getChildren().addAll(new Label("How Rcvd"), rechw, 
				new Label("Occured Between"), date1);
		
		HBox hb13 = new HBox();
		hb13.getChildren().addAll(new Label("Rcvd By"), recby, 
				new Label("And"), date2);
		
		nature.setItems(getNature());
		new AutoComboBox(nature);
		nature.valueProperty().addListener(new ChangeListener<Nature>() {
			@Override
			public void changed(ObservableValue<? extends Nature> observable,
					Nature oldValue, Nature newValue) { 
				ntext = "";
				try{
				ntext = newValue.getNature();
					if(!(newValue.getDisptype().isEmpty() || newValue.getPriority().isEmpty())){
						type.setText(newValue.getDisptype());
						pri.setText(newValue.getPriority());
					}
					else{ return; }
				}
				catch(Exception e){ }}    
	      });
		nature.setConverter(new StringConverter<Nature>() {
		    @Override
		    public String toString(Nature object) {
		        if (object == null) return null;
		        return object.toString();
		    }
		    @Override
		    public Nature fromString(String string) {
		        return new Nature(string);
		    }
		});
		callinfo.getChildren().addAll(hb1, hb2, hb3, hb4, hb5,
				hb6, hb7, hb8, hb9, hb10, hb11, hb12, hb13);
		
		callinfo.setSpacing(10);
		return callinfo;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallTakerScreen.getNature()
	 * SYNOPSIS
	 * 		
	 * DESCRIPTION
	 * 		compares two natures, then returns a observable list 
	 * 		of all the natures in the natures.dat file
	 * RETURNS
	 * 		ObservableList<Nature> nature -> list of natures
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private ObservableList<Nature> getNature() throws IOException {
		Comparator<? super Nature> compareNat = new Comparator<Nature>() {
	        @Override
	        public int compare(Nature o1, Nature o2) {
	            return o1.getNature().compareToIgnoreCase(o2.getNature());
	        }
		};
		ObservableList<Nature> nature = 
				FXCollections.observableArrayList();
		FileReader input = new FileReader("lib/natures.dat");
		BufferedReader bufRead = new BufferedReader(input);
		String line = null;
		while ( (line = bufRead.readLine()) != null) { 
			String[] t = line.split(",");
			t[1] = t[1].substring(1);
			t[2] = t[2].replaceAll("\\s+", "");
			nature.add(new Nature(t[0],t[1],t[2]));
	    	}
        Collections.sort(nature, compareNat);
		bufRead.close();
		return nature;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallTakerScreen.createRadioLog(Stirng s)
	 * SYNOPSIS
	 * 		String s -> Unit Number for radio log
	 * DESCRIPTION
	 * 		creates a GUI to insert a radio log for apparatus s, 
	 * 		then calls ApparatusDispatch.rlog
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	protected static void createRadioLog(String s){
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Create Radio Log");
		dialog.setHeaderText("Enter Unit and comment for RLog");
		dialog.setContentText("Unit: ");

		ButtonType loginButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField unit = new TextField();
		unit.setText(s);
		TextField comm = new TextField();
		comm.setPrefWidth(200);
		comm.setPromptText("Comment (location, time until, etc.)");
		
		grid.add(new Label("Unit:"), 0, 0);
		grid.add(unit, 1, 0);
		grid.add(new Label("Comment:"), 0, 1);
		grid.add(comm, 1, 1);

		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(unit.getText(), comm.getText());
		    }
		    return null;
		});

		dialog.getDialogPane().setContent(grid);
		
		unit.setFocusTraversable(false);
		
		comm.requestFocus();
		
		Optional<Pair<String, String>> result = dialog.showAndWait();
		result.ifPresent(unitCom -> {
		    ApparatusDispatch.rlog(unitCom);
		});
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallTakerScreen.createRadioLog()
	 * SYNOPSIS
	 * 		 
	 * DESCRIPTION
	 * 		creates a GUI to insert a radio log for specified apparatus, 
	 * 		then calls ApparatusDispatch.rlog
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	protected static void createRadioLog() {
		//set unit as busy with comment
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Create Radio Log");
		dialog.setHeaderText("Enter Unit and comment for RLog");
		dialog.setContentText("Unit: ");

		ButtonType loginButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField unit = new TextField();
		unit.setPromptText("Unit");
		TextField comm = new TextField();
		comm.setPrefWidth(200);
		comm.setPromptText("Comment (location, time until, etc.)");
		
		grid.add(new Label("Unit:"), 0, 0);
		grid.add(unit, 1, 0);
		grid.add(new Label("Comment:"), 0, 1);
		grid.add(comm, 1, 1);

		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(unit.getText(), comm.getText());
		    }
		    return null;
		});

		dialog.getDialogPane().setContent(grid);
		
		Optional<Pair<String, String>> result = dialog.showAndWait();
		result.ifPresent(unitCom -> {
		    System.out.println("Unit=" + unitCom.getKey() + ", Comment=" + unitCom.getValue());
		    ApparatusDispatch.rlog(unitCom);
		});
		
	}
	private void new911() {
		clearScreen();
		rechw.setText("9");
		recby.setText(Login.getUser());
	}
	private ToolBar getToolBar() {
		ToolBar temp = new ToolBar();
		
		Button exit = new Button("_Exit");
		exit.setTooltip(new Tooltip("Exit"));
		exit.setOnAction(actionEvent -> stage.close());
		
		Button srch = new Button("_Srch");
		srch.setTooltip(new Tooltip("Search"));
		srch.setOnAction(actionEvent -> search(getInfo()));
		
		Button mod = new Button("_Mod");
		mod.setTooltip(new Tooltip("Modify"));
		mod.setOnAction(actionEvent -> modifyEvent(getCallInfo()));
		
		Button add = new Button("_Add");
		if(cadid.getText().equalsIgnoreCase(null)){add.setDisable(true);}
		add.setTooltip(new Tooltip("Submit Call"));
		add.setOnAction(actionEvent -> addNewCall());
		
		Button clr = new Button("_Clr");
		clr.setTooltip(new Tooltip("Clear Call Screen"));
		clr.setOnAction(actionEvent -> clearScreen());
		
		Button del = new Button("_Del");
		del.setTooltip(new Tooltip("Delete Call"));
		del.setOnAction(actionEvent -> deleteCurrent());
		
		Button lst = new Button("_List");
		lst.setTooltip(new Tooltip("List Active Calls"));
		lst.setOnAction(actionEvent -> new ActCallMenu());
		
		Button prt = new Button("_Prnt");
		prt.setTooltip(new Tooltip("Print Current Call"));
		prt.setDisable(true);
		prt.setOnAction(actionEvent -> System.out.println("NO EVENT YET"));
		
		Button fwd = new Button("_Fwd");
		fwd.setTooltip(new Tooltip("Foward"));
		fwd.setOnAction(actionEvent -> nextCall());
		
		Button bck = new Button("_Bck");
		bck.setTooltip(new Tooltip("Back"));
		bck.setOnAction(actionEvent -> prevCall());
		
		temp.getItems().addAll(exit, srch, mod, add, clr, del, lst, prt, 
				 new Separator(), bck, fwd);
		
		exit.setFocusTraversable(false);
		srch.setFocusTraversable(false);
		mod.setFocusTraversable(false);
		add.setFocusTraversable(false);
		clr.setFocusTraversable(false);
		del.setFocusTraversable(false);
		lst.setFocusTraversable(false);
		prt.setFocusTraversable(false);
		bck.setFocusTraversable(false);
		fwd.setFocusTraversable(false);
		
		return temp;
	}
	private ToolBar getToolBar2() {
		ToolBar temp = new ToolBar();
		
		Button use = new Button("Use");
		use.setTooltip(new Tooltip("Update Unit Usage"));
		use.setOnAction(actionEvent -> new UsageLog(cadid.getText()));
		
		Button invl = new Button("Invl");
		invl.setTooltip(new Tooltip("Call Involvement"));
		invl.setOnAction(actionEvent -> {
			try{
				new FireCallScreen(cadid.getText());
			}
			catch(Exception e){ }
		});
		
		Button files = new Button("Files");
		files.setTooltip(new Tooltip("Files"));
		files.setDisable(true);
		files.setOnAction(actionEvent -> System.out.println("NO EVENT YET"));
		
		Button rlog = new Button("RLog");
		rlog.setTooltip(new Tooltip("New Radio Log"));
		rlog.setOnAction(actionEvent -> createRadioLog());
		
		Button n911 = new Button("911");
		n911.setTooltip(new Tooltip("New Call From 911"));
		n911.setOnAction(actionEvent -> new911());
		
		Button hist = new Button("Hist");
		hist.setTooltip(new Tooltip("History"));
		hist.setOnAction(actionEvent -> search(addr.getText()));
		
		temp.getItems().addAll(use, invl, files, rlog, n911, hist);
		
		use.setFocusTraversable(false);
		invl.setFocusTraversable(false);
		files.setFocusTraversable(false);
		rlog.setFocusTraversable(false);
		n911.setFocusTraversable(false);
		hist.setFocusTraversable(false);
		
		return temp;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallTakerScreen.search(Stirng text)
	 * SYNOPSIS
	 * 		String test -> searches for address
	 * DESCRIPTION
	 * 		searches the Calls.basicInfo table for all calls consisting of address text
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	protected void search(String text) {
		List<Call> list = new ArrayList<Call>();
		DBCursor curs = Database.getCol("Calls", "basicInfo").find(new BasicDBObject("addr", searchAddr(text)));
	   	while(curs.hasNext()){
	   		curs.next();
	   		System.out.println(curs.curr());
	   		list.add(new Call((BasicDBObject)curs.curr()));
	   	}
		new CallStack(list, this);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallTakerScreen.search(HashMao<String, String> info)
	 * SYNOPSIS
	 * 		info -> hashmap of all the information of the call
	 * DESCRIPTION
	 * 		searches the Calls.basicInfo table for all calls consisting of the 
	 * 		information in the hashmap
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private void search(HashMap<String, String> info) {
		List<Call> list = new ArrayList<Call>();
		for (Iterator<Map.Entry<String, String>> iter = 
				info.entrySet().iterator();
			    iter.hasNext();) {
					Map.Entry<String, String> entry = iter.next();
					if ("".equals(entry.getValue()) || entry.getValue() == null) {
						iter.remove();
					}
		} 
	   	DBCursor curs = Database.getCol("Calls", "basicInfo").find(new BasicDBObject(info));
	   	while(curs.hasNext()){
	   		curs.next();
	   		list.add(new Call((BasicDBObject)curs.curr()));
	   	}
		new CallStack(list, this);
	}
	private void modifyEvent(BasicDBObject object) {
		object = getNewInfo(object);
		Database.update("Calls", "basicInfo", object, object.getString("_id"));
	}
	private BasicDBObject getNewInfo(BasicDBObject object) {
		validateAddr(addr.getText());
		object.put("nature", ntext);
		object.put("cadid", cadid.getText());
		object.put("actid", actid.getText());
		object.put("type", type.getText());
		object.put("pri", pri.getText());	
		object.put("addr", addr.getText());
		object.put("city", city.getText());
		object.put("addr2", addr2.getText());
		object.put("pzone", pzone.getText());
		object.put("fzone", fzone.getText());
		object.put("ezone", ezone.getText());
		object.put("deter", det.getText());
		object.put("alarm", alarm.getText());
		object.put("dir", dir.getText());
		object.put("contName", contact.getText());
		object.put("contPhone", contNumber.getText());
		object.put("contAddr", address.getText()); 
		object.put("callInfo", info.getText());
		object.put("LicPlate", lp.getText());
		object.put("LicPlateSt", st.getText());
		object.put("contName", contact.getText());
		object.put("contPhone", contNumber.getText());
		object.put("contAddr", address.getText());
		object.put("callInfo", info.getText());
		
		Call c = new Call(object);
		object.put("Calls", "");
		object.put("dups", Call.checkDups(c));
		object.put("Names", "");
		object.put("Alerts", Call.checkAlert(c)); 
		
		return object;
	}
	private void deleteCurrent() {
		if(Database.remove("Calls", "basicInfo", actCall.get(index).get("_id").toString())){
			actCall.remove(index);
			clearScreen();
		}
	}
	private void prevCall() {
		if(index > 0){
			index--;
			clearScreen();
			setScreen(actCall.get(index));
		}
		else{
			System.out.println("INDEX TO LOW");
		}
	}
	private void nextCall() {
		try{
			if(index < actCall.size()){
				index++;
				clearScreen();
				setScreen(actCall.get(index));
			}
			else{
				System.out.println("INDEX TO HIGH");
			}
		} catch(IndexOutOfBoundsException e) {
			clearScreen();
			System.out.println("NO NEW");
		}
	}
	protected void setScreen(DBObject dbObject) {
		cadid.setText(dbObject.get("cadid").toString());
		actid.setText(dbObject.get("actid").toString());
		type.setText(dbObject.get("type").toString());
		pri.setText(dbObject.get("pri").toString());
		nature.setValue(new Nature(dbObject.get("nature").toString()));
		addr.setText(dbObject.get("addr").toString());
		city.setText(dbObject.get("city").toString());
		addr2.setText(dbObject.get("addr2").toString());
		calls.setText(dbObject.get("Calls").toString());
		dups.setText(dbObject.get("Dups").toString());
		alerts.setText(dbObject.get("Alerts").toString());
	}
	private void clearScreen() {
		cadid.clear();actid.clear();
		type.clear();pri.clear();
		addr.clear();city.clear();
		addr2.clear();pzone.clear();
		fzone.clear();ezone.clear();
		det.clear();alarm.clear();
		dir.clear();contact.clear();
		contNumber.clear();address.clear();
		info.clear();lp.clear();
		st.clear();calls.clear();
		dups.clear();names.clear();
		alerts.clear();rechw.clear();
		date1.clear();date2.clear();
		recby.clear();nature.getSelectionModel().select(null);
	}
	private void addNewCall() {
		 validateAddr(addr.getText());
		 callinfo = getNewCallInfo();
		 Call c = new Call(callinfo);
		 Call.addCall(c);
		 actid.setText(c.getCall().get("actid"));
		 cadid.setText(c.getCall().get("cadid"));
		 dups.setText(c.getCall().get("dups"));
		 alerts.setText(c.getCall().get("alerts"));
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.CallTakerScreen.validateAddr(String text)
	 * SYNOPSIS
	 * 		String text -> address to be formatted 
	 * DESCRIPTION
	 * 		uses Googles geocoder to search for the 
	 * 		closest address to text and then returns 
	 * 		the value of the formatted address.
	 * RETURNS
	 * 		null
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	private void validateAddr(String text) {
		try {
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(text+", NJ")
					.setLanguage("en").getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			addr.setText(geocoderResponse.getResults().get(0).getFormattedAddress());
			city.setText(Municipality.getCodeFromDB("Municipality",addr.getText().split(",")[1].trim()));
		} catch (IOException e) {
			System.err.print(e);
		}
	}	
	protected String searchAddr(String text) {
		try {
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(text+", NJ").setLanguage("en").getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			System.out.println(geocoderResponse.getResults().get(0).getFormattedAddress());
			return geocoderResponse.getResults().get(0).getFormattedAddress();
		} catch (IOException e) {
			System.err.print(e);
		}
		return "";
	}
	private HashMap<String, String> getInfo() {
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("cadid", cadid.getText());
		temp.put("nature", ntext);
		temp.put("type", type.getText());
		temp.put("pri", pri.getText());
		temp.put("addr", addr.getText());
		temp.put("city", city.getText());
		temp.put("addr2", addr2.getText());
		temp.put("dir", dir.getText());
		temp.put("contName", contact.getText());
		temp.put("contPhone", contNumber.getText());
		temp.put("contAddr", address.getText());
		temp.put("callInfo", info.getText());
		temp.put("LicPlate", lp.getText());
		temp.put("LicPlateSt", st.getText());
		temp.put("RecType", rechw.getText());
		temp.put("RecBy", recby.getText());
		return temp;
	}
	private HashMap<String, String> getNewCallInfo() {
		String date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("cadid", "");
		temp.put("actid", "");
		temp.put("nature", ntext);
		temp.put("type", type.getText());
		temp.put("pri", pri.getText());
		temp.put("addr", addr.getText());
		temp.put("city", city.getText());
		temp.put("addr2", addr2.getText());
		temp.put("pzone", "");
		temp.put("fzone", "");
		temp.put("ezone", "");
		temp.put("deter", "");
		temp.put("alarm", "");
		temp.put("dir", dir.getText());
		temp.put("contName", contact.getText());
		temp.put("contPhone", contNumber.getText());
		temp.put("contAddr", address.getText());
		temp.put("callInfo", info.getText());
		temp.put("LicPlate", lp.getText());
		temp.put("LicPlateSt", st.getText());
		temp.put("Calls", "");
		temp.put("dups", "");
		temp.put("Names", "");
		temp.put("Alerts", "");
		temp.put("RecType", rechw.getText());
		temp.put("RecBy", recby.getText());
		temp.put("CallTime", date);
		return temp;
	}
	private BasicDBObject getCallInfo() {
		BasicDBObject temp = (BasicDBObject) Database
			.getCol("Calls", "basicInfo")
			.findOne(new BasicDBObject("cadid", cadid.getText()));
		return temp;
	}
	private MenuBar getMenus() {
		MenuBar temp = new MenuBar();
		Menu file = new Menu("_File");
        Menu edit = new Menu("_Edit");
        Menu search = new Menu("_Search");
        Menu tools = new Menu("_Tools");
        Menu help = new Menu("_Help");
        
        MenuItem exit = new MenuItem("Exit");
        exit.setMnemonicParsing(true);
        exit.setOnAction(actionEvent -> stage.close());
        
        file.getItems().addAll(exit);
        
        temp.getMenus().addAll(file, edit, search, tools, help);
		return temp;
	}
	public static void closeStage() {
		stage.close();
	}
}