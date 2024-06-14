package com.savadanko.server.database.collections;

import com.savadanko.common.models.Flat;
import com.savadanko.common.models.User;
import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.Tables;

import java.util.LinkedHashMap;

public class CollectionHandler implements ICollectionHandler{
    private final FlatCollection flatCollection;
    private final UserCollection userCollection;

    public CollectionHandler() {
        this.flatCollection = new FlatCollection();
        this.userCollection = new UserCollection();
    }

    @Override
    public void fill(ISQLDataBase dataBase) {
        flatCollection.fill(dataBase);
        userCollection.fill(dataBase);
        System.out.println("FlatCollection and UserCollection have been filled.");
    }

    @Override
    public void create(long id, Object object, Tables collections) {
        if (collections.equals(Tables.FLATS)){
            Flat flat = (Flat) object;
            flat.setId(id);
            flatCollection.create(flat);
        }
        else if (collections.equals(Tables.USERS)){
            User user = (User) object;
            user.setId(id);
            userCollection.create(object);
        }
    }

    @Override
    public Object read(long id, Tables collections) {
        if (collections.equals(Tables.FLATS)){
            return flatCollection.read(id);
        }
        else if (collections.equals(Tables.USERS)){
            return userCollection.read(id);
        }
        return null;
    }

    @Override
    public LinkedHashMap<Long, Object> readAll(Tables collections) {
        if (collections.equals(Tables.FLATS)){
            return flatCollection.readAll();
        }
        else if (collections.equals(Tables.USERS)){
            return userCollection.readAll();
        }
        return null;
    }

    @Override
    public void update(long id, Object object, Tables collections) {
        if (collections.equals(Tables.FLATS)){
            flatCollection.update(id, object);
        }
        else if (collections.equals(Tables.USERS)){
            userCollection.update(id, object);
        }
    }

    @Override
    public void delete(long id, Tables collections) {
        if (collections.equals(Tables.FLATS)){
            flatCollection.delete(id);
        }
        else if (collections.equals(Tables.USERS)){
            userCollection.delete(id);
        }
    }
}
