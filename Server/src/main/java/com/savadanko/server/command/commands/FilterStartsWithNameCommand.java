package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class FilterStartsWithNameCommand implements Command{
    private static final Logger log = LoggerFactory.getLogger(DeleteCommand.class);
    private final String[] args;
    private final Flat flat;
    private final String ownerLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        StringBuilder sb = new StringBuilder();
        boolean isEmpty = true;

        String pattern = args[0];
        for (Object o : dataBaseHandler.readAll(Tables.FLATS).values()){
            Flat f = (Flat) o;
            if (f.getName().startsWith(pattern)){
                sb.append(f.toString()).append("\n");
                isEmpty = false;
            }
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
