package com.savadanko.server.database;

import com.savadanko.server.database.sql.Tables;

import java.util.LinkedHashMap;

public interface IDataBaseHandler {
    void connect();
    void disconnect();
    long create(Object object, Tables table);
    Object read(long id, Tables table);
    LinkedHashMap<Long, Object> readAll(Tables tables);
    void update(long id, Object object, Tables table);
    void delete(long id, Tables table);

}
