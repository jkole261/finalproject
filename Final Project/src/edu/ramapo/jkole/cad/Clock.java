/**/
/** Clock.java
 * 
 * @author Jason Kole
 * 
 * The clock object is displayed on the main menu as a constantly
 * running time with about a 1/2 second delay at times.
 **/
/**/

package edu.ramapo.jkole.cad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Clock {
	public static Node getClock() {
        final Label clock = new Label();
        clock.setTextAlignment(TextAlignment.RIGHT);
        final DateFormat format = new SimpleDateFormat("EEE, MMM dd, yyyy'\n' HH : mm : ss z");
        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {   
             public void handle(ActionEvent event) {  
                  final Calendar cal = Calendar.getInstance();  
                  clock.setText(format.format(cal.getTime()));  
             }  
        }));  
        timeline.setCycleCount(Animation.INDEFINITE);  
        timeline.play();  
		return clock;
	}

	public static String getYr() {
		return new SimpleDateFormat("yy").format(Calendar.getInstance().getTime());
	}

	public static String getTime() {
		return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
	}
}
