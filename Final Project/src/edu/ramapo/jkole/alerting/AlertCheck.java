package edu.ramapo.jkole.alerting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.ramapo.jkole.cad.MainMenu;

public class AlertCheck extends Thread{
	BufferedReader in;
    PrintWriter out;
    String str;
    JFrame frame = new JFrame("Chatter");
    static String message;
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    static String call = null;
    StringBuilder info;
    private Thread t;
    Socket socket;
  
    
    static boolean lock = false;

   public AlertCheck(String string) {
	   str = string;
	   info = new StringBuilder();
   }

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
	       }
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (ConnectException e1){
			System.err.println("AlertCheck.java "+e1.getMessage());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 

   }
    private void showMessage(String message2) {
    	MainMenu.showPopup(info.toString());
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
}