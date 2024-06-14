package com.savadanko.server.database.sql;

import java.util.LinkedHashMap;

public interface ISQLDataBase {
    void connect(String url, String user, String pass);
    void disconnect();
    long create(Object object, Tables tables);
    Object read(long id, Tables table);
    LinkedHashMap<Long, Object> readAll(Tables tables);
    void update(long id, Object object, Tables table);
    void delete(long id, Tables table);
}
