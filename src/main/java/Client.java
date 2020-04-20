import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {


    JButton send;
    JFrame frame;
    JPanel panel;
    JTextField textSend;
    JTextArea textChat;
    JLabel status;
    Socket sock;
    PrintWriter writer;
    BufferedReader reader;

    public static void main(String[] args) {
        new Client().GUI();
    }

    public void GUI() {
        frame = new JFrame();
        send = new JButton("Send");
        panel = new JPanel();
        textSend = new JTextField(20);
        status = new JLabel("Not connected");

        textChat = new JTextArea(15, 50);
        textChat.setLineWrap(true);
        textChat.setWrapStyleWord(true);
        textChat.setEditable(false);
        JScrollPane chatScrool = new JScrollPane(textChat);
        chatScrool.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrool.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        send.addActionListener(new SendListener());
        panel.add(chatScrool);
        panel.add(textSend);
        panel.add(send);
        panel.add(status);
        frame.getContentPane().add(BorderLayout.CENTER, panel);


        setUpNetworking();
        Thread listenClient = new Thread(new IncomingReader());
        listenClient.start();

        frame.setSize(610, 350);
        frame.setVisible(true);

    }

    public class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (sock != null) {
                try {
                    writer.println(textSend.getText());
                    writer.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                textSend.setText("");
                textSend.requestFocus();
            }
        }
    }

    private void setUpNetworking() {
        try {
            sock = new Socket("127.0.0.1", 5000);
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            writer = new PrintWriter(sock.getOutputStream());
            status.setText("Connected to server");

        } catch (IOException e) {
            status.setText("Error connected to server");
        }
    }

    public class IncomingReader implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    if (!message.equals("")) {
                        textChat.append(message + "\n");
                    }
                }
            } catch (Exception ex) {

            }
        }
    }

}
