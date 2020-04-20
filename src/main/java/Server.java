import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try {
            ServerSocket serverSock = new ServerSocket(5000);

            while (true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();

                writer.println("server a live");
            }

        } catch (IOException ex) {

        }

    }

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        PrintWriter writer;
        Socket sock;

        public ClientHandler(Socket clientSock) {
            try {
                this.sock = clientSock;
                this.reader = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
                this.writer = new PrintWriter(clientSock.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String messageClients;
            try {
                while ((messageClients = reader.readLine()) != null) {
                    writer.println(messageClients);
                    writer.flush();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
