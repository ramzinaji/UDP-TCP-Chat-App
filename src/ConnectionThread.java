import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class ConnectionThread extends Thread {
    private final Socket clientSocket;

    public ConnectionThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InetAddress clientAddress = clientSocket.getInetAddress();
            int clientPort = clientSocket.getPort();
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);

            processData(reader, writer, clientAddress, clientPort);

            clientSocket.close();
        } catch (IOException e) {
            System.err.println("IOException while handling client connection. ");

        }
    }

    private void processData(BufferedReader reader, PrintWriter writer, InetAddress clientAddress, int clientPort) throws IOException {
        String inputData;
        while ((inputData = reader.readLine()) != null) {
            System.out.println("Received from client " + clientAddress + ":" + clientPort + " : " + inputData);
            writer.println(inputData);
        }
    }
}