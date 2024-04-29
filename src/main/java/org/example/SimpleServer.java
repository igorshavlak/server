package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SimpleServer {
    private int port;
    private List<ClientHandler> clientHandlerList = new LinkedList<ClientHandler>();
    private DatabaseConnector databaseConnector;
    private String clientName;
    private MessageDAO messageDAO;
    private ClientDAO clientDAO;
    private ClientInformation clientInformation;




    public SimpleServer(int port, DatabaseConnector databaseConnector) {
        this.port = port;
        this.databaseConnector = databaseConnector;
        this.messageDAO = new MessageDAO(databaseConnector.getConnection());
        this.clientDAO = new ClientDAO(databaseConnector.getConnection());

    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started, port:" + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected " + socket.getInetAddress());
                if(authenticate(socket)) {
                    System.out.println("Client is authenticated");
                    ClientHandler clientHandler = new ClientHandler(socket, this, clientInformation);
                    clientHandlerList.add(clientHandler);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                } else {
                    System.out.println("Authenticate failed");

                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticate(Socket socket) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("Enter your username");
        writer.newLine();
        writer.flush();
        String username = reader.readLine();
        writer.write("Enter your password");
        writer.newLine();
        writer.flush();
        String password = reader.readLine();
        if(clientDAO.authenticate(username,password)) {
            clientInformation = new ClientInformation(username,clientDAO.getMessagesByUsername(username),clientDAO.getIdByUsername(username));
            return clientDAO.authenticate(username, password);
        }
        writer.write("Authenticate failed");
        writer.newLine();
        writer.flush();
        return false;
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException, SQLException {
        messageDAO.saveMessage(message, clientDAO.getIdByUsername(sender.getClientInformation().getClientName()));
        for (ClientHandler client : clientHandlerList) {

            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
    public synchronized void sendPrivateMessage(String message, int senderId, String receiverName) throws IOException, SQLException {
        messageDAO.savePrivateMessage(message,senderId,clientDAO.getIdByUsername(receiverName));
        for(ClientHandler client : clientHandlerList) {
            if(client.getClientInformation().getClientName().equals(receiverName)){
                client.sendMessage(message);
                return;
            }
        }
        System.out.println("Користувач " + receiverName + " відсутній або не підключений.");
    }


    public synchronized void removeClientHandler(ClientHandler clientHandler) {
        clientHandlerList.remove(clientHandler);
        System.out.println("Client has been disconnected");
    }
}

