package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageDAO {

    private Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    public void saveMessage(String message, int senderId ) throws SQLException {
        /*try (PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (sender_id, content) VALUES (?, ?)")) {
            statement.setInt(1, senderId);
            statement.setString(2, message);
            statement.executeUpdate();
        }*/
    }
    public void savePrivateMessage( String message,int senderId, int receiverId) throws SQLException {

    }
}