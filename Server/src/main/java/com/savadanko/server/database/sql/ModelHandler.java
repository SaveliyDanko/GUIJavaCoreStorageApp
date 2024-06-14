package com.savadanko.server.database.sql;

import java.util.LinkedHashMap;

public interface ModelHandler {
    void createTables();
    long createModel(Object obj, Tables table);
    Object readModel(long id, Tables table);
    LinkedHashMap<Long, Object> readAll(Tables tables);
    void updateModel(long id, Object obj, Tables table);
    void deleteModel(long id, Tables table);
}
