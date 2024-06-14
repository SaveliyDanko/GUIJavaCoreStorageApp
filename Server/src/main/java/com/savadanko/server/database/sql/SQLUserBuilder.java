package com.savadanko.server.database.sql;

import com.savadanko.common.models.User;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedHashMap;

@Setter
public class SQLUserBuilder implements ModelBuilder<User> {
    private static final Logger logger = LogManager.getLogger(SQLUserBuilder.class);

    private Connection connection;

    public SQLUserBuilder() {
    }

    public void createTables() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY," +
                "login VARCHAR(128) UNIQUE," +
                "password BYTEA," +
                "salt BYTEA);"; // Добавлено поле salt

        try (PreparedStatement statement = connection.prepareStatement(usersTable)) {
            statement.execute();
        } catch (SQLException e) {
            logger.error("Error creating table: {}", e.getMessage(), e);
        }
    }

    @Override
    public long createModel(User user) {
        String query = "INSERT INTO users (login, password, salt) VALUES (?, ?, ?);"; // Добавлено поле salt

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLogin());
            statement.setBytes(2, user.getPasswordHash());
            statement.setBytes(3, user.getSalt()); // Установка значения salt

            statement.executeUpdate();

            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    long userId = generatedKeys.getLong(1);
                    user.setId(userId);
                    return userId;
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving object: {}", e.getMessage());
        }
        return 0;
    }

    @Override
    public User readModel(long id) {
        String query = "SELECT * FROM users WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    long userId = resultSet.getLong("id");
                    String login = resultSet.getString("login");
                    byte[] passwordHash = resultSet.getBytes("password");
                    byte[] salt = resultSet.getBytes("salt"); // Чтение значения salt
                    return new User(userId, login, passwordHash, salt);
                }
            }
        } catch (SQLException e) {
            logger.error("Error reading object: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public LinkedHashMap<Long, User> readAll() {
        String query = "SELECT * FROM users;";

        LinkedHashMap<Long, User> linkedHashMap = new LinkedHashMap<>();

        try(PreparedStatement statement = connection.prepareStatement(query)){
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    long userId = resultSet.getLong("id");
                    String login = resultSet.getString("login");
                    byte[] passwordHash = resultSet.getBytes("password");
                    byte[] salt = resultSet.getBytes("salt"); // Чтение значения salt

                    User user = new User(userId, login, passwordHash, salt);

                    linkedHashMap.put(userId, user);
                }
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
        return linkedHashMap;
    }

    @Override
    public void updateModel(long id, User user) {
        String query = "UPDATE users SET login = ?, password = ?, salt = ? WHERE id = ?"; // Добавлено поле salt

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getLogin());
            statement.setBytes(2, user.getPasswordHash());
            statement.setBytes(3, user.getSalt()); // Установка значения salt
            statement.setLong(4, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating object: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteModel(long id) {
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting object: {}", e.getMessage(), e);
        }
    }
}
