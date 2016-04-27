/**/
/** ApparatusDispatch.java
 * 
 * @author Jason Kole
 * 
 * The ApparatusDispatch class is a menu that is displayed once dispatching begins,
 * within this menu dispatchers get basic information regarding a call, as well as, 
 * a list of responding apparatus to an emergency.
 **/
/**/
package edu.ramapo.jkole.cad;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.mongodb.BasicDBObject;

import edu.ramapo.jkole.alerting.Alert;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ApparatusDispatch extends Application{
	static Stage stage;
	List<AppList> appList;
	static String callid;
	Alert alert;
	
	public ApparatusDispatch(String call, List<AppList> nUni){
		try {
			stage = new Stage();
			appList = nUni;
			callid = call;
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 600, 400, Color.ANTIQUEWHITE);
    	try {
			scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css").toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
    	root.setTop(getTop());
    	root.setCenter(getCenter(appList));
		root.setBottom(getBot());
    	
		stage.setTitle("Dispatch Unit Screen"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show();
	}

	private Node getBot() {
		VBox bot = new VBox();
		Button page = new Button("PAGE UNITS");
		Button que = new Button("QUEUE CALL");
		
		HBox hb1 = new HBox();
		hb1.getChildren().addAll(page, que);
		bot.getChildren().add(hb1);
	
		page.setOnAction(actionEvent -> {
			pageApparatus(appList);
			try{
				CallTakerScreen.closeStage();
			}
			catch(NullPointerException e){
				
			}
			stage.close();
		});
		
		que.setOnAction(actionEvent -> {
			setPending(callid);
			stage.close();
			CallTakerScreen.closeStage();
		});
		
		return bot;
	}

	private void setPending(String callid2) {
		Database.add("Calls", "pend", new BasicDBObject("CadID", callid2));
	}

	private void pageApparatus(List<AppList> appList2) {
		alert = new Alert(callid);
		Database.getCol("Calls", "status").findAndModify(
				new BasicDBObject("CallId", callid), 
				new BasicDBObject("CallId", callid)
					.append("Status", "PAGED"));
		for(int i = 0; i < appList2.size(); i++){
			alert.addApp(appList.get(i).app);
			page(appList.get(i));
		}
		alert.sendAlert();
	}

	public static void clearApp(String str){
		String[] string = {str.substring(0, 1),
				str.substring(1, 3),str.substring(3, 5),""};
		try{
			string[3] = str.substring(5, 6);
		}
		catch(StringIndexOutOfBoundsException e){
			string[3] = "";
		}
		Apparatus app = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", string[0])
						.append("UnitCount", string[1])
						.append("UnitMunic", string[2])
						.append("appNum", string[3])));
		Status.updateStatus(new Status(true, false, false, true, false, app),
				"CALL CLEAR:"+ActCallMenu.table.getSelectionModel().getSelectedItem().getCall().get("cadid")+"|OPR:"+Login.getUser());	
	}
	
	public static void arvdApp(String str){
		String[] string = {str.substring(0, 1),
				str.substring(1, 3),str.substring(3, 5),""};
		try{
			string[3] = str.substring(5, 6);
		}
		catch(StringIndexOutOfBoundsException e){
			string[3] = "";
		}
		Apparatus app = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", string[0])
						.append("UnitCount", string[1])
						.append("UnitMunic", string[2])
						.append("appNum", string[3])));
		Status.updateStatus(new Status(true, false, true, false, true, app),
				"CALL ARVD:"+ActCallMenu.table.getSelectionModel().getSelectedItem().getCall().get("cadid")+"|OPR:"+Login.getUser());	
		Call.setArvd(ActCallMenu.table.getSelectionModel().getSelectedItem());
	}
	
	public static void arvdApp(String callid, String app){
		String[] string = {app.substring(0, 1),
				app.substring(1, 3),app.substring(3, 5),""};
		try{
			string[3] = app.substring(5, 6);
		}
		catch(StringIndexOutOfBoundsException e){
			string[3] = "";
		}
		Apparatus a = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", string[0])
						.append("UnitCount", string[1])
						.append("UnitMunic", string[2])
						.append("appNum", string[3])));
		Status.updateStatus(new Status(true, false, true, false, true, a),
				"CALL ARVD:"+callid+"|OPR:"+Login.getUser());	
		Call.setArvd(new Call(callid));
	}
	
	public static void enrtApp(String str){
		String[] string = {str.substring(0, 1),
		str.substring(1, 3),str.substring(3, 5),""};
		try{
			string[3] = str.substring(5, 6);
		}
		catch(StringIndexOutOfBoundsException e){
			string[3] = "";
		}
		Apparatus app = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", string[0])
						.append("UnitCount", string[1])
						.append("UnitMunic", string[2])
						.append("appNum", string[3])));
		Status.updateStatus(new Status(true, true, false, false, true, app),
				"CALL ENRT:"+ActCallMenu.table.getSelectionModel().getSelectedItem().getCall().get("cadid")+"|OPR:"+Login.getUser());		
		Call.setEnrt(ActCallMenu.table.getSelectionModel().getSelectedItem());
	}
	
	public static void enrtApp(String callid, String app){
		String[] string = {app.substring(0, 1),
		app.substring(1, 3),app.substring(3, 5),""};
		try{
			string[3] = app.substring(5, 6);
		}
		catch(StringIndexOutOfBoundsException e){
			string[3] = "";
		}
		Apparatus a = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", string[0])
						.append("UnitCount", string[1])
						.append("UnitMunic", string[2])
						.append("appNum", string[3])));
		Status.updateStatus(new Status(true, true, false, false, true, a),
				"CALL ENRT:"+callid+"|OPR:"+Login.getUser());		
		Call.setEnrt(new Call(callid));
	}
	
	private void page(AppList appList2) {
//		GENERATE TONES
		Apparatus app = appList2.app;
		
		if(app.getUnitString().contains("*") || 
				app.getUnitString().equalsIgnoreCase("utilit") ||
				app.getUnitString().equalsIgnoreCase("admin")){
			return;
		}
		Status.updateStatus(new Status(true, false, false, false, true, app),
				"CALL PAGE:"+callid+"|OPR:"+Login.getUser());
		Call.setPaged(callid);
	}

	@SuppressWarnings("unchecked")
	private Node getCenter(List<AppList> applist) {
		VBox cent = new VBox();
		TableView<AppList> table = new TableView<AppList>();
		ObservableList<AppList> apps = FXCollections.observableArrayList(applist);
		
		TableColumn<AppList, String> App = new TableColumn<AppList, String>();
		TableColumn<AppList, String> type = new TableColumn<AppList, String>();
		
		App.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().toString()));
		type.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().type));
		
		table.setRowFactory(tv -> {
	            TableRow<AppList> row = new TableRow<AppList>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	AppList napp = getList(row.getItem(), apps);
	                	row.setItem(napp);
	                	applist.set(row.getIndex(), napp);
	                }
	            });
	            return row ;
	        });
		
		table.getColumns().addAll(App, type);
		table.setItems(apps);
		
		cent.getChildren().add(table);
		
		return cent;
	}

	private AppList getList(AppList item, ObservableList<AppList> apps) {
		// TODO Auto-generated method stub
		TextInputDialog dialog = new TextInputDialog(item.app.getUnitString());
		dialog.setTitle("Change Apparatus Menu");
		dialog.setHeaderText("Enter Apparatus to Change Disp");
		dialog.setContentText("Apparatus Number:");

		Optional<String> results = dialog.showAndWait();
		results.ifPresent(appNum -> {
			String result = results.get().toUpperCase();
			BasicDBObject obj = new BasicDBObject("AppType", result.toString().substring(0,1))
			.append("UnitCount", result.toString().substring(1, 3))
			.append("UnitMunic", result.toString().substring(3, 5));
			try {obj.append("appNum", result.toString().substring(5, 6));} 
			catch(Exception e){obj.append("appNum", "");}
			Apparatus app = new Apparatus((BasicDBObject) 
					Database.getCol("Apparatus", "info").findOne(obj));

			item.setApp(app);			
		});	
				
		return new AppList(item.type, item.getApp());
	}

	private Node getTop() {
		VBox top = new VBox();
		Call c = getCall(callid);
		TextField cad = new TextField(c.getCall().get("cadid"));
		TextField actid = new TextField(c.getCall().get("actid"));
		TextField addr = new TextField(c.getCall().get("addr").split(",")[0]);
		TextField city = new TextField(c.getCall().get("city"));
		TextField nature = new TextField(c.getCall().get("nature"));
		TextField type = new TextField(c.getCall().get("type"));
		TextField info = new TextField(c.getCall().get("callInfo"));

		HBox hb1 = new HBox();
		hb1.getChildren().addAll(new Label("CAD ID: "), cad, new Label("ACT ID: "), actid);
		
		HBox hb2 = new HBox();
		hb2.getChildren().addAll(new Label("Address: "), addr, new Label("City: "), city);
		
		HBox hb3 = new HBox();
		hb3.getChildren().addAll(new Label("Nature: "), nature, new Label("Type: "), type);
		
		HBox hb4 = new HBox();
		hb4.getChildren().add(new Label("Info: "));
		
		HBox hb5 = new HBox();
		hb5.getChildren().add(info);
		
		top.getChildren().addAll(hb1, hb2, hb3, hb4, hb5);

		return top;
	}

	private Call getCall(String callid2) {
		return new Call((BasicDBObject)Database.getCol("Calls", "basicInfo")
				.findOne(new BasicDBObject("cadid", callid2)));
	}

	@Override
	public void stop(){
		stage.close();
	}

	public static void setAvail(String str) {
		String[] string = {str.substring(0, 1),
		str.substring(1, 3),str.substring(3, 5),""};
		try{
			string[3] = str.substring(5, 6);
		}
		catch(StringIndexOutOfBoundsException e){
			string[3] = "";
		}
		Apparatus app = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", string[0])
						.append("UnitCount", string[1])
						.append("UnitMunic", string[2])
						.append("appNum", string[3])));
		Status.updateStatus(new Status(true, false, false, true, false, app),
				"CMDLINE AVAIL|OPR:"+Login.getUser());		
	}
	public static void setAvail(String callid, String app) {
		String[] string = {app.substring(0, 1),
		app.substring(1, 3),app.substring(3, 5),""};
		try{
			string[3] = app.substring(5, 6);
		}
		catch(StringIndexOutOfBoundsException e){
			string[3] = "";
		}
		Apparatus a = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", string[0])
						.append("UnitCount", string[1])
						.append("UnitMunic", string[2])
						.append("appNum", string[3])));
		Status.updateStatus(new Status(true, false, false, true, false, a),
				"CMDLINE AVAIL|OPR:"+Login.getUser());		
	}
	
	
	public static void upgrade() {
		List<String> choices = new ArrayList<>();

		choices.addAll(getChoices());

		ChoiceDialog<String> dialog = new ChoiceDialog<String>("" ,choices);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText("Look, a Choice Dialog");
		dialog.setContentText("Choose your letter:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			String str = result.get().toUpperCase();
			Dispatch.recUnits(FireCallScreen.callid, str);
			System.out.println(callid);
		}
	}

	private static Collection<? extends String> getChoices() {
		List<String> list = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader("lib/disptypes.dat");
			BufferedReader buffReader = new BufferedReader(fileReader);
			String line;
			while((line = buffReader.readLine()) != null) {
                list.add(line.substring(0, line.indexOf("::")));
            }   
			buffReader.close();
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void setOss(String string, String[] str) {
		// TODO Auto-generated method stub
		String[] str1 = {str[1].substring(0, 1),
		str[1].substring(1, 3),str[1].substring(3, 5),""};
		try{str1[3] = str[1].substring(5, 6);}
		catch(StringIndexOutOfBoundsException e){str1[3] = "";}
		String com = "";
		for(int i = 2; i < str.length; i++){
			com += str[i]+" ";
		}
		Apparatus app = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", str1[0])
						.append("UnitCount", str1[1])
						.append("UnitMunic", str1[2])
						.append("appNum", str1[3])));
		Status.updateStatus(new Status(false, false, false, false, false, app),
				"OOS|"+com+"|OPR:"+Login.getUser());	
	}
	public static void setOss(String app) {
		String[] string = {app.substring(0, 1),
				app.substring(1, 3),app.substring(3, 5),""};
		try{
			string[3] = app.substring(5, 6);
		}
		catch(StringIndexOutOfBoundsException e){
			string[3] = "";
		}
		Apparatus a = new Apparatus((BasicDBObject) Database
				.getCol("Apparatus", "info")
				.findOne(new BasicDBObject("AppType", string[0])
					.append("UnitCount", string[1])
					.append("UnitMunic", string[2])
					.append("appNum", string[3])));
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Set Unit "+a.getUnitString()+" OOS");
		dialog.setHeaderText("Enter Comment to set "+a.getUnitString()+"\nOut of Service");
		dialog.setContentText("Reason:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(com -> {
			Status.updateStatus(new Status(false, false, false, false, false, a),
					"OOS|"+com+"|OPR:"+Login.getUser());
		});	
	}
	
	
	public static void rlog(Pair<String, String> unitCom) {
		Apparatus app = Apparatus.findApp(unitCom.getKey());
		
		Status.updateStatus(new Status(true, false, false, false, true, app),
				"RADIOLOG|"+unitCom.getValue()+"|OPR:"+Login.getUser());
	}
}
