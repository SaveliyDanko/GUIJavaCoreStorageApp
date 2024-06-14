package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;

@AllArgsConstructor
public class MinByNameCommand implements Command{
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        ArrayList<String> list = new ArrayList<>();

        if (dataBaseHandler.readAll(Tables.FLATS).isEmpty()){
            return new CommandResponse("Data Base is empty");
        }

        for (Object o : dataBaseHandler.readAll(Tables.FLATS).values()){
            Flat f = (Flat) o;
            list.add(f.getName());
        }

        String minName = Collections.min(list);
        return new CommandResponse("Min name = " + minName);
    }
}
