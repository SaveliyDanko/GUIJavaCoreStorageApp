package com.savadanko.client.network;

import com.savadanko.client.network.authorization.LoginScene;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class PortScene {
    private final Stage stage;
    @Getter
    private Scene scene;
    private ResourceBundle bundle;
    private Label label1;
    private Label warningPort;
    private Label warningConnection;
    private TextField textField;
    private Button button;

    public PortScene(Stage stage) {
        this.stage = stage;
        loadLocale(new Locale("en", "US")); // Default locale
        createScene();
    }

    private void createScene() {
        // Create the VBox for the UI elements
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        // Locale selection
        ComboBox<String> localeSelector = createLocaleSelector();

        label1 = new Label(bundle.getString("enter_port"));
        warningPort = new Label();
        warningConnection = new Label();

        textField = new TextField();
        textField.setPromptText(bundle.getString("enter_port"));

        button = getButton();

        vBox.getChildren().addAll(localeSelector, label1, textField, button, warningPort, warningConnection);

        // Create the background image
        Image backgroundImage = new Image("painting-mountain-lake-with-mountain-background.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth());
        backgroundImageView.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight());
        backgroundImageView.setPreserveRatio(false);

        // Create the StackPane to hold the background image and the VBox
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, vBox);

        // Create the scene with the StackPane
        scene = new Scene(stackPane);

        // Get screen size and set stage size
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        // Set the scene to the stage
        stage.setScene(scene);

        // Remove "Press ESC to exit full screen" message
        stage.setFullScreenExitHint("");

        // Set full screen and maximize
        stage.setFullScreen(true);
        stage.setMaximized(true);
        stage.show();
    }

    private ComboBox<String> createLocaleSelector() {
        ComboBox<String> localeSelector = new ComboBox<>();
        localeSelector.getItems().addAll("English", "Русский", "Polski", "Slovenský");
        localeSelector.setValue("English");

        localeSelector.setOnAction(event -> {
            String selectedLocale = localeSelector.getValue();
            switch (selectedLocale) {
                case "English":
                    loadLocale(new Locale("en", "US"));
                    break;
                case "Русский":
                    loadLocale(new Locale("ru", "RU"));
                    break;
                case "Polski":
                    loadLocale(new Locale("pl", "PL"));
                    break;
                case "Slovenský":
                    loadLocale(new Locale("sk", "SK"));
                    break;
            }
            updateTexts();
        });

        return localeSelector;
    }

    private void loadLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("locale", locale, new UTF8Control());
    }

    private void updateTexts() {
        label1.setText(bundle.getString("enter_port"));
        textField.setPromptText(bundle.getString("enter_port"));
        button.setText(bundle.getString("connect"));
        warningPort.setText("");
        warningConnection.setText("");
    }

    private Button getButton() {
        Button button = new Button(bundle.getString("connect"));

        button.setOnAction(actionEvent -> {
            String port = textField.getText();
            if (isNumeric(port)) {
                warningPort.setText(bundle.getString("valid_port") + port);
                warningPort.setStyle("-fx-text-fill: green;");
                try {
                    Socket socket = new Socket("localhost", Integer.parseInt(port));
                    LoginScene loginScene = new LoginScene(
                            stage,
                            scene,
                            new ObjectOutputStream(socket.getOutputStream()),
                            new ObjectInputStream(socket.getInputStream()),
                            bundle);

                    stage.setTitle("Login");
                    stage.setScene(loginScene.getScene());
                } catch (IOException e) {
                    warningConnection.setText(bundle.getString("invalid_connection"));
                    warningConnection.setStyle("-fx-text-fill: red;");
                }
            } else {
                warningPort.setText(bundle.getString("invalid_input"));
                warningPort.setStyle("-fx-text-fill: red;");
            }
        });
        return button;
    }

    // Метод для проверки, является ли строка числом
    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static class UTF8Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(
                String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle;
            try (InputStreamReader reader = new InputStreamReader(loader.getResourceAsStream(resourceName), StandardCharsets.UTF_8)) {
                bundle = new PropertyResourceBundle(reader);
            }
            return bundle;
        }
    }
}
