package cz.kul.snippets.java.networking;

import java.net.*;
import java.io.*;

public class GreetClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws IOException {
        GreetClient client = new GreetClient();
        client.startConnection("localhost", 6666);
        String serverResponse = client.sendMessage("hello server");
        System.out.println("Server response: " + serverResponse);
    }

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

        String msg = "Socket\n  local addr/port:%s\n  remote addr/port: %s";
        System.out.println(String.format(
                msg,
                clientSocket.getLocalAddress() + ":" + clientSocket.getLocalPort(),
                clientSocket.getInetAddress() + ":" + clientSocket.getPort()));

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
    }
}
