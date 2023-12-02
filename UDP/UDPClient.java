package UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java " + UDPClient.class.getSimpleName() + " <server-hostname> <server-port>");
            return;
        }

        String serverHostname = args[0];
        int serverPort = Integer.parseInt(args[1]);

        UDPClient client = new UDPClient();
        client.launch(serverHostname, serverPort);
    }
    public void launch(String serverHostname, int serverPort) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverHostname);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            System.out.println(UDPClient.class.getSimpleName() + " is ready. Enter text to send, or type 'exit' to quit.");

            while (true) {
                userInput = getUserInput(console, socket, serverAddress, serverPort);
                if (userInput == null) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("IOException while creating socket or reading input. ");
        }
    }

    private String getUserInput(BufferedReader console, DatagramSocket socket, InetAddress serverAddress, int serverPort) throws IOException {
        System.out.print("Enter text: ");
        String userInput = console.readLine();

        if ("exit".equalsIgnoreCase(userInput)) {
            System.out.println("Exiting...");
            return null;
        }

        sendPacket(socket, serverAddress, serverPort, userInput);
        return userInput;
    }

    private void sendPacket(DatagramSocket socket, InetAddress serverAddress, int serverPort, String message) throws IOException {
        byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
        socket.send(packet);
    }
}