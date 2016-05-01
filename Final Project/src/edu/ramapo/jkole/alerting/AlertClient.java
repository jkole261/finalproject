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
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.AlertClient.run()
	 * SYNOPSIS
	 * 		this.AlertClient	-> thread that will be ran
	 * DESCRIPTION
	 * 		runs the thread AlertClient which opens a socket 
	 * 		to sends String message to the server.
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public void run() {
		try {
			socket = new Socket(serverAddress, 9001);
	        in = new BufferedReader(new InputStreamReader(
	            socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
	        Thread.currentThread().interrupt();
		} 
    }
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.AlertClient.start()
	 * SYNOPSIS
	 * 		this.AlertClient -> this instance of the Thread
	 * DESCRIPTION
	 * 		runs the thread AlertClient
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
	 * 		edu.ramapo.jkole.alerting.AlertClient.close()
	 * SYNOPSIS
	 * 		this.AlertClient ->		this instance of the thread
	 * DESCRIPTION
	 * 		closes the socket and interupts the thread
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
	/**/
	/*
	 * NAME
	 * 		edu.ramapo.jkole.alerting.AlertClient.setMessage(String str)
	 * SYNOPSIS
	 * 		String str -> string that will be send as an Alert
	 * DESCRIPTION
	 * 		send the message before the thread is ran
	 * RETURNS
	 * 		void
	 * Author
	 * 		Jason Kole - Spring 2016
	 */
	/**/
	public void setMessage(String str) throws IOException {
		message = str;
	}
}