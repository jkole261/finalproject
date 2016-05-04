/**/
/**
 * FireCallScreen.java
 * 
 * @author Jason Kole
 * 
 * the FireCallScreen is a javaFX application where there is more indepth information
 * regarding calls. within this screen dispatchers can modify the information and keep
 * time stamps of all important moments within the call.
 */
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FireCallScreen extends Application{
	static Stage stage;
	static String callid = "";
	static Call c;
	static String app;
	boolean editable = true;
	
	BorderPane root; 
	TreeItem<String> applog;
	TextField cadid = new TextField();
	TextField actid = new TextField();
	TextField addr = new TextField();
	TextField city = new TextField();
	TextField nature = new TextField();
	TextField st = new TextField();
	TextField contact = new TextField();
	TextField fzone = new TextField();
	
	TextField cond = new TextField();
	TextField circ = new TextField(); //WHY
	TextField oic = new TextField();
	TextField disp = new TextField(); //disposition
	TextField btype = new TextField(); //building type
	
	TextField paged = new TextField();
	TextField enrt = new TextField();
	TextField arrvd = new TextField();
	TextField ctrld = new TextField();
	TextField clear = new TextField();
	
	public FireCallScreen(){
		stage = new Stage();
		try {
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public FireCallScreen(Call call){
		stage = new Stage();
		c = call;
		callid = c.getCall().get("cadid");
		try{
			start(stage);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public FireCallScreen(String cad){
		stage = new Stage();
		callid = cad;
		c = new Call((BasicDBObject) Database
				.getCol("Calls", "basicInfo")
				.findOne(new BasicDBObject("cadid",cad)));
		try {
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		root = new BorderPane();
    	Scene scene = new Scene(root, 600, 650, Color.ANTIQUEWHITE);
    	try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
    	MenuBar menu = new MenuBar();
    	
    	Menu file = new Menu("_File");
    	Menu edit = new Menu("_Edit");
    	Menu cad = new Menu("_CAD");
    	Menu help = new Menu("_Help");
    	
    	MenuItem enrt = new MenuItem("_ENRT");
		enrt.setMnemonicParsing(true);
		MenuItem onloc = new MenuItem("_ONLOC");
		onloc.setMnemonicParsing(true);
		MenuItem avail = new MenuItem("_AVAIL");
		avail.setMnemonicParsing(true);
		MenuItem oos = new MenuItem("_OOS");
		oos.setMnemonicParsing(true);
		MenuItem busy = new MenuItem("_BUSY");
		busy.setMnemonicParsing(true);
		
		cad.getItems().addAll(enrt, onloc, avail, oos, busy);
    	
    	menu.getMenus().addAll(file, edit, cad, help);
    	   
    	disp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent event) {
    			new ChoiceMenu(disp, "nfrisDispTypes");
    		}
    	});
    	
    	btype.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent event) {
    			new ChoiceMenu(btype, "nfrisPropertyUse");
    		}
    	});
    	
		enrt.setOnAction(actionEvent -> {
			if(!(callid.isEmpty())){
				ApparatusDispatch.enrtApp(callid, app);
			}
		});
		
		onloc.setOnAction(actionEvent -> {
			if(!(callid.isEmpty())){
				ApparatusDispatch.arvdApp(callid, app);
			}
		});
		
		avail.setOnAction(actionEvent -> {
			ApparatusDispatch.setAvail(app);
		});
		
		oos.setOnAction(actionEvent -> ApparatusDispatch
				.setOss(app));
		
		busy.setOnAction(actionEvent -> CallTakerScreen.createRadioLog(app));
    	
    	root.setTop(new VBox(menu, new HBox(new VBox(getToolBar(), getToolBar2()), 
    			new VBox(Clock.getClock()))));
    	root.setCenter(getCallScreen());
    	
		stage.setTitle("Call Incident Screen"); 
	    stage.setScene(scene);
	    stage.setMaxWidth(GraphicsEnvironment
	    		.getLocalGraphicsEnvironment().getMaximumWindowBounds().width);
	    stage.sizeToScene(); 
	    stage.show(); 	
	}
	private HBox getToolBar() {
		HBox box = new HBox();
		ToolBar tool = new ToolBar();

		Button hist = new Button("HIST");
		Button modify = new Button("MOD");
		Button add = new Button("ADD");
		Button clear = new Button("CLR");
		Button delete = new Button("DEL");
		Button print = new Button("PRT");
		Button next = new Button("FWD");
		Button prev = new Button("BCK");
		
		hist.setOnAction(actionEvent -> search(addr.getText()));
		
		add.setOnAction(actionEvent -> new CallTakerScreen());
		
		clear.setOnAction(actionEvent -> {
			Call.clearCall(new Call(callid));
			stage.close();
		});
		
		modify.setOnAction(actionEvent -> {
			
		});
		modify.setDisable(true);
		
		delete.setDisable(true);
		
		print.setDisable(true);
		
		tool.getItems().addAll(hist, modify, add, clear, delete, print, new Separator(),
				prev, next);
		box.getChildren().addAll(tool);
		return box;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.FireCallScreen.search(String text)
	 * SYNOPSIS
	 * 		String text -> address to search
	 * DESCRIPTION
	 * 		pulls the history of all calls with the address of text
	 * RETURNS
	 * 		List of calls
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	protected void search(String text) {
		List<Call> list = new ArrayList<Call>();
		DBCursor curs = Database.getCol("Calls", "basicInfo").find(new BasicDBObject("addr", searchAddr(text)));
	   	while(curs.hasNext()){
	   		curs.next();
	   		list.add(new Call((BasicDBObject)curs.curr()));
	   	}
		new CallStack(list);
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.FireCallScreen.searchAddr(String text)
	 * SYNOPSIS
	 * 		String text -> text to be searched
	 * DESCRIPTION
	 * 		Geocodes address to ensure that address 
	 * 		is of valid format and location
	 * RETURNS
	 * 		String formatted addres
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	public String searchAddr(String text) {
		try {
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(text+", NJ").setLanguage("en").getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			return geocoderResponse.getResults().get(0).getFormattedAddress();
		} catch (IOException e) {
			System.err.print(e);
		}
		return "";
	}
	private Node getToolBar2() {
		HBox box = new HBox();
		
		ToolBar tool = new ToolBar();
		
		Button involv = new Button("INVL");
		Button usage = new Button("USE");
		Button files = new Button("FILES");
		Button narr = new Button("NARR");
		Button rlog = new Button("RLOG");
		Button nfris = new Button("NFIRS");
		Button image = new Button("IMAGE");
		Button upgrd = new Button("UPGRD");
		
		tool.getItems().addAll(involv, usage, files, narr, rlog, nfris, image, upgrd);
		
		usage.setOnAction(actionEvent -> new UsageLog(cadid.getText()));
		narr.setDisable(true);
		files.setDisable(true);

		narr.setOnAction(actionEvent -> {
			TextInputDialog dialog = new TextInputDialog("walter");
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Look, a Text Input Dialog");
			dialog.setContentText("Please enter your name:");

			Optional<String> result = dialog.showAndWait();
			
			result.ifPresent(name -> System.out.println("Your name: " + name));			
		});
		
		involv.setDisable(true);
		nfris.setDisable(true);
		image.setDisable(true);
		
		rlog.setOnAction(actionEvent -> CallTakerScreen.createRadioLog());
		
		upgrd.setOnAction(actionEvent -> {
			ApparatusDispatch.upgrade();
		});
		
		box.getChildren().add(tool);
		
		return box;
	}
	
	public void clear() {
		stage.close();
	}
	
	private Node getCallScreen() {
		VBox main = new VBox();
		HBox header = new HBox();
		HBox header2 = new HBox();
		HBox header3 = new HBox();
		VBox box = new VBox();

		header.getChildren().add(new Label("Call Information:"));
		header.setStyle("-fx-background-color: #d3d3d3;");
		header.setPadding(new Insets(10, 10, 10, 10));

		header2.getChildren().add(new Label("Call Details:"));
		header2.setStyle("-fx-background-color: #d3d3d3;");
		header2.setPadding(new Insets(10, 10, 10, 10));
		
		header3.getChildren().add(new Label("Narrative:"));
		header3.setStyle("-fx-background-color: #d3d3d3;");
		header3.setPadding(new Insets(10, 10, 10, 10));
		
		addr.setPrefWidth(325);
		
		box.getChildren().add(new HBox(new Label("CAD ID: "), cadid));
		box.getChildren().add(new HBox(new Label("Nature: "), nature));
		box.getChildren().add(new HBox(new Label("Address: "), addr,
				new Label("Zone: "), fzone));
		box.getChildren().add(new HBox(new Label("City: "), city,
				new Label("State: "), st, new Label("Contact: "), contact));
		box.setSpacing(5);
		
		box.getChildren().add(header2);
		getInfo();
		//Call information
		box.getChildren().addAll(getCallInfo());
		
		box.getChildren().add(header3);
		box.getChildren().add(getNarrative());
		//Narrative
		
		main.getChildren().addAll(header, box);
		return main;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.FireCallScreen.getNarrative()
	 * SYNOPSIS
	 * 		 
	 * DESCRIPTION
	 * 		Returns two treeviews once with dispatcher comments 
	 * 		retrieved from the initial call object as well as all 
	 * 		comments regarding this call in the call.comments table. 
	 * 		also returns a treeview of all apparatus linked to current call
	 * RETURNS
	 * 		VBox box -> visual of two treeViews
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	private Node getNarrative() {
		VBox box = new VBox();
		
		TreeItem<String> dispcom = new TreeItem<String>("Dispatcher Comments");
		TreeItem<String> subnode = new TreeItem<String>(c.getDispComments());
		if(!(subnode.getValue().equalsIgnoreCase(""))){
			dispcom.getChildren().add(subnode);
		}
		TreeItem<String> initcom = new TreeItem<String>(c.getCall().get("callInfo").toString());
		if(!(initcom.getValue().equalsIgnoreCase(""))){
			dispcom.getChildren().add(initcom);
		}
		applog = new TreeItem<String>("Unit Log");
		List<Apparatus> applist = Call.getAppFromCall(callid);
		for(int i = 0; i < applist.size(); i ++){
			TreeItem<String> it = new TreeItem<String>(
					applist.get(i).getUnitString()+"\t\t"+applist.get(i).getStat());
			applog.getChildren().add(it);
		}
		TreeView<String> item = new TreeView<String>(dispcom);
		TreeView<String> item1 = new TreeView<String>(applog);
		
		item1.setOnMouseClicked(new EventHandler<MouseEvent>(){
		    @Override
		    public void handle(MouseEvent mouseEvent){            
		    	if(mouseEvent.getClickCount() == 2){
		            update(item1.getSelectionModel().getSelectedItem().getValue());
		        }
		    	else if(mouseEvent.getClickCount() == 1){
		    		try { 
		    			app = item1.getSelectionModel().getSelectedItem().getValue();
		    		} catch(NullPointerException e){ }
		    	}
		    }
		});
		
		box.getChildren().addAll(item, item1);
		
		return box;
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.cad.FireCallScreen.update(String appUnit)
	 * SYNOPSIS
	 * 		String appUnit -> apparatus to update
	 * DESCRIPTION
	 * 		updates the status of appUnit depending on what the current 
	 * 		status is using switch cases. if busy -> enrt, if enrt -> onloc
	 * 		if onloc->avail.
	 * RETURNS
	 * 		Action
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	protected void update(String appUnit) {
		appUnit = appUnit.substring(0, appUnit.indexOf("\t"));
		BasicDBObject obj = new BasicDBObject("AppType", appUnit.substring(0,1))
				.append("UnitCount", appUnit.substring(1, 3))
				.append("UnitMunic", appUnit.substring(3, 5));
		try {obj.append("appNum", appUnit.substring(5, 6));} 
		catch (Exception e) {obj.append("appNum", "");}
		Apparatus app = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info").findOne(obj));
		String stat = app.getStat();
		switch(stat){
		case "BUSY":{
			Status.updateStatus(new Status(true, true, false, false, true, app),
					"CALL ENRT:"+app.getCurCall()+"|OPR:"+Login.getUser());		
			Call.setEnrt(new Call(callid));
			root.setCenter(getCallScreen());
			applog.setExpanded(true);
			break;
		}
		case "ENRT":{
			Status.updateStatus(new Status(true, false, true, false, true, app),
					"CALL ARVD:"+app.getCurCall()+"|OPR:"+Login.getUser());		
			Call.setArvd(new Call(callid));
			root.setCenter(getCallScreen());
			applog.setExpanded(true);
			break;
		}
		case "ONLOC":{
			Status.updateStatus(new Status(true, false, false, true, false, app),
					"CALL CLEAR:"+app.getCurCall()+"|OPR:"+Login.getUser());	
			root.setCenter(getCallScreen());
			applog.setExpanded(true);
			break;
		}}
	}
	private Node getCallInfo() {
		VBox box = new VBox();
		
		paged.setPrefWidth(200);
		enrt.setPrefWidth(200);
		arrvd.setPrefWidth(200);
		ctrld.setPrefWidth(200);
		clear.setPrefWidth(200);
		
		box.getChildren().add(new HBox(new Label("BuildType: "), btype, new Label("Paged: "), paged));
		box.getChildren().add(new HBox(new Label("OIC: "), oic, new Label("ENRT: "), enrt));
		box.getChildren().add(new HBox(new Label("Condition Code: "), cond, new Label("ARRVD: "), arrvd));
		box.getChildren().add(new HBox(new Label("Circumstance: "), circ, new Label("CNTRLD: "), ctrld));
		box.getChildren().add(new HBox(new Label("Disposition: "), disp, new Label("CLEARED: "), clear));
		box.setSpacing(5);
		
		getTimes();
		
		return box;
	}
	private void getTimes() {
		paged.setText(getTime("PAGED"));
		enrt.setText(getTime("ENRT"));
		arrvd.setText(getTime("ARVD"));
		ctrld.setText(getTime("CTRLD"));
		clear.setText(getTime("CLEAR"));
	}
	private String getTime(String string) {
		try{
			return Database.getCol("Calls", "times")
					.findOne(new BasicDBObject("call",callid)).get(string).toString();
		}
		catch(NullPointerException e){
			return "";
		}		
	}
	private void getInfo() {
		clearAll();
		cadid.setText(c.getCall().get("cadid"));
		nature.setText(c.getCall().get("nature"));
		addr.setText(c.getCall().get("addr"));
		fzone.setText(c.getCall().get("fzone"));
		city.setText(c.getCall().get("city"));
		st.setText(c.getCall().get("st"));	
	}
	private void clearAll() {
		cadid.setText("");
		nature.setText("");
		addr.setText("");
		fzone.setText("");
		city.setText("");
		st.setText("");
	}
}