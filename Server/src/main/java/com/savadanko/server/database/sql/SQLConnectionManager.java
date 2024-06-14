package com.savadanko.server.database.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnectionManager implements ConnectionManager{
    private static final Logger logger = LoggerFactory.getLogger(SQLConnectionManager.class);

    private Connection connection;

    @Override
    public void connect(String url, String user, String pass) {
        try {
            connection = DriverManager.getConnection(url, user, pass);
            logger.info("Connected to the database successfully.");
        } catch (SQLException e) {
            logger.error("Failed to connect to the database: {}", e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Disconnected from the database successfully.");
            }
        } catch (SQLException e) {
            logger.error("Failed to disconnect from the database: {}", e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
