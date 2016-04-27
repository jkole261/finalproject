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

	    /**
	     * The set of all the print writers for all the clients.  This
	     * set is kept so we can easily broadcast messages.
	     */
	    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

	    /**
	     * The appplication main method, which just listens on a port and
	     * spawns handler threads.
	     */
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

	    /**
	     * A handler thread class.  Handlers are spawned from the listening
	     * loop and are responsible for a dealing with a single client
	     * and broadcasting its messages.
	     */
	    private static class Handler extends Thread {
	        private Socket socket;
	        private BufferedReader in;
	        private PrintWriter out;

	        /**
	         * Constructs a handler thread, squirreling away the socket.
	         * All the interesting work is done in the run method.
	         */
	        public Handler(Socket socket) {
	            this.socket = socket;
	        }

	        /**
	         * Services this thread's client by repeatedly requesting a
	         * screen name until a unique one has been submitted, then
	         * acknowledges the name and registers the output stream for
	         * the client in a global set, then repeatedly gets inputs and
	         * broadcasts them.
	         */

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