/**/
/**
 * PrefMenu.java
 * 
 * @author Jason Kole
 * 
 * the PrefMenu will be accessed through the mainmenu and be 
 * able to change CSS formatting throughout the entire program.
 * the preferences are stored in a CSS file according to the 
 * user.
 * 
 * NOT COMPLETED FOR THIS RELEASE
 */
/**/
package edu.ramapo.jkole.cad;
 
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

import edu.ramapo.jkole.css.parser.RuleParser;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PrefMenu extends Application{
	static Stage stage;
	static CSSStyleSheet sheet;
	
	public PrefMenu(){
		try{
			if(stage.isShowing()){
				return;
			} else {
				stage.show();
			} 
		} catch (Exception e) {
			stage = new Stage();
			try { start(stage); }
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		BorderPane root = new BorderPane();
    	Scene scene = new Scene(root, 600, 600, Color.ANTIQUEWHITE);
    	try { scene.getStylesheets().add((new File("lib/css/"+Main.pro.getUser()+".css")
    			.toURI().toURL()).toExternalForm());
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
    	//get file
    	sheet = getStyleSheet(Main.pro.getUser());
    	
    	root.setCenter(getCenter());
    	
		stage.setTitle("Preferences Menu"); 
        stage.setScene(scene);
        stage.setMaxWidth(GraphicsEnvironment.getLocalGraphicsEnvironment()
        		.getMaximumWindowBounds().width);
        stage.sizeToScene(); 
        stage.show();
	}

	private CSSStyleSheet getStyleSheet(String user) {		
		try {
			InputStream inStream = new FileInputStream("lib/css/"+user+".css");
		    InputSource source = new InputSource(new InputStreamReader(inStream, "UTF-8"));
		    CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
		    sheet = parser.parseStyleSheet(source, null, null);
		    inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	private Node getCenter() {
		GridPane grid =  new GridPane();
		
		CSSRuleList rules = sheet.getCssRules();
	    for (int i = 0; i < rules.getLength(); i++) {
	        final CSSRule rule = rules.item(i);
	        GridPane.setConstraints(RuleParser.parseRule(rule), 1, i); // column=3 row=1
	        
	    }
		
		//Row 1
		//TextField 
		
		return grid;
	}
}
