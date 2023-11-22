import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class ConnectionThread extends Thread {
    private final Socket clientSocket;
    private final TCPServer server;
    private PrintWriter writer;

    public ConnectionThread(Socket clientSocket, TCPServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error creating PrintWriter for client.");
        }
    }

    public void run() {
        try {
            InetAddress clientAddress = clientSocket.getInetAddress();
            int clientPort = clientSocket.getPort();
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            processData(reader, clientAddress, clientPort);

            server.removeClientThread(this);
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("IOException while handling client connection. ");
        }
    }

    private void processData(BufferedReader reader, InetAddress clientAddress, int clientPort) throws IOException {
        String inputData;
        while ((inputData = reader.readLine()) != null) {
            System.out.println("Received from client " + clientAddress + ":" + clientPort + " : " + inputData);
            server.broadcast("Client " + clientAddress + ":" + clientPort + " says: " + inputData);
        }
    }

    public void send(String message) {
        writer.println(message);
    }

    public InetAddress getClientAddress() {
        return clientSocket.getInetAddress();
    }

    public int getClientPort() {
        return clientSocket.getPort();
    }
}
