package cz.kul.snippets.java.networking.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    private static Selector selector = null;

    public static void main(String[] args) {

        try {
            // Creates a new Selector
            selector = Selector.open();

            // Create seerver socker, bind it to address, set is non-blocking
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress("localhost", 8089));
            serverSocketChannel.configureBlocking(false);

            // It retunrs operations supported by this channel. ServerSocketChannel supports only SelectionKey.OP_ACCEPT
            int ops = serverSocketChannel.validOps();

            // Register the channel with the selector
            serverSocketChannel.register(selector, ops, null);

            // Event loop. It watches IO operations
            while (true) {

                // Selects a set of keys whose corresponding channels are ready for I/O operations.
                // It is a Blocking operation (so there are no useless operations)
                selector.select();

                // Return selected keys, so the keys ready for the IO operation
                Set<SelectionKey> selectedKeys = selector.selectedKeys();

                // Iterate through ready IO operations
                Iterator<SelectionKey> i = selectedKeys.iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();

                    if (key.isAcceptable()) {
                        // New client has been accepted
                        handleAccept(serverSocketChannel, key);
                    } else if (key.isReadable()) {
                        // We can run non-blocking operation READ on our client
                        handleRead(key);
                    }

                    // Should be removed from selected keys after processing. Selector does not do it automatically.
                    // (so you can call select() method more times before processing them)
                    i.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleAccept(ServerSocketChannel serverSocketChannel, SelectionKey key) throws IOException {

        System.out.println("Connection Accepted...");

        // Accept the connection and set non-blocking mode
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);

        // Register that client is reading this channel
        client.register(selector, SelectionKey.OP_READ);
    }

    private static void handleRead(SelectionKey key) throws IOException {

        System.out.println("Reading...");
        // create a ServerSocketChannel to read the request
        SocketChannel socketChannel = (SocketChannel) key.channel();

        // Create buffer to read data
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer);
        // Parse data from buffer to String
        String data = new String(buffer.array()).trim();
        if (data.length() > 0) {
            System.out.println("Received message: " + data);
            if (data.equalsIgnoreCase("exit")) {
                socketChannel.close();
                System.out.println("Connection closed...");
            }
        }

    }
}
