package cz.kul.snippets.java.example36_networking;

import java.net.*;
import java.io.*;

public class GreetServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws Exception {
        GreetServer server = new GreetServer();
        server.start(6666);
    }

    public void start(int port) throws IOException, InterruptedException {

        // Creates a server socket, bound to the specified port.
        serverSocket = new ServerSocket(port);

        // Listens for a connection to be made to this socket and accepts it.
        // The method blocks until a connection is made.
        clientSocket = serverSocket.accept();
        String msg = "Client socket created\n  local addr/port:%s\n  remote addr/port: %s";
        System.out.println(String.format(
                msg,
                clientSocket.getLocalAddress() + ":" + clientSocket.getLocalPort(),
                clientSocket.getInetAddress() + ":" + clientSocket.getPort()));

        // Create input/output streams
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

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

}
