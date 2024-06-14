package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class UpdateCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(UpdateCommand.class);
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        long flatId;

        try {
            flatId = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            log.error("Invalid ID format: {}", args[0], e);
            return new CommandResponse("Invalid ID format");
        }

        Flat existingFlat = (Flat) dataBaseHandler.read(flatId, Tables.FLATS);
        if (existingFlat == null) {
            return new CommandResponse("There is no object with this ID");
        }

        if (!existingFlat.getOwner().equals(userLogin)) {
            return new CommandResponse("You can't update an object, it doesn't belong to you");
        }

        dataBaseHandler.update(flatId, flat, Tables.FLATS);
        log.info("Flat with ID {} updated by user {}", flatId, userLogin);
        return new CommandResponse("Update object successfully");
    }
}
