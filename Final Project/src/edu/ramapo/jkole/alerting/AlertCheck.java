/**/
/** AlertCheck.java
 * 
 * @author Jason Kole
 * 
 * The AlertCheck class creates and runs a thread constantly while the program
 * is running to listen for all new calls. once a new call is recieved it checks 
 * for the dispatched stations. if they are the same it will display the message 
 * in the console.
 **/
/**/
package edu.ramapo.jkole.alerting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.ramapo.jkole.cad.MainMenu;

public class AlertCheck extends Thread {
	BufferedReader in;
    PrintWriter out;
    String str;
    static String message;
    static String call = null;
    StringBuilder info;
    private Thread t;
    Socket socket;
    Exception ex = new ConnectException();
    
    static boolean lock = false;

    public AlertCheck(String string) {
	   str = string;
	   info = new StringBuilder();
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.AlertCheck.run()
	 * SYNOPSIS
	 * 		Socket socket	->	connection to server
	 * DESCRIPTION
	 * 		runs a thread to listen for incoming messages and 
	 * 		prints them to the console
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
    public void run() {
    	try {		   
    		String serverAddress = "127.0.0.1";
    		socket = new Socket(serverAddress, 9001);
    		in = new BufferedReader(new InputStreamReader(
    				socket.getInputStream()));
    		while (true) {
    			try {
    				String line = in.readLine();
	       			if (line.contains(str)) {
	       				info.append(line.substring((line.indexOf("MESSAGE ")+8))+"\n");
	                }
	       			if (line.contains("!!!!")){
	       				showMessage(info.toString());
	       			}
	                else {
	                	info.append(line.substring((line.indexOf("MESSAGE ")+8))+"\n");
	                }
    			} catch (Exception e) {
    				Thread.currentThread().interrupt();
    			} 
				Thread.sleep(500);
	       }
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (ConnectException e1){
			System.err.println("ALERTCHECK.JAVA "+e1.getMessage());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
   }

	private void showMessage(String message2) {
    	MainMenu.showPopup(info.toString());
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.Alert.start()
	 * SYNOPSIS
	 * 		this.AlertCheck		-> this instance of the thread
	 * DESCRIPTION
	 * 		begins running the thread
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public void start () {
		if (t == null) {
			t = new Thread (this);
			t.start ();
		}
	}
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.AlertCheck.close()
	 * SYNOPSIS
	 * 		this.AlertCheck		->	connection that is closed
	 * DESCRIPTION
	 * 		closes the socket and interrupts the thread running 
	 * 		to give chance to terminate the thread.
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public void close(){
		try {
			socket.close();
			Thread.currentThread().interrupt();
			t.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}