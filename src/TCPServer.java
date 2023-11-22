import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    protected static final int DEFAULT_PORT = 8080;
    ServerSocket serverSocket;


    public static void main(String[] args) throws IOException {
        int port = TCPServer.DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port 8080.");
            }
        }

        TCPServer server = new TCPServer(port);
        server.launch();
    }

    public TCPServer(int port) throws IOException {
        serverSocket= new ServerSocket(port);
        System.out.println(TCPServer.class.getSimpleName() + " is listening on port " + port);
    }
    public TCPServer() throws IOException {
        this(DEFAULT_PORT);
    }

    public void launch() {
        try {


            while (true) {
                Socket clientSocket = serverSocket.accept();
                ConnectionThread connectionThread = new ConnectionThread(clientSocket);
                connectionThread.start();

                int activeThreads = Thread.activeCount();
                System.out.println("Estimated number of active threads in the thread group: " + activeThreads);
            }
        } catch (IOException e) {
            System.err.println("IOException while accepting client connection. ");

        }
    }
}