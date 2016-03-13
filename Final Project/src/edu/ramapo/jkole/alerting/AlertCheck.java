package edu.ramapo.jkole.alerting;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.ramapo.jkole.cad.ActCallMenu;
import edu.ramapo.jkole.cad.Main;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

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
	   System.out.println("new");
	   str = string;
	   info = new StringBuilder();
   }

	public void run() {
	   try {		   
		   String serverAddress = "127.0.0.1";
		   socket = new Socket(serverAddress, 9001);
		   in = new BufferedReader(new InputStreamReader(
		           socket.getInputStream()));
		   System.out.println("IN THREAD");
	       while (true) {
	       		System.out.println("T..");
	       		try {
	       			String line = in.readLine();
	       			if (line.contains(Main.pro.getAgency())) {
	       				showMessage(message);
	                }
	                else {
	                }
	           } catch (Exception e) {
	               Thread.currentThread().interrupt();
	           }
	       }
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

   }
   private void showMessage(String message2) {
	   Alert alert = new Alert(AlertType.INFORMATION);
	   alert.setTitle("Information Dialog");
	   alert.setHeaderText("Look, an Information Dialog");
	   alert.setContentText("alert");
	   alert.showAndWait();
	}

public void start ()
   {
      System.out.println("Starting ");
      if (t == null)
      {
         t = new Thread (this);
         t.start ();
      }
   }
	public void setMessage(String string) throws IOException {
		unlock();
		message = string;
	}
	public void close(){
		try {
			t.interrupt();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void unlock(){
		lock = true;
	}
}
