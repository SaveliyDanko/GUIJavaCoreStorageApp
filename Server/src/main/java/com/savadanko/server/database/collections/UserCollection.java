package com.savadanko.server.database.collections;

import com.savadanko.common.models.User;
import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.Tables;

import java.util.LinkedHashMap;

public class UserCollection implements CrudCollection {
    private LinkedHashMap<Long, Object> userCollection;

    public UserCollection() {
        this.userCollection = new LinkedHashMap<>();
    }

    @Override
    public void fill(ISQLDataBase dataBase) {
        userCollection = dataBase.readAll(Tables.USERS);
        if (userCollection == null) {
            userCollection = new LinkedHashMap<>();
        }
        System.out.println("UserCollection has been filled: " + userCollection.size() + " items.");
    }

    @Override
    public void create(Object object) {
        User user = (User) object;
        userCollection.put(user.getId(), user);
        System.out.println("User with ID " + user.getId() + " was added to the collection.");
    }

    @Override
    public Object read(long id) {
        return userCollection.get(id);
    }

    @Override
    public LinkedHashMap<Long, Object> readAll() {
        return userCollection;
    }

    @Override
    public void update(long id, Object object) {
        User user = (User) object;
        userCollection.put(id, user);
        System.out.println("User with ID " + id + " was updated in the collection.");
    }

    @Override
    public void delete(long id) {
        if (userCollection.remove(id) != null) {
            System.out.println("User with ID " + id + " was successfully removed from the collection.");
        } else {
            System.out.println("User with ID " + id + " was not found in the collection.");
        }
    }
}
