package com.savadanko.server.database.collections;

import com.savadanko.common.models.Flat;
import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.Tables;

import java.util.LinkedHashMap;

public class FlatCollection implements CrudCollection {
    private LinkedHashMap<Long, Object> flatCollection;

    public FlatCollection() {
        this.flatCollection = new LinkedHashMap<>();
    }

    @Override
    public void fill(ISQLDataBase dataBase) {
        flatCollection = dataBase.readAll(Tables.FLATS);
        if (flatCollection == null) {
            flatCollection = new LinkedHashMap<>();
        }
        System.out.println("FlatCollection has been filled: " + flatCollection.size() + " items.");
    }

    @Override
    public void create(Object object) {
        Flat flat = (Flat) object;
        flatCollection.put(flat.getId(), flat);
        System.out.println("Flat with ID " + flat.getId() + " was added to the collection.");
    }

    @Override
    public Object read(long id) {
        return flatCollection.get(id);
    }

    @Override
    public LinkedHashMap<Long, Object> readAll() {
        return flatCollection;
    }

    @Override
    public void update(long id, Object object) {
        Flat flat = (Flat) object;
        flatCollection.put(id, flat);
        System.out.println("Flat with ID " + id + " was updated in the collection.");
    }

    @Override
    public void delete(long id) {
        if (flatCollection.remove(id) != null) {
            System.out.println("Flat with ID " + id + " was successfully removed from the collection.");
        } else {
            System.out.println("Flat with ID " + id + " was not found in the collection.");
        }
    }
}
