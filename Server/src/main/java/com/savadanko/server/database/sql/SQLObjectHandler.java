package com.savadanko.server.database.sql;

import com.savadanko.common.models.Flat;
import com.savadanko.common.models.User;

import java.sql.Connection;
import java.util.LinkedHashMap;

public class SQLObjectHandler implements ModelHandler{
    private final SQLFlatBuilder flatBuilder;
    private final SQLUserBuilder userBuilder;

    public SQLObjectHandler() {
        this.flatBuilder = new SQLFlatBuilder();
        this.userBuilder = new SQLUserBuilder();
    }

    public void setConnection(Connection connection){
        flatBuilder.setConnection(connection);
        userBuilder.setConnection(connection);
    }

    @Override
    public void createTables() {
        userBuilder.createTables();
        flatBuilder.createTables();
    }

    @Override
    public long createModel(Object obj, Tables table) {
        if (table.equals(Tables.FLATS)){
            return flatBuilder.createModel((Flat) obj);
        }
        else if (table.equals(Tables.USERS)){
            return userBuilder.createModel((User) obj);
        }
        return 0;
    }

    @Override
    public Object readModel(long id, Tables table) {
        if (table.equals(Tables.FLATS)){
            return flatBuilder.readModel(id);
        }
        else if (table.equals(Tables.USERS)){
            return userBuilder.readModel(id);
        }
        return null;
    }

    @Override
    public LinkedHashMap<Long, Object> readAll(Tables table) {
        if (table.equals(Tables.FLATS)){
            return new LinkedHashMap<>(flatBuilder.readAll());
        }
        else if (table.equals(Tables.USERS)){
            return new LinkedHashMap<>(userBuilder.readAll());
        }
        return null;
    }

    @Override
    public void updateModel(long id, Object obj, Tables tables) {
        if (tables.equals(Tables.FLATS)){
            flatBuilder.updateModel(id, (Flat) obj);
        }
        else if (tables.equals(Tables.USERS)){
            userBuilder.updateModel(id, (User) obj);
        }
    }

    @Override
    public void deleteModel(long id, Tables table) {
        if (table.equals(Tables.FLATS)){
            flatBuilder.deleteModel(id);
        }
        else if (table.equals(Tables.USERS)){
            userBuilder.deleteModel(id);
        }
    }
}
