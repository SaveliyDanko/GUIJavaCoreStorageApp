package com.savadanko.server.database.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class SQLDataBase implements ISQLDataBase{
    private static final Logger logger = LogManager.getLogger(SQLDataBase.class);

    private static SQLDataBase instance;
    private Connection connection;
    private final SQLObjectHandler objectHandler;
    private final ConnectionManager connectionManager;


    private SQLDataBase(SQLObjectHandler objectHandler){
        this.objectHandler = objectHandler;
        this.connectionManager = new SQLConnectionManager();
    }

    public static synchronized SQLDataBase getInstance(SQLObjectHandler objectHandler){
        if (instance == null){
            instance = new SQLDataBase(objectHandler);
        }
        return instance;
    }

    @Override
    public void connect(String url, String user, String pass) {
        try{
            if (connection == null || connection.isClosed()){
                connectionManager.connect(url, user, pass);
                connection = connectionManager.getConnection();
                objectHandler.setConnection(connection);
                logger.info("Data Base connecting successfully");
                objectHandler.createTables();
                logger.info("Create tables successfully");
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public void disconnect(){
        connectionManager.disconnect();
        logger.info("Data Base disconnecting successfully");
    }

    @Override
    public long create(Object object, Tables table) {
        long id = objectHandler.createModel(object, table);
        logger.info("Add item successfully");
        return id;
    }

    @Override
    public Object read(long id, Tables table) {
        logger.info("Read object with id = {}", id);
        return objectHandler.readModel(id, table);
    }

    @Override
    public LinkedHashMap<Long, Object> readAll(Tables tables) {
        return objectHandler.readAll(tables);
    }

    @Override
    public void update(long id, Object object, Tables table) {
        objectHandler.updateModel(id, object, table);
        logger.info("Update object with id = {} successfully", id);
    }

    @Override
    public void delete(long id, Tables table) {
        objectHandler.deleteModel(id, table);
        logger.info("Deleted object with id = {} successfully", id);
    }
}
