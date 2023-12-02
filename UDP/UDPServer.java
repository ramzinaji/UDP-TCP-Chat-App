package UDP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPServer {
    protected static final int DEFAULT_PORT = 8080;
    byte[] buffer = new byte[1024];
    DatagramSocket socket;

    public static void main(String[] args) throws SocketException {
        int port = UDPServer.DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port 8080.");
            }
        }

        UDPServer server = new UDPServer(port);
        server.launch();
    }

    public UDPServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
        System.out.println(UDPServer.class.getSimpleName() + " is listening on port " + port);

    }

    public UDPServer() throws SocketException {
        this(DEFAULT_PORT);
    }

    public void launch() {
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                process(packet);}
        } catch (IOException e) {
            System.err.println("IOException while receiving packet. ");

        }
    }

    public void process(DatagramPacket packet) throws UnsupportedEncodingException {
        System.out.println(toString(packet));
    }

    public String toString(DatagramPacket packet){
        InetAddress clientAddress = packet.getAddress();
        int clientPort = packet.getPort();
        String lastReceivedData = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
        return "Received from " + clientAddress + " : " + clientPort + " : " + lastReceivedData;
    }
}

