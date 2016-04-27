/**/
/** AlertClient.java
 * 
 * @author Jason Kole
 * 
 * The AlertClient class creates a thread and waits for input. this input is sending information
 * to the server to then be recieved by the alertCheck class to listen for calls. the AlertClient 
 * is responsible for sending then information.
 **/
/**/
package edu.ramapo.jkole.alerting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AlertClient extends Thread{

     BufferedReader in;
     PrintWriter out;
     String str;
     static String message;
     static String call = null;
     static String serverAddress = "127.0.0.1";
     static Socket socket;
     private Thread t;

    public AlertClient(String string) { 
    	str = string;
    }
    
	public void run() {
		try {
        // Make connection and initialize streams
			socket = new Socket(serverAddress, 9001);
	        in = new BufferedReader(new InputStreamReader(
	            socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
	        Thread.currentThread().interrupt();
		} 
    }
	public void start () {
		if (t == null) {
			t = new Thread (this);
			t.start ();
		}
	}
	public void close(){
		try {
			socket.close();
			Thread.currentThread().interrupt();
			t.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setMessage(String string) throws IOException {
		message = string;
	}
}