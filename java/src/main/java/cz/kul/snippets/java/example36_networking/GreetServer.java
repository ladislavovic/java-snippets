package cz.kul.snippets.java.example36_networking;

import java.net.*;
import java.io.*;

public class GreetServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {

        // Creates a server socket, bound to the specified port.
        serverSocket = new ServerSocket(port);

        // Listens for a connection to be made to this socket and accepts it.
        // The method blocks until a connection is made.
        clientSocket = serverSocket.accept();


        // TODO continue here
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String greeting = in.readLine();
        if ("hello server".equals(greeting)) {
            out.println("hello client");
        }
        else {
            out.println("unrecognised greeting");
        }
    }

    public void stop() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
    public static void main(String[] args) throws Exception {
        GreetServer server=new GreetServer();
        server.start(6666);
    }
}
