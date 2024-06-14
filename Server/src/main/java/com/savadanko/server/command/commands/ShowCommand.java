package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShowCommand implements Command {
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        StringBuilder sb = new StringBuilder();
        boolean isEmpty = true;

        for (Object o : dataBaseHandler.readAll(Tables.FLATS).values()) {
            Flat f = (Flat) o;
            sb.append(f.toString()).append("\n");
            isEmpty = false;
        }

        if (isEmpty) {
            return new CommandResponse("The collection is empty.");
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return new CommandResponse(sb.toString());
    }
}
