package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private Connection connection;

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    public int getClientIdByIpAddress(String ipAddress) throws SQLException {
        int clientId = -1;
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM clients WHERE ip_address = ?")) {
            statement.setString(1, ipAddress);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                clientId = resultSet.getInt("id");
            }
        }
        return clientId;
    }

    public boolean authenticate(String name, String password) throws SQLException {
        boolean isAuthenticated = false;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM clients WHERE name = ? AND password = ?")) {
            statement.setString(1, name);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isAuthenticated = true;
            }
        }
        return isAuthenticated;
    }

    public int getIdByUsername(String username) throws SQLException {
        int clientId = -1;
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM clients WHERE name = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                clientId = resultSet.getInt("id");
            }
        }
        return clientId;
    }

    public List<String> getMessagesByUsername(String name) throws SQLException {
        List<String> messages = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT content FROM messages WHERE sender_id = (SELECT id FROM clients WHERE name = ?)")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                messages.add(resultSet.getString("content"));
            }
        }
        return messages;
    }
}