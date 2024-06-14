package com.savadanko.server.command;

import com.savadanko.common.dto.CommandProperties;
import com.savadanko.server.command.commands.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CommandFactory {
    private final Map<String, Class<? extends Command>> commandMap;
    private final Map<String, CommandProperties> commandPropertiesMap;


    public CommandFactory() {
        this.commandMap = new HashMap<>();
        this.commandPropertiesMap = new HashMap<>();

        commandMap.put(ECommand.HELP.getName(), HelpCommand.class);
        commandMap.put(ECommand.INSERT.getName(), InsertCommand.class);
        commandMap.put(ECommand.REMOVE_KEY.getName(), DeleteCommand.class);
        commandMap.put(ECommand.SHOW.getName(), ShowCommand.class);
        commandMap.put(ECommand.INFO.getName(), InfoCommand.class);
        commandMap.put(ECommand.UPDATE.getName(), UpdateCommand.class);
        commandMap.put(ECommand.CLEAR.getName(), ClearCommand.class);
        commandMap.put(ECommand.HISTORY.getName(), HistoryCommand.class);
        commandMap.put(ECommand.FILTER_STARTS_WITH_NAME.getName(), FilterStartsWithNameCommand.class);
        commandMap.put(ECommand.REMOVE_GREATER_KEY.getName(), RemoveGreaterKey.class);
        commandMap.put(ECommand.REMOVE_LOWER.getName(), RemoveLower.class);
        commandMap.put(ECommand.COUNT_BY_TRANSPORT.getName(), CountByTransportCommand.class);
        commandMap.put(ECommand.MIN_BY_NAME.getName(), MinByNameCommand.class);
        commandMap.put(ECommand.SYNC.getName(), SyncCommand.class);

        commandPropertiesMap.put(ECommand.HELP.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.INSERT.getName(), new CommandProperties(0, true));
        commandPropertiesMap.put(ECommand.REMOVE_KEY.getName(), new CommandProperties(1, false));
        commandPropertiesMap.put(ECommand.SHOW.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.INFO.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.UPDATE.getName(), new CommandProperties(1, true));
        commandPropertiesMap.put(ECommand.CLEAR.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.HISTORY.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.FILTER_STARTS_WITH_NAME.getName(), new CommandProperties(1, false));
        commandPropertiesMap.put(ECommand.REMOVE_GREATER_KEY.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.REMOVE_LOWER.getName(), new CommandProperties(0, false));
        commandPropertiesMap.put(ECommand.COUNT_BY_TRANSPORT.getName(), new CommandProperties(0, true));
        commandPropertiesMap.put(ECommand.MIN_BY_NAME.getName(), new CommandProperties(0, true));
        commandPropertiesMap.put(ECommand.SYNC.getName(), new CommandProperties(0, false));
    }

}
