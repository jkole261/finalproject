package edu.ramapo.jkole.alerting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AlertClient {

     BufferedReader in;
     PrintWriter out;
     String str;
     static String message;
     static String call = null;
     static String serverAddress = "127.0.0.1";
     static Socket socket;
     static boolean lock = false;

    public AlertClient(String string) {
        
    	
        // Layout GUI
    	str = string;
    	try {
			this.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	public void run() throws IOException {

        // Make connection and initialize streams
		socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        // Process all messages from server, according to the protocol.
        while (lock) {
            try {
            	 out.println(message);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            socket.close();
            lock = false;
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