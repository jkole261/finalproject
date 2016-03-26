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
     static boolean lock = false;

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
	        
	        // Process all messages from server, according to the protocol.

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
		unlock();
		message = string;
	}
	
	private static void unlock(){
		lock = true;
	}
}