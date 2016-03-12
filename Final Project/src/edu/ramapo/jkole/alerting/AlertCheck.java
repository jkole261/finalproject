package edu.ramapo.jkole.alerting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AlertCheck {
	BufferedReader in;
    PrintWriter out;
    String str;
    JFrame frame = new JFrame("Chatter");
    static String message;
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    static String call = null;
    
    static boolean lock = false;

   public AlertCheck(String string) {
	   System.out.println("new");
       // Layout GUI
   	str = string;
   	try {
			this.run();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }

   @SuppressWarnings("resource")
	public void run() throws IOException, InterruptedException {

       // Make connection and initialize streams
       String serverAddress = "127.0.0.1";
       Socket socket = new Socket(serverAddress, 9001);
       in = new BufferedReader(new InputStreamReader(
           socket.getInputStream()));
 //      out = new PrintWriter(socket.getOutputStream(), true);
       
       // Process all messages from server, according to the protocol.
       while (true) {
           try {
           	 String line = in.readLine();
                if (line.startsWith("MESSAGE")) {
                    System.out.println("yes");
                }
                else {
                	messageArea.append(line + "\n");
                }
           } catch (Exception e) {
               Thread.currentThread().interrupt();
           }
          // wait(7000);
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
