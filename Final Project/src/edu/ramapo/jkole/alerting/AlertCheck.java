package edu.ramapo.jkole.alerting;

import java.awt.Insets;
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
import edu.ramapo.jkole.cad.MainMenu;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
	       		System.out.println("..");
	       		try {
	       			String line = in.readLine();
	       			if (line.contains(str)) {
	       				info.append(line.substring((line.indexOf("MESSAGE ")+7)));
	                }
	       			if (line.contains("!!!!")){
	       				showMessage(info.toString());
	       			}
	                else {
	                	info.append(line.substring((line.indexOf("MESSAGE ")+7)));
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
	   System.out.println("NEW ALERT\n"+info);
	   MainMenu.showPopup(info.toString());
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
