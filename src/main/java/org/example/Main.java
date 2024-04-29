package org.example;

public class Main {
    public static void main(String[] args) {
        int port = 12345;

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgresql";

        DatabaseConnector databaseConnector = new DatabaseConnector(url, user, password);
        SimpleServer server = new SimpleServer(port,databaseConnector);
        server.start();
    }
}