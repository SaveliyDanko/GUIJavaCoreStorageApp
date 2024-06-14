package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

import java.util.LinkedHashMap;

@AllArgsConstructor
public class RemoveGreaterKey implements Command{
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        LinkedHashMap<Long, Object> map = dataBaseHandler.readAll(Tables.FLATS);

        if (map.isEmpty()) {
            return new CommandResponse("The map is empty. Nothing to remove.");
        }

        Long maxKey = null;
        for (Long key : map.keySet()) {
            if (maxKey == null || key > maxKey) {
                maxKey = key;
            }
        }

        if (maxKey != null) {
            Flat f = (Flat) dataBaseHandler.read(maxKey, Tables.FLATS);
            if (f.getOwner().equals(userLogin)){
                dataBaseHandler.delete(maxKey, Tables.FLATS);
                return new CommandResponse("Remove key with key = " + maxKey + " successfully");
            }
            else return new CommandResponse("You can't delete an object, it doesn't belong to you");
        } else {
            return new CommandResponse("No element with a lower key found.");
        }
    }
}
