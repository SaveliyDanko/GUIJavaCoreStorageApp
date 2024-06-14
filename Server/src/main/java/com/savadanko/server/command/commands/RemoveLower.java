package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

import java.util.LinkedHashMap;

@AllArgsConstructor
public class RemoveLower implements Command {
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

        Long minKey = null;
        for (Long key : map.keySet()) {
            if (minKey == null || key < minKey) {
                minKey = key;
            }
        }

        if (minKey != null) {
            Flat f = (Flat) dataBaseHandler.read(minKey, Tables.FLATS);
            if (f.getOwner().equals(userLogin)){
                dataBaseHandler.delete(minKey, Tables.FLATS);
                return new CommandResponse("Remove key with key = " + minKey + " successfully");
            }
            else return new CommandResponse("You can't delete an object, it doesn't belong to you");
        } else {
            return new CommandResponse("No element with a lower key found.");
        }
    }
}
