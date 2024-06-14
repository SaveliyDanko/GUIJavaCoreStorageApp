package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CountByTransportCommand implements Command{
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        int c = 0;
        for (Object o : dataBaseHandler.readAll(Tables.FLATS).values()){
            Flat f = (Flat) o;
            if (flat.getTransport().equals(f.getTransport())){
                c ++;
            }
        }
        return new CommandResponse("Transport with name " + flat.getTransport().toString() + " = " + c);
    }
}
