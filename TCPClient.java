import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient {
    static private final int maxRetryCount = 3;
    static private final int retryDelayMillis = 2000;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java " + TCPClient.class.getSimpleName() + " <server_address> <port>");
            return;
        }

        String serverHostname = args[0];
        int serverPort = Integer.parseInt(args[1]);

        TCPClient client = new TCPClient();
        Socket clientSocket = client.connectToServer(serverHostname, serverPort);

        if (clientSocket != null) {
            client.startHandlingConnection(clientSocket);
        }
    }

    private Socket connectToServer(String serverHostname, int serverPort) {
        for (int retry = 1; retry <= TCPClient.maxRetryCount; retry++) {
            try {
                Socket clientSocket = new Socket(serverHostname, serverPort);
                System.out.println("Connected to " + serverHostname + " on port " + serverPort);
                return clientSocket;
            } catch (Exception e) {
                System.err.println("Connection attempt " + retry + " failed. Retrying in " + TCPClient.retryDelayMillis + "ms.");
                try {
                    Thread.sleep(TCPClient.retryDelayMillis);
                } catch (InterruptedException ex) {
                    System.out.println("Thread.sleep() was interrupted.");
                }
            }
        }
        return null;
    }

    private void startHandlingConnection(Socket clientSocket) {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            // Start a new thread for receiving and printing messages from the server
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println("Server Response: " + serverResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server.");
                }
            }).start();

            // Main thread for sending messages to the server
            handleUserInputAndSendMessage(userInput, out);

        } catch (IOException e) {
            System.out.print("Exception caught when trying to listen on port " + clientSocket.getPort() + " or listening for a connection");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket.");
            }
        }
    }

    private void handleUserInputAndSendMessage(BufferedReader userInput, PrintWriter out) throws IOException {
        System.out.println("Enter text to send to the server. Press <CTRL>+D to exit.");

        String inputLine;
        while ((inputLine = userInput.readLine()) != null) {
            out.println(inputLine);
        }
    }
}