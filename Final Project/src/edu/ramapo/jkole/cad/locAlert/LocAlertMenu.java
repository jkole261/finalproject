package edu.ramapo.jkole.cad.locAlert;

import edu.ramapo.jkole.cad.Main;
import edu.ramapo.jkole.cad.Profile;
import javafx.application.Application;
import javafx.stage.Stage;

public class LocAlertMenu extends Application{
	Stage stage;
	
	public LocAlertMenu(){
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
	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) throws Exception {
		if(Main.pro.getAdminlvl() < 4){return;}
		System.out.println("Started");
	}

}
