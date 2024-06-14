package com.savadanko.server.command.commands;

import com.savadanko.server.command.CommandResponse;

public interface Command {
    CommandResponse execute();
}
