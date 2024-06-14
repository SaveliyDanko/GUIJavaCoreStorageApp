package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandResponse;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.Tables;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class DeleteCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(DeleteCommand.class);
    private final String[] args;
    private final Flat flat;
    private final String ownerLogin;

    @Override
    public CommandResponse execute() {
        if (args.length == 0) {
            return new CommandResponse("No ID provided for deletion");
        }

        try {
            long flatId = Long.parseLong(args[0]);
            DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
            Flat f = (Flat) dataBaseHandler.read(flatId, Tables.FLATS);

            if (f == null) {
                log.warn("Flat with ID {} not found", flatId);
                return new CommandResponse("There is no object with this ID");
            }

            if (!f.getOwner().equals(ownerLogin)) {
                log.warn("User {} attempted to delete flat owned by {}", ownerLogin, f.getOwner());
                return new CommandResponse("You can't delete an object, it doesn't belong to you");
            }

            dataBaseHandler.delete(flatId, Tables.FLATS);
            log.info("Flat with ID {} deleted by user {}", flatId, ownerLogin);
            return new CommandResponse("Delete flat successfully");
        } catch (NumberFormatException e) {
            log.error("Invalid ID format: {}", args[0], e);
            return new CommandResponse("Invalid ID format");
        } catch (Exception e) {
            log.error("Error deleting flat with ID {}: {}", args[0], e.getMessage(), e);
            return new CommandResponse("Error deleting flat");
        }
    }
}
