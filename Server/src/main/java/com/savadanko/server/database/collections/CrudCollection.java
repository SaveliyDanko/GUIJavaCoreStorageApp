package com.savadanko.server.database.collections;

import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.SQLDataBase;

import java.util.LinkedHashMap;

public interface CrudCollection {
    void fill(ISQLDataBase dataBase);
    void create(Object object);
    Object read(long id);
    LinkedHashMap<Long, Object> readAll();
    void update(long id, Object object);
    void delete(long id);
}
