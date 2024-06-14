package com.savadanko.client;

// java --module-path lib --add-modules javafx.controls,javafx.fxml -cp target/Client-1.0-SNAPSHOT.jar com.savadanko.client.ClientApp

import com.savadanko.client.network.PortScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        showPortScene(primaryStage);
    }

    private void showPortScene(Stage stage){
        PortScene loginScene = new PortScene(stage);
        stage.setTitle("Port Scene");
        stage.setScene(loginScene.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}