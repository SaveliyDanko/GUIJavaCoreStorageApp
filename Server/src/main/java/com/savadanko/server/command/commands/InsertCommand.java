package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InsertCommand implements Command{
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        dataBaseHandler.create(flat, Tables.FLATS);
        return new CommandResponse("Insert flat successfully");
    }
}
