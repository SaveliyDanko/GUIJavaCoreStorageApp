package com.savadanko.server.command;

import lombok.Getter;

@Getter
public enum ECommand {
    HELP("help", "вывести справку по доступным командам"),
    INFO("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)"),
    SHOW("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении"),
    INSERT("insert", "добавить новый элемент с заданным ключом"),
    UPDATE("update", "обновить значение элемента коллекции, id которого равен заданному"),
    REMOVE_KEY("remove_key", "удалить элемент из коллекции по его ключу"),
    CLEAR("clear", "очистить коллекцию"),
    EXECUTE_SCRIPT("execute_script", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."),
    EXIT("exit", "завершить программу"),
    REMOVE_LOWER("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный"),
    HISTORY("history", "вывести последние 12 команд (без их аргументов)"),
    REMOVE_GREATER_KEY("remove_greater_key", "удалить из коллекции все элементы, ключ которых превышает заданный"),
    MIN_BY_NAME("min_by_name", "вывести любой объект из коллекции, значение поля name которого является минимальным"),
    COUNT_BY_TRANSPORT("count_by_transport", "вывести количество элементов, значение поля transport которых равно заданному"),
    FILTER_STARTS_WITH_NAME("filter_starts_with_name", "вывести элементы, значение поля name которых начинается с заданной подстроки"),
    SYNC("sync", "команда для синхронизации с базой данных");

    private final String name;
    private final String description;

    ECommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
