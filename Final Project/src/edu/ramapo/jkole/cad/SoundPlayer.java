/**/
/**
 * SoundPlayer.java
 * 
 * @author Jason Kole
 * 
 * SoundPlayer class contains sound player functions within the program.
 */
/**/
package edu.ramapo.jkole.cad;

import java.io.FileInputStream;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class SoundPlayer {
	static void playSound(){
		 String gongFile = "/Windows/Media/notify.wav";
		 try{
		 InputStream in = new FileInputStream(gongFile);
		 AudioStream audioStream = new AudioStream(in);
		 AudioPlayer.player.start(audioStream);
		 } catch(Exception e){
			 e.printStackTrace();
		 }
	}
}
