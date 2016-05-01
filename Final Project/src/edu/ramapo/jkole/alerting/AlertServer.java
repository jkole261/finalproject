/**/
/** AlertServer.java
 * 
 * @author Jason Kole
 * 
 * the AlertServer class is an independant program from StreetWatch. it is the server
 * that recieves and pushes out all information to the respective clients. this class
 * will not run when the main program runs unless started previously.
 * 
 **/
/**/

package edu.ramapo.jkole.alerting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class AlertServer {

	 private static final int PORT = 9001;

	    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	    
		/**/
		/*
		 * NAME
		 * 		edu.ramapo.jkole.alerting.AlertServer.main()
		 * SYNOPSIS
		 * 		beginning point of AlertServer 
		 * DESCRIPTION
		 * 		starts and creates a server to listen for Alerts and 
		 * 		then sends them out to alertclients.
		 * RETURNS
		 * 		void
		 * Author
		 * 		Jason Kole - Spring 2016
		 */
		/**/
	    public static void main(String[] args) throws Exception {
	        System.out.println("The Alert server is running.");
	        ServerSocket listener = new ServerSocket(PORT);
	        try {
	            while (true) {
	                new Handler(listener.accept()).start();
	            }
	        } finally {
	            listener.close();
	        }
	    }

	    /**/
		/**
		 * a thread class that is responsible for listening to a 
		 * client and broadcasting messages.
		 **/
		/**/
	    private static class Handler extends Thread {
	        private Socket socket;
	        private BufferedReader in;
	        private PrintWriter out;

	        public Handler(Socket socket) {
	            this.socket = socket;
	        }
	        /**/
			/*
			 * NAME
			 * 		edu.ramapo.jkole.alerting.AlertServer.run()
			 * SYNOPSIS
			 * 		thread loop for the handler class 
			 * DESCRIPTION
			 * 		listens to the server and once it recieves a message it will
			 * 		send it out to all listeners on the server. 
			 * RETURNS
			 * 		void
			 * Author
			 * 		Jason Kole - Spring 2016
			 */
			/**/
	        public void run() {
	            try {
	                in = new BufferedReader(new InputStreamReader(
	                    socket.getInputStream()));
	                out = new PrintWriter(socket.getOutputStream(), true);

	                writers.add(out);

	                while (true) {
	                    String input = in.readLine();
	                    System.out.println(input);
	                    if (input == null) {
	                        return;
	                    }
	                    for (PrintWriter writer : writers) {
	                        writer.println("MESSAGE " + input);
	                    }
	                }
	            } catch (IOException e) {
	                System.out.println(e);
	            } finally {
	                if (out != null) {
	                    writers.remove(out);
	                }
	                try {
	                    socket.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	    }
}