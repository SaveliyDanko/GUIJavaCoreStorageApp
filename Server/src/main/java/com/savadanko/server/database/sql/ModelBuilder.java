package com.savadanko.server.database.sql;

import java.util.LinkedHashMap;

public interface ModelBuilder<T> {
    void createTables();
    long createModel(T obj);
    T readModel(long id);
    LinkedHashMap<Long, T> readAll();
    void updateModel(long id, T obj);
    void deleteModel(long id);
}

