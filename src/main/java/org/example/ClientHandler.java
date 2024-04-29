package org.example;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    public Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private SimpleServer server;
    private ClientInformation clientInformation;


    ClientHandler(Socket socket, SimpleServer server, ClientInformation clientInformation) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.server = server;
        this.clientInformation = clientInformation;

    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    if (message.startsWith("@")) {
                        int charId = message.indexOf(":");
                        if(charId == -1){
                            sendMessage("Incorrect message");
                        } else {
                        String privateMessage = message.substring(charId + 1);
                        String username = message.substring(1, charId);
                        server.sendPrivateMessage(privateMessage, clientInformation.getId(),username);
                        }
                    } else {
                        System.out.println("Received message from " + clientInformation.getClientName() + ": " + message);
                        server.broadcastMessage(message, this);
                        if (message.equalsIgnoreCase("exit")) {
                            break;
                        }
                    }

                }
            }
            socket.close();
            server.removeClientHandler(this);
            System.out.println("client " + clientInformation.getClientName());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public ClientInformation getClientInformation() {
        return clientInformation;
    }
}
