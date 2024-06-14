package com.savadanko.server.database.collections;

import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.Tables;

import java.util.LinkedHashMap;

public interface ICollectionHandler {
    void fill(ISQLDataBase dataBase);
    void create(long id, Object object, Tables collections);
    Object read(long id, Tables collections);
    LinkedHashMap<Long, Object> readAll(Tables collections);
    void update(long id, Object object, Tables collections);
    void delete(long id, Tables collections);
}
