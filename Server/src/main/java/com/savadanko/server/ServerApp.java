package com.savadanko.server;

import com.savadanko.server.database.DataBaseHandler;

public class ServerApp {
    public static void main(String[] args) {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        dataBaseHandler.connect();
        try {
//            if (args.length != 1){
//                throw new Exception("Invalid args");
//            }
//            int port = Integer.parseInt(args[0]);
            int port = 8888;
            ServerManager serverManager = new ServerManager(port);
            serverManager.start();
            Runtime.getRuntime().addShutdownHook(new Thread(serverManager::stop));
        } catch (Exception e) {
            System.err.println("Failed to start the server: " + e.getMessage());
        }
    }
}
