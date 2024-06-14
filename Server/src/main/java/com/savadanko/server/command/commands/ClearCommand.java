package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ClearCommand implements Command {
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        List<Flat> toRemove = new ArrayList<>();

        for (Object o : dataBaseHandler.readAll(Tables.FLATS).values()) {
            Flat f = (Flat) o;
            if (f.getOwner().equals(userLogin)) {
                toRemove.add(f);
            }
        }

        for (Flat f : toRemove) {
            dataBaseHandler.delete(f.getId(), Tables.FLATS);
        }

        return new CommandResponse("Clear collection successfully");
    }
}
