package com.savadanko.server.database.sql;

import java.sql.Connection;

public interface ConnectionManager {
    void connect(String url, String user, String pass);
    void disconnect();
    Connection getConnection();
}
