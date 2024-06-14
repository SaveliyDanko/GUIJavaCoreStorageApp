package com.savadanko.server.command.commands;

import com.savadanko.common.models.Flat;
import com.savadanko.server.command.CommandHistory;
import com.savadanko.server.command.CommandResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HistoryCommand implements Command{
    private final String[] args;
    private final Flat flat;
    private final String userLogin;

    @Override
    public CommandResponse execute() {
        CommandHistory commandHistory = CommandHistory.getInstance();
        StringBuilder sb = new StringBuilder();
        for (String item : commandHistory.getHistory()){
            sb.append(item).append("\n");
        }
        return new CommandResponse(sb.toString());
    }
}
